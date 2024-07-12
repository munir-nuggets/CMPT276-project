package cmpt276.project.marketmimic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class CurrencyService {
    @Autowired
    private UserRepository userRepository;

    public void addCurrency(String username, int amount) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.get() != null) {
            userOpt.get().setUsd(userOpt.get().getUsd() + amount);
            userRepository.save(userOpt.get());
        }
    }

    public int getCurrencyBalance(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.get() != null) {
            return userOpt.get().getUsd();
        }
        return 0;
    }
}