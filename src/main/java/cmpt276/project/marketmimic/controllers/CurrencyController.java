package cmpt276.project.marketmimic.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.services.CurrencyService;
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
            currencyService.addCurrency(user.getUsername(), Double.parseDouble(currencyData.get("amount")));
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
        if (user != null) {
            model.addAttribute("usd", currencyService.getCurrencyBalance(user.getUsername()));
            // Add other user attributes as needed
            return "currencyscreen";
        }
        return "redirect:/login.html"; // Redirect to login if user is not logged in
    }

    @PostMapping("/currencyscreen")
    public String currencyScreen(){
        return "currencyscreen";
    }
    
    
}