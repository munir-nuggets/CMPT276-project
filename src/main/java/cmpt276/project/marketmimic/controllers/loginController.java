package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;
    

    @PostMapping("/usersignup")
    public String userSignup(@RequestParam Map<String, String> entity) {
        String username = entity.get("username");
        String email = entity.get("email");
        String password = entity.get("password");
        
        if(usernameIsTaken(username)){
            return "usernameIsTaken";
        } else if (emailIsTaken(email)) {
            return "emailIsTaken";
        }
        User user = new User(username, email, password);
        userRepo.save(user);
        return "homepage";
    }
    
    // borrowed from Bobby Chan's demo
    @GetMapping("/userlogin")
    public String getLogin(Model model, HttpServletRequest request, HttpSession session){
        User user = (User) session.getAttribute("session_user");
        if (user == null){
            return "redirect:/login.html";
        }
        else {
            model.addAttribute("user", user);
            String endpoint = user.isIsadmin() ? "redirect:/admin/dashboard" : "redirect:/api/stocks/";
            return endpoint;
        }
    }

    @PostMapping("/userlogin")
    public String userLogin(@RequestParam Map<String, String> loginData, Model model, HttpSession session, HttpServletRequest request){
        String usernameOrEmail = loginData.get("usernameOrEmail");
        String password = loginData.get("password");
        Optional<User> userOpt = userRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)){
            User user = userOpt.get();
            request.getSession().setAttribute("session_user", user);
            model.addAttribute("user", user);
            String endpoint = user.isIsadmin() ? "redirect:/admin/dashboard" : "redirect:/api/stocks/";
            return endpoint;
        }
        else {
            return "invalidLogin";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

    @RequestMapping("/")
    public String homepage() {
        createAdminIfDoesntExist();
        return "homepage";
    }

    public Boolean usernameIsTaken(String username){
        if (userRepo.findByUsername(username).isEmpty()){
            return false;
        }
        return true;
    }

    public Boolean emailIsTaken(String email) {
        if (userRepo.findByEmail(email).isEmpty()) {
            return false;
        }
        return true;
    }

    public void createAdminIfDoesntExist() {
        List<User> users = userRepo.findAllByIsadmin(true);
        if (users.isEmpty()){ 
            User user = new User("admin", "admin", "admin", true, Double.valueOf(0));
            userRepo.save(user);
        }
    }
}
