package cmpt276.project.marketmimic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

import java.util.List;
import cmpt276.project.marketmimic.model.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if(user == null) return "redirect:/login.html";
        model.addAttribute("user", user);
        if (!user.isIsadmin()) return "redirect:/api/stocks/";
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "admindashboard";
    }
}
