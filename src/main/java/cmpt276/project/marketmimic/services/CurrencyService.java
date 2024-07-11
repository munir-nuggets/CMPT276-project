package cmpt276.project.marketmimic.services;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CurrencyService {
    public void addCurrency(HttpSession session, int amount) {
        Integer currentBalance = (Integer) session.getAttribute("currencyBalance");
        if (currentBalance == null) {
            currentBalance = 0;
        }
        session.setAttribute("currencyBalance", currentBalance + amount);
    }

    public int getCurrencyBalance(HttpSession session) {
        Integer currentBalance = (Integer) session.getAttribute("currencyBalance");
        if(currentBalance != null){
            return currentBalance;
        }
        return 0;
    }
}
