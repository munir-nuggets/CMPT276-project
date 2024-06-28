package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;



@Controller
public class loginController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/usersignup")
    public String userSignup(@RequestParam Map<String, String> entity) {
        String username = entity.get("username");
        String password = entity.get("password");
        String email = entity.get("email");
        User user = new User(username, email, password);
        userRepo.save(user);
        return "homepage";
    }
    

    @GetMapping("/userlogin")
    public String userLogin(@RequestBody String entity) {
        
        return "homepage";
    }
    @RequestMapping("home")
    public String homepage() {
        return "/homepage";
    }
    
    
}
