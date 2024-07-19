package cmpt276.project.marketmimic.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cmpt276.project.marketmimic.model.StockData;
import com.fasterxml.jackson.databind.ObjectMapper;

import cmpt276.project.marketmimic.config.ApiConfig;

@Service
public class FinnhubService {

    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;
    private final ObjectMapper objectMapper;

    public FinnhubService(RestTemplateBuilder restTemplateBuilder, ApiConfig apiConfig, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiConfig = apiConfig;
        this.objectMapper = objectMapper;
    }

    public double getSinglePrice(String symbol) {
        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiConfig.getApiKey();
        StockData stockData = restTemplate.getForObject(quoteUrl, StockData.class);
        return stockData.getCurrentPrice();
    }
}
