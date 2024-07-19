package cmpt276.project.marketmimic.model;

import java.util.Map;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;
    private String username;
    private String email;
    private String password;
    private Boolean isadmin;
    private Double usd;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "stock_purchases", joinColumns = @JoinColumn(name = "uid"))
    @MapKeyColumn(name = "stock_name")
    private Map<String, StockPurchase> stockPurchases = new java.util.HashMap<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<PasswordResetToken> passwordResetTokens;

    public User() {
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isadmin = false;
        this.usd = Double.valueOf(10000);
    }
    public User(String username, String email, String password, boolean isAdmin, Double usd) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isadmin = isAdmin;
        this.usd = usd;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean admin) {
        isadmin = admin;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public Double getUsd() {
        return usd;
    }
    public void setUsd(Double usd) {
        this.usd = Math.round(usd * 100.0) / 100.0;
    }
    public Map<String, StockPurchase> getStockPurchases() {
        return stockPurchases;
    }
    public void setStockPurchases(Map<String, StockPurchase> stockPurchases) {
        this.stockPurchases = stockPurchases;
    }
    public void addStockPurchase(StockPurchase stockPurchase) {
        if(stockPurchases.containsKey(stockPurchase.getSymbol())) {
            StockPurchase existingStockPurchase = stockPurchases.get(stockPurchase.getSymbol());
            existingStockPurchase.setQuantity(existingStockPurchase.getQuantity() + stockPurchase.getQuantity());
            existingStockPurchase.setPrice(existingStockPurchase.getPrice() + stockPurchase.getPrice());
        } else {
            stockPurchases.put(stockPurchase.getSymbol(), stockPurchase);
        }
    }
    public void removeStockPurchase(String symbol) {
        stockPurchases.remove(symbol);
    }
}
