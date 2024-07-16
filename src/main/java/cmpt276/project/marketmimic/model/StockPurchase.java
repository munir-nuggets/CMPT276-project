package cmpt276.project.marketmimic.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StockPurchase implements Serializable {
    private String symbol;
    private double quantity;
    private double price;

    public StockPurchase(String symbol, double quantity, double price) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public StockPurchase() {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
}
