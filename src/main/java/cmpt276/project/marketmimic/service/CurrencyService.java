package cmpt276.project.marketmimic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmpt276.project.marketmimic.model.StockPurchase;
import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;

@Service
public class CurrencyService {
    @Autowired
    private UserRepository userRepository;

    public void addCurrency(User user, double amount) {
        user.setUsd(user.getUsd() + amount);
        userRepository.save(user);
    }

    public void removeCurrency(User user, double amount) {
        user.setUsd(user.getUsd() - amount);
        userRepository.save(user);
    }

    public void purchaseStock(String symbol, double quantity, double price, User user) {
        if (user.getUsd() < price) 
            return;
        
        StockPurchase stockPurchase = new StockPurchase(symbol, quantity, price);
        user.addStockPurchase(stockPurchase);
        user.setUsd(user.getUsd() - price);

        userRepository.save(user);
    }

    public void sellStock(String symbol, double sellingQuantity, double price, User user) {
        StockPurchase purchasedStock = user.getStockPurchases().get(symbol);
        if(purchasedStock == null || purchasedStock.getQuantity() < sellingQuantity) {
            System.out.println("User does not own enough stock to sell");
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
}