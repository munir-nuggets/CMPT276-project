package cmpt276.project.marketmimic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockData {
    @JsonProperty("c")
    private double currentPrice;

    @JsonProperty("h")
    private double highPrice;

    @JsonProperty("l")
    private double lowPrice;

    @JsonProperty("o")
    private double openingPrice;

    @JsonProperty("pc")
    private double previousClosePrice;

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(double openingPrice) {
        this.openingPrice = openingPrice;
    }

    public double getPreviousClosePrice() {
        return previousClosePrice;
    }

    public void setPreviousClosePrice(double previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }
    
}
