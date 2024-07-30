package cmpt276.project.marketmimic.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import cmpt276.project.marketmimic.model.CompanyInfo;
import cmpt276.project.marketmimic.model.MarketStatus;
import cmpt276.project.marketmimic.model.StockData;
import cmpt276.project.marketmimic.model.StockSymbol;

@Service
public class FinnhubService {

    @Value("${finnhub.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public FinnhubService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public double getSinglePrice(String symbol) {
        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
        StockData stockData = restTemplate.getForObject(quoteUrl, StockData.class);
        return stockData.getCurrentPrice();
    }

    public MarketStatus geMarketStatus() {
        String marketStatusUrl = "https://finnhub.io/api/v1/stock/market-status?exchange=US&token=" + apiKey;
        return restTemplate.getForObject(marketStatusUrl, MarketStatus.class);
    }

    public List<StockSymbol> getAllSymbols() {
        String symbolsUrl = "https://finnhub.io/api/v1/stock/symbol?exchange=US&token=" + apiKey;
        ResponseEntity<List<StockSymbol>> response = restTemplate.exchange(
                symbolsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockSymbol>>() {}
        );
        List<StockSymbol> symbolsResponse = response.getBody();
        Collections.sort(symbolsResponse, Comparator.comparing(StockSymbol::getDescription));
        return symbolsResponse;
    }

    public StockData getStockData(String symbol) {
        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
        return restTemplate.getForObject(quoteUrl, StockData.class);
    }

    public CompanyInfo getCompanyInfo(String symbol) {
        String companyInfoUrl = "https://finnhub.io/api/v1/stock/profile2?symbol=" + symbol + "&token=" + apiKey;
        return restTemplate.getForObject(companyInfoUrl, CompanyInfo.class);
    }

    public void injectApiKey(Model model) {
        model.addAttribute("apiKey", apiKey);
    }
}
