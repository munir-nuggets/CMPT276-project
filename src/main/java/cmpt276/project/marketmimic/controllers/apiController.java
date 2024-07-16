package cmpt276.project.marketmimic.controllers;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import org.springframework.ui.Model;
import cmpt276.project.marketmimic.config.ApiConfig;
import cmpt276.project.marketmimic.model.*;
import cmpt276.project.marketmimic.service.ChartService;
import cmpt276.project.marketmimic.services.CurrencyService;
import jakarta.servlet.http.HttpSession;;

@Controller
@RequestMapping("/api/stocks")
public class apiController {

    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;
    private final ChartService chartService;

    @Autowired
    private CurrencyService currencyService;

    public apiController(RestTemplateBuilder restTemplateBuilder, ApiConfig apiConfig, ChartService chartService) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiConfig = apiConfig;
        this.chartService = chartService;
    }

    @GetMapping("/")
    public String getStockSymbols(Model model, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if(user == null) return "redirect:/login.html";
        model.addAttribute("user", user);

        String symbolsUrl = "https://finnhub.io/api/v1/stock/symbol?exchange=US&token=" + apiConfig.getApiKey();
        ResponseEntity<List<StockSymbol>> response = restTemplate.exchange(
                symbolsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockSymbol>>() {}
        );
        List<StockSymbol> symbolsResponse = response.getBody();
        Collections.sort(symbolsResponse, Comparator.comparing(StockSymbol::getDescription));
        model.addAttribute("symbols", symbolsResponse);
        return "stocklist";
    }

    @GetMapping("/price")
    public String getStockPrice(@RequestParam String symbol, Model model, HttpSession session) {
        String stockDataUrl = "https://api.polygon.io/v2/aggs/ticker/" + symbol + "/range/1/day/" + getOneYearAgo() + "/" + getCurrentDate() + "?apiKey=" + apiConfig.getPolygonApiKey();
        StockDataResponse stockDataResponse = restTemplate.getForObject(stockDataUrl, StockDataResponse.class);

        try {
            chartService.createStockChart(stockDataResponse, symbol);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiConfig.getApiKey();
        StockData stockData = restTemplate.getForObject(quoteUrl, StockData.class);
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login.html";
        }
        model.addAttribute("usd", currencyService.getCurrencyBalance(user.getUsername()));
        model.addAttribute("symbol", symbol);
        model.addAttribute("stockData", stockData);
        model.addAttribute("user", user);
        return "stocksymbol";
    }

    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    private String getOneYearAgo() {
        return java.time.LocalDate.now().minusYears(1).toString();
    }
}