package cmpt276.project.marketmimic.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.Role;
import cmpt276.project.marketmimic.model.RoleRepository;
import cmpt276.project.marketmimic.model.UserRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Map;
import java.util.Optional;
import java.util.List;

 
@Controller
public class loginController {

    public static boolean isLoggedIn = false;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
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
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin");
        user.setPassword(passwordEncoder.encode("admin"));
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
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())){
            Optional<Role> roleOpt = roleRepo.findByUsername(userOpt.get().getUsername());
            if (roleOpt.isPresent() && roleOpt.get().getRoleName().equals("admin")) {
                isLoggedIn = true;
                adminController.isAdmin = true;
                return "redirect:/admin/dashboard";
            }
            isLoggedIn = true;
            return "redirect:/api/stocks/";
        }
        else {
            return "invalidlogin";
        }
    }

    @PostMapping("/logout")
    public String logout(){
        isLoggedIn = false;
        adminController.isAdmin = false;
        return "redirect:/";
    }

    @RequestMapping("/")
    public String homepage() {
        createAdminIfDoesntExist();
        return "homepage";
    }
}
