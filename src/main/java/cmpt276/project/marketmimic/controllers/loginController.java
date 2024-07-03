package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;

// For getmapping
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

 
@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;
    // for testing

    @GetMapping("/allusers")
    public String getAllUsers(Model model) {
        System.out.println("Getting all users");
        
        List<User> users = userRepo.findAll();
        // end of database call

        // rects represents rectangles
        model.addAttribute("users", users);
        return "showAllUsers";
    }

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

    @PostMapping("/userlogin")
    public String userLogin(@RequestParam Map<String, String> loginData){
        String usernameOrEmail = loginData.get("usernameOrEmail");
        String password = loginData.get("password");
        Optional<User> userOpt = userRepo.findByUsername(usernameOrEmail);
        if(!userOpt.isPresent()){
            userOpt = userRepo.findByEmail(usernameOrEmail);
        }
        if(userOpt.isPresent() && userOpt.get().getPassword().equals(password)){
           return "redirect:/api/stocks/";
        }
        else{
           return "invalidlogin";
        }
    }

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/";
    }
    @PostMapping("/resetpassword")
    public String resetPassword(@RequestParam Map<String, String> entity){
        String email = entity.get("email");
        String newPassword = entity.get("newPassword");
        Optional<User> userOpt = userRepo.findByEmail(email);
        if(userOpt.isPresent()){
            userOpt.get().setPassword(newPassword);
            userRepo.save(userOpt.get());
            return "homepage";
         }
        else{
            return "noUserHasThatEmail";
        }
    }
    @RequestMapping("/")
    public String homepage() {
        return "homepage";
    }
}
