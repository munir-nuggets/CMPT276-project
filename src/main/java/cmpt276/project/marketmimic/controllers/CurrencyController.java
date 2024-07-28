package cmpt276.project.marketmimic.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.StockPurchase;
import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.service.CurrencyService;
import cmpt276.project.marketmimic.service.FinnhubService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private FinnhubService finnhubService;

    @PostMapping("/addcurrency")
    public String addCurrency(@RequestParam Map<String, String> currencyData, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if (user == null){
            return "redirect:/login.html";
        }
        else {
            currencyService.addCurrency(user, Double.parseDouble(currencyData.get("amount")));
            return "redirect:/currencyscreen";
        }
    }

    
    @GetMapping("/balance")
    public ResponseEntity<Double> getCurrencyBalance(@RequestParam String username) {
        Double balance = currencyService.getCurrencyBalance(username);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/currencyscreen")
    public String currencyscreen(Model model, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if(user==null) return "redirect:/login.html";

        model.addAttribute("usd", currencyService.getCurrencyBalance(user.getUsername()));
        model.addAttribute("purchases", user.getStockPurchases().values());

        Map<String, Double> stockReturns = new java.util.HashMap<>();
        Map<String, Double> currentValues = new java.util.HashMap<>();
        Double totalStockValue = 0.0;
        for(StockPurchase stock : user.getStockPurchases().values()){
            Double currentStockWorth = finnhubService.getSinglePrice(stock.getSymbol()) * stock.getQuantity();
            totalStockValue += currentStockWorth;
            Double stockReturn = (currentStockWorth / stock.getPrice());
            stockReturn = Math.round(stockReturn * 1000.0) / 10.0;
            stockReturns.put(stock.getSymbol(), stockReturn);
            currentValues.put(stock.getSymbol(), currentStockWorth);
        }
        model.addAttribute("stockReturns", stockReturns);
        model.addAttribute("currentValues", currentValues);
        double accountValue = totalStockValue + user.getUsd();
        model.addAttribute("accountValue", accountValue);
        double initialAccountValue = 10000.0;
        double accountReturn = Math.round(accountValue / initialAccountValue * 1000.0) / 10.0;
        model.addAttribute("accountReturn", accountReturn);
        
        return "currencyscreen";
    }

    @PostMapping("/currencyscreen")
    public String currencyScreen(){
        return "currencyscreen";
    }
    
    @GetMapping("/buyStock")
    public String buyStock(){
        return "currencyScreen";
    }
    
}