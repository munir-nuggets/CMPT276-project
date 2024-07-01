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

 
@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/usersignup")
    public String userSignup(@RequestParam Map<String, String> entity) {
        String username = entity.get("username");
        
        if(usernameIsTaken(username)){
            return "usernameIsTaken";
        }

        String password = entity.get("password");
        String email = entity.get("email");
        User user = new User(username, email, password);
        userRepo.save(user);
        return "homepage";
    }
    
    public Boolean usernameIsTaken(String username){
        if(userRepo.findByUsername(username).isEmpty()){
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
        return "redirect:/home";
    }

    @RequestMapping("home")
    public String homepage() {
        return "/homepage";
    }
}
