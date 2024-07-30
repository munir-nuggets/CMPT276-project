package cmpt276.project.marketmimic.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import cmpt276.project.marketmimic.model.PendingTradeStockData;
import cmpt276.project.marketmimic.model.PendingTradeStockData.StockData;

@Service
public class FmpService {

    @Value("${fmp.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public FmpService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public double nextOpeningPrice(String symbol, LocalDate date) {
        String priceUrl = "https://financialmodelingprep.com/api/v3/historical-price-full/" + symbol + "?from=" + date + "&apikey=" + apiKey;
        PendingTradeStockData pendingTradeStockData = restTemplate.getForObject(priceUrl, PendingTradeStockData.class);

        if (pendingTradeStockData.getHistorical() == null) {
            return -9999.99;
        }

        List<StockData> list = pendingTradeStockData.getHistorical();

        Optional<StockData> earliestObject = list.stream()
            .min((o1, o2) -> o1.getDate().compareTo(o2.getDate()));

        double earliestOpen = earliestObject
            .map(StockData::getOpen)
            .orElseThrow(() -> new RuntimeException("List is empty"));

        return earliestOpen;
    }
}
