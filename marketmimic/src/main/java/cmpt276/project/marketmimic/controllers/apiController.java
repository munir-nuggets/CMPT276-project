package cmpt276.project.marketmimic.controllers;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cmpt276.project.marketmimic.config.ApiConfig;

@RestController
@RequestMapping("/api/stocks")
public class apiController {

    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;

    public apiController(RestTemplateBuilder restTemplateBuilder, ApiConfig apiConfig) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiConfig = apiConfig;
    }

    @GetMapping("/prices")
    public Map<String, Object> getStockPrices(@RequestParam String exchange) {
        String symbolsUrl = "https://finnhub.io/api/v1/stock/symbol?exchange=" + exchange + "&token=" + apiConfig.getApiKey();
        ResponseEntity<List<Map<String, Object>>> symbolsResponse = restTemplate.exchange(
                symbolsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<String> symbols = symbolsResponse.getBody().stream()
                .map(symbol -> symbol.get("symbol").toString())
                .collect(Collectors.toList());

        Map<String, Object> stockPrices = new HashMap<>();
        for (String symbol : symbols) {
            String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiConfig.getApiKey();
            Map<String, Object> quote = restTemplate.getForObject(quoteUrl, Map.class);
            stockPrices.put(symbol, quote);
        }
        return stockPrices;
    }

    @GetMapping("/price")
    public Map<String, Object> getStockPrice(@RequestParam String symbol) {
        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiConfig.getApiKey();
        return restTemplate.getForObject(quoteUrl, Map.class);
    }
}
