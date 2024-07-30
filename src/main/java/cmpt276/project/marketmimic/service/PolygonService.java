package cmpt276.project.marketmimic.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cmpt276.project.marketmimic.model.StockDataResponse;

@Service
public class PolygonService {

    @Value("${polygon.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PolygonService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String getHistoricalData(String ticker) {
        String stockDataUrl = "https://api.polygon.io/v2/aggs/ticker/" + ticker + "/range/1/day/" + getOneYearAgo() + "/" + getCurrentDate() + "?apiKey=" + apiKey;
        StockDataResponse stockDataResponse = restTemplate.getForObject(stockDataUrl, StockDataResponse.class);

        String stockDataResponseJson = "";
        try {
            stockDataResponseJson = objectMapper.writeValueAsString(stockDataResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return stockDataResponseJson;
    }

    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    private String getOneYearAgo() {
        return java.time.LocalDate.now().minusYears(1).toString();
    }
}
