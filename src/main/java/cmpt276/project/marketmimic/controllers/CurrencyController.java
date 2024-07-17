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
import jakarta.servlet.http.HttpSession;

@Controller
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

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
        if(user == null) return "redirect:/login.html";
        
        model.addAttribute("usd", currencyService.getCurrencyBalance(user.getUsername()));
        model.addAttribute("purchases", user.getStockPurchases().values());
        for(StockPurchase purchase : user.getStockPurchases().values()) {
            model.addAttribute(purchase.getSymbol(), purchase.getQuantity());
        }
        // Add other user attributes as needed
        return "currencyscreen";
    }

    @PostMapping("/currencyscreen")
    public String currencyScreen(){
        return "currencyscreen";
    }
    
    @GetMapping("/buyStock")
    public String buyStock(){
        return "buyStock";
    }

    @PostMapping("/sell")
    public String sellStock(@RequestParam Map<String, String> stockData, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if (user == null){
            return "redirect:/login.html";
        }
        else {
            Double quantity = Double.parseDouble(stockData.get("quantity"));
            Double price = Double.parseDouble(stockData.get("price"));
            currencyService.sellStock(stockData.get("symbol"), quantity, price, user);
            return "redirect:/currencyscreen";
        }
    }

    @GetMapping("/sellStock")
    public String sellStock(){
        return "sellStock";
    }
    
}