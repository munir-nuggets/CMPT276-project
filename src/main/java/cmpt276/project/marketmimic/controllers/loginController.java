package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.PasswordResetToken;
import cmpt276.project.marketmimic.model.UserRepository;
import cmpt276.project.marketmimic.service.EmailService;
import cmpt276.project.marketmimic.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Optional;
import java.util.List;



@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${app.base-url}")
    private String baseUrl;

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
        User user = new User(username, email, passwordEncoder.encode(password));
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

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())){
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

    @PostMapping("/userlogout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

    @RequestMapping("/")
    public String homepage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        createAdminIfDoesntExist();
        if(user != null){
            return user.isIsadmin() ? "redirect:/admin/dashboard" : "redirect:/api/stocks/";
        }
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
            User user = new User("admin", "admin", passwordEncoder.encode("admin"), true, Double.valueOf(0));
            userRepo.save(user);
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(){
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(HttpServletRequest request, @RequestParam("email") String email, Model model) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            PasswordResetToken token = tokenService.createToken(user);

            String resetLink = baseUrl + "/reset-password?token=" + token.getToken();
            emailService.sendEmail(user.getEmail(), "Password Reset Request", "Click the link to reset your password: " + resetLink);

            model.addAttribute("message", "We have sent a reset password link to your email.");
        } else {
            model.addAttribute("error", "No account found with the provided email.");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        // Pass the token to the model so it can be included in the form
        model.addAttribute("token", token);
        return "reset-password";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
            Optional<PasswordResetToken> resetTokenOptional = tokenService.validatePasswordResetToken(token);
    
            if (!resetTokenOptional.isPresent()) {
                return "forgot-password";
            }
    
            PasswordResetToken resetToken = resetTokenOptional.get();
            User user = resetToken.getUser();
    
            // Update the user's password here
            user.setPassword(passwordEncoder.encode(newPassword));  // Ensure to hash the password before saving
            userRepo.save(user);
    
            return "redirect:/";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam String username) {
        Optional<User> user = userRepo.findByUsername(username);
            tokenService.deleteTokensByUser(user.get());
            userRepo.delete(user.get());
            return "redirect:/admin/dashboard";
    }
}
