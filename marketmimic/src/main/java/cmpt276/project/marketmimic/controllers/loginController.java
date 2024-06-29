package cmpt276.project.marketmimic.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class loginController {
    @Autowired
    private UserRepository userRepo;

    @PostMapping("/users/add")
    public String addUsers(@RequestParam Map<String, String> newUser, HttpServletResponse response) {
        System.out.println("ADD USER");
        String newUserName = newUser.get("username");
        String newEmail = newUser.get("email");
        String newPassword = newUser.get("password");
        userRepo.save(new User(newUserName, newEmail, newPassword));
        response.setStatus(201);
        return "users/addedUser";
    }
}
