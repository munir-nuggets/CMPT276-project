package cmpt276.project.marketmimic.controllers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;
import cmpt276.project.marketmimic.service.CurrencyService;
import jakarta.servlet.http.HttpSession;

@Controller
public class mainPageController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/leaderboard")
    public String leaderboard(Model model, HttpSession session) {
        User u = (User) session.getAttribute("session_user");
        if (u==null) return "redirect:/login.html";

        List<User> users = userRepository.findAllByIsadmin(false);
        Map<User, Double> accountValues = new HashMap<User,Double>();

        for (User user : users) {
            Double accountValue = currencyService.getAccountValue(user);
            accountValues.put(user, accountValue);
        }
        users.sort(Comparator.comparing(accountValues::get).reversed());
        users = users.subList(0, Math.min(10, users.size()));
        model.addAttribute("users", users);
        model.addAttribute("accountValues", accountValues);
        return "leaderboard";
    }

    @GetMapping("/learn")
    public String learn() {
        return "learn";
    }
}
