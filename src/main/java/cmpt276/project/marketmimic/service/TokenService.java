package cmpt276.project.marketmimic.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import cmpt276.project.marketmimic.model.PasswordResetToken;
import cmpt276.project.marketmimic.model.PasswordResetTokenRepository;
import cmpt276.project.marketmimic.model.User;
import cmpt276.project.marketmimic.model.UserRepository;
import jakarta.transaction.Transactional;


@Service
public class TokenService {

    // Method to generate a new token with an expiry time of one hour
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PasswordResetToken createToken(User user) {
        // Check if a token already exists for the user
        PasswordResetToken existingToken = tokenRepository.findByUser(user);
        if (existingToken != null && !isTokenExpired(existingToken)) {
            return existingToken; // Return existing token if not expired
        }
        
        // Generate new token and expiry date
        String tokenValue = generateToken(); // Implement your token generation logic
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // Example: Token expires in 24 hours

        // Create new PasswordResetToken entity
        PasswordResetToken token = new PasswordResetToken(tokenValue, user, expiryDate);
        return tokenRepository.save(token);
    }

    // Implement your token generation logic here
    
    private String generateToken() {
        // Implement your token generation logic (e.g., UUID.randomUUID().toString())
        return UUID.randomUUID().toString();
    }

    
    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    
    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOptional = tokenRepository.findByToken(token);
    
        if (!resetTokenOptional.isPresent()) {
            return Optional.empty(); // or throw an exception
        }
    
        PasswordResetToken resetToken = resetTokenOptional.get();
    
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            return Optional.empty(); // or throw an exception
        }
    
        return resetTokenOptional;
    }
    

    // Get user by token
    
    public Optional<User> getUserByToken(String token) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        return optionalToken.map(PasswordResetToken::getUser);
    }

    @Transactional
    public void deleteTokensByUser(User user) {
        tokenRepository.deleteByUser(user);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);
        if (optionalToken.isPresent()) {
            PasswordResetToken passwordResetToken = optionalToken.get();
            if (passwordResetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
                User user = passwordResetToken.getUser();
                // Set the new password (ensure it is properly encoded before setting)
                user.setPassword(newPassword);
                userRepository.save(user); // Save the updated user
                tokenRepository.delete(passwordResetToken); // Invalidate the token
                return true;
            }
        }
        return false;
    }
}

