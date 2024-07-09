package cmpt276.project.marketmimic.controllers;

import java.util.*;
import java.util.stream.Collectors;

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
import jakarta.servlet.http.HttpSession;;

@Controller
@RequestMapping("/api/stocks")
public class apiController {

    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;

    public apiController(RestTemplateBuilder restTemplateBuilder, ApiConfig apiConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiConfig = apiConfig;
    }

    //
    // NOTE: Using this method exceeds the 30 API calls per second limit for Finnhub, so it should be ignored for now
    //
    @GetMapping("/prices")
    public Map<String, Object> getStockPrices(@RequestParam String exchange) {
        String symbolsUrl = "https://finnhub.io/api/v1/stock/symbol?exchange=" + exchange + "&token=" + apiConfig.getApiKey();
        ResponseEntity<List<Map<String, Object>>> symbolsResponse = restTemplate.exchange(symbolsUrl, 
            HttpMethod.GET,
            null, 
            new ParameterizedTypeReference<>() {});

        List<String> symbols = symbolsResponse.getBody().stream()
                .map(symbol -> symbol.get("symbol").toString())
                .collect(Collectors.toList());

        Map<String, Object> stockPrices = new HashMap<>();
        for (String symbol : symbols) {
            String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiConfig.getApiKey();
            @SuppressWarnings("unchecked")
            Map<String, Object> quote = restTemplate.getForObject(quoteUrl, Map.class);
            stockPrices.put(symbol, quote);
        }
        return stockPrices;
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
    public String getStockPrice(@RequestParam String symbol, Model model) {
        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiConfig.getApiKey();
        StockData stockData = restTemplate.getForObject(quoteUrl, StockData.class);
        model.addAttribute("symbol", symbol);
        model.addAttribute("stockData", stockData);
        return "stocksymbol";
    }
}