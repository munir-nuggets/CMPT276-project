package cmpt276.project.marketmimic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockSymbol {
    @JsonProperty("ticker")
    private String symbol;
    @JsonProperty("name")
    private String description;

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
