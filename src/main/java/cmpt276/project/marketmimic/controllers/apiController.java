package cmpt276.project.marketmimic.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import cmpt276.project.marketmimic.model.*;
import cmpt276.project.marketmimic.service.CurrencyService;
import cmpt276.project.marketmimic.service.FinnhubService;
import cmpt276.project.marketmimic.service.PolygonService;
import jakarta.servlet.http.HttpSession;;

@Controller
@RequestMapping("/api/stocks")
public class apiController {

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private FinnhubService finnhubService;
    @Autowired
    private PolygonService polygonService;

    @GetMapping("/")
    public String getStockSymbols(Model model, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if(user == null) return "redirect:/login.html";
        model.addAttribute("user", user);
        MarketStatus marketStatus = finnhubService.geMarketStatus();
        List<StockSymbol> symbolsResponse = finnhubService.getAllSymbols();
        model.addAttribute("symbols", symbolsResponse);
        model.addAttribute("status", marketStatus);
        return "stocklist";
    }

    @GetMapping("/price")
    public String getStockPrice(@RequestParam String symbol, Model model, HttpSession session) {
        String stockDataResponseJson = polygonService.getHistoricalData(symbol);
        MarketStatus marketStatus = finnhubService.geMarketStatus();
        StockData stockData = finnhubService.getStockData(symbol);
        CompanyInfo companyInfo = finnhubService.getCompanyInfo(symbol);
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login.html";
        }
        double quantityOwned = user.getStockPurchases().containsKey(symbol) ? user.getStockPurchases().get(symbol).getQuantity() : 0;
        double pendingSellingQuantity = user.getStockPurchases().containsKey("**" + symbol) ? user.getStockPurchases().get("**" + symbol).getQuantity() : 0;
        model.addAttribute("usd", currencyService.getCurrencyBalance(user.getUsername()));
        model.addAttribute("symbol", symbol);
        model.addAttribute("stockData", stockData);
        model.addAttribute("stockDataResponse", stockDataResponseJson);
        model.addAttribute("user", user);
        model.addAttribute("company", companyInfo);
        model.addAttribute("status", marketStatus);
        model.addAttribute("quantityOwned", quantityOwned);
        model.addAttribute("pendingSellingQuantity", pendingSellingQuantity);
        finnhubService.injectApiKey(model);
        return "stocksymbol";
    }

    @PostMapping("/buy")
    public String buyStock(@RequestParam Map<String, String> stockData, HttpSession session, Model model) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) return "redirect:/login.html";
        boolean isBuy = Boolean.parseBoolean(stockData.get("action"));
        double quantity = Double.parseDouble(stockData.get("quantity"));
        String symbol = stockData.get("symbol");
        Double stockPrice = Double.parseDouble(stockData.get("price"));
        double totalPrice = stockPrice * quantity;
        if (isBuy) {
            currencyService.purchaseStock(symbol, quantity, totalPrice, user);
        } else {
            System.out.println("Selling stock"); 
            currencyService.sellStock(symbol, quantity, totalPrice, user);
        } 
        return "redirect:/currencyscreen";
    }
}