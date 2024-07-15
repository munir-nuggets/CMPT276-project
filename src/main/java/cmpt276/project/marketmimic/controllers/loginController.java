package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.PasswordResetToken;
import cmpt276.project.marketmimic.model.Role;
import cmpt276.project.marketmimic.model.RoleRepository;
import cmpt276.project.marketmimic.model.UserRepository;
import cmpt276.project.marketmimic.service.EmailService;
import cmpt276.project.marketmimic.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;
import java.util.Optional;
import java.util.List;

 
@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TokenService tokenService;

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
        Role role = new Role(username, "user");
        roleRepo.save(role);
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
        List<Role> roles = roleRepo.findAll();
        for (Role role : roles) {
            if (role.getRoleName().equals("admin")) {
                return;
            }
        }
        User user = new User("admin", "admin", "admin");
        userRepo.save(user);
        Role role = new Role("admin", "admin");
        roleRepo.save(role);
    }

    @PostMapping("/userlogin")
    public String userLogin(@RequestParam Map<String, String> loginData){
        String usernameOrEmail = loginData.get("usernameOrEmail");
        String password = loginData.get("password");
        Optional<User> userOpt = userRepo.findByUsername(usernameOrEmail);
        if (!userOpt.isPresent()){
            userOpt = userRepo.findByEmail(usernameOrEmail);
        }
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)){
            Optional<Role> roleOpt = roleRepo.findByUsername(userOpt.get().getUsername());
            if (roleOpt.isPresent() && roleOpt.get().getRoleName().equals("admin")) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/api/stocks/";
        }
        else {
            return "invalidlogin";
        }
    }

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

    @RequestMapping("/")
    public String homepage() {
        createAdminIfDoesntExist();
        return "homepage";
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

            String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/reset-password?token=" + token.getToken();
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
            user.setPassword(newPassword);  // Ensure to hash the password before saving
            userRepo.save(user);
    
            return "login";
    }
}
