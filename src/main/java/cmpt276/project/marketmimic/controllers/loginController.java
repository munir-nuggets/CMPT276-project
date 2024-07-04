package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;
import java.util.Optional;
import java.util.List;

 
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
            User user = new User("admin", "admin", "admin", true);
            userRepo.save(user);
        }
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
            User user = userOpt.get();
            if (user.isIsadmin()) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/api/stocks/";
            }
        }

        return "invalidLogin";
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
}
