package cmpt276.project.marketmimic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolygonResponse {

    @JsonProperty("results")
    private List<StockSymbol> tickers;

    public List<StockSymbol> getTickers() {
        return tickers;
    }

    public void setTickers(List<StockSymbol> tickers) {
        this.tickers = tickers;
    }
}