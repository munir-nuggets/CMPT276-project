package cmpt276.project.marketmimic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

import java.util.List;
import cmpt276.project.marketmimic.model.*;

@Controller
@RequestMapping("/admin")
public class adminController {

    public static boolean isAdmin = false;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        if (!loginController.isLoggedIn || !isAdmin) {
            return "redirect:/";
        }
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "admindashboard";
    }
}
