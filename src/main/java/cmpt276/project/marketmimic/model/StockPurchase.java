package cmpt276.project.marketmimic.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public class StockPurchase implements Serializable {
    private String symbol;
    private double quantity;
    private double price;
    private boolean isPending;
    private LocalDate pendingDate;
    private boolean isBuy;

    public StockPurchase(String symbol, double quantity, double price, boolean isPending, LocalDate pendingDate, boolean isBuy) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.isPending = isPending;
        this.pendingDate = pendingDate;
        this.isBuy = isBuy;
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
    
    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }

    public LocalDate getPendingDate() {
        return pendingDate;
    }

    public void setPendingDate(LocalDate pendingDate) {
        this.pendingDate = pendingDate;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean isBuy) {
        this.isBuy = isBuy;
    }
}
