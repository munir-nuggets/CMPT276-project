package cmpt276.project.marketmimic.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cmpt276.project.marketmimic.model.StockDataResponse;

@Service
public class PolygonService {

    private final RestTemplate restTemplate;

    public PolygonService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public StockDataResponse getStockData(String ticker) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusYears(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateString = startDate.format(formatter);
        String endDateString = endDate.format(formatter);
        String url = "https://api.polygon.io/v2/aggs/ticker/" + ticker + "/range/1/day/" + startDateString + "/" + endDateString + "?adjusted=true&sort=asc&apiKey=oKU_VAKwXYjlkUoMeB2CodWt1hQmCYI3";
        return restTemplate.getForObject(url, StockDataResponse.class);
    }
}
