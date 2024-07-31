package cmpt276.project.marketmimic.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmpt276.project.marketmimic.model.StockPurchase;
import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;

@Service
public class CurrencyService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinnhubService finnhubService;

    @Autowired
    private FmpService fmpService;

    public void addCurrency(User user, double amount) {
        user.setUsd(user.getUsd() + amount);
        userRepository.save(user);
    }

    public void removeCurrency(User user, double amount) {
        user.setUsd(user.getUsd() - amount);
        userRepository.save(user);
    }

    public void purchaseStock(String symbol, double quantity, double price, User user) {
        if (user.getUsd() < price) return;
        if (price <= -9999.99) {
            addPendingTrade(symbol, quantity, price, user, true);
            return;
        }
        StockPurchase stockPurchase = new StockPurchase(symbol, quantity, price, false, null, true);
        user.addStockPurchase(stockPurchase);
        user.setUsd(user.getUsd() - price);

        userRepository.save(user);
    }

    public Double twoDecimals(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public void sellStock(String symbol, double sellingQuantity, double price, User user) {
        StockPurchase purchasedStock = user.getStockPurchases().get(symbol);
        if(purchasedStock == null || purchasedStock.getQuantity() < sellingQuantity) {
            System.out.println("User does not own enough stock to sell");
            return;
        }
        if (price <= -9999.99) {
            addPendingTrade(symbol, sellingQuantity, price, user, false);
            return;
        }

        Double remainingQuantityRatio = (purchasedStock.getQuantity() - sellingQuantity) / purchasedStock.getQuantity();
        Double updatedPrice = purchasedStock.getPrice() * remainingQuantityRatio;

        user.setUsd(user.getUsd() + price);
        purchasedStock.setQuantity(purchasedStock.getQuantity() - sellingQuantity);
        purchasedStock.setPrice(updatedPrice);

        if (purchasedStock.getQuantity() == 0) 
            user.removeStockPurchase(symbol);
            
        userRepository.save(user);
    }

    public Double getCurrencyBalance(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.get() != null) {
            return userOpt.get().getUsd();
        }
        return Double.valueOf(0);
    }

    public Double getAccountValue(User user) {
        Double totalStockValue = 0.0;
        for(StockPurchase stock : user.getStockPurchases().values()){
            if(!stock.isPending()) {
            Double currentStockWorth = finnhubService.getSinglePrice(stock.getSymbol()) * stock.getQuantity();
            totalStockValue += currentStockWorth;
            }
        }
        return totalStockValue + user.getUsd();
    }

    public void checkPendingTrades(User user) {
        LocalDate currentDate = LocalDate.now();
        //double test = fmpService.nextOpeningPrice("AAPL", LocalDate.of(2024, 07, 25));
        List<StockPurchase> pendingTrades = user.getStockPurchases().values().stream()
                .filter(StockPurchase::isPending)
                .filter(stock -> stock.getPendingDate().isBefore(currentDate))
                .collect(Collectors.toList());

        for (StockPurchase stock : pendingTrades) {
            double openPrice = fmpService.nextOpeningPrice(stock.getSymbol(), stock.getPendingDate());
            if (openPrice != -9999.99) {
                if (stock.isBuy()) {
                    purchaseStock(stock.getSymbol(), stock.getQuantity(), openPrice*stock.getQuantity(), user);
                    if (user.getStockPurchases().containsKey("*" + stock.getSymbol())) {
                        user.getStockPurchases().remove("*" + stock.getSymbol());
                    }
                } else {
                    sellStock(stock.getSymbol(), stock.getQuantity(), openPrice*stock.getQuantity(), user);
                    if (user.getStockPurchases().containsKey("**" + stock.getSymbol())) {
                        user.getStockPurchases().remove("**" + stock.getSymbol());
                    }
                }
            }
        }
    }

    private void addPendingTrade(String symbol, double quantity, double price, User user, boolean isBuy) {
        StockPurchase pendingTrade = new StockPurchase(symbol, quantity, price, true, LocalDate.now(), isBuy);
        user.addStockPurchase(pendingTrade);
        userRepository.save(user);
    }
}