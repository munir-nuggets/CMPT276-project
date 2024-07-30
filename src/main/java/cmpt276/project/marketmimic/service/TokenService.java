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
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PasswordResetToken createToken(User user) {
        PasswordResetToken existingToken = tokenRepository.findByUser(user);
        if (existingToken != null && !isTokenExpired(existingToken)) {
            return existingToken;
        }
        
        String tokenValue = generateToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        PasswordResetToken token = new PasswordResetToken(tokenValue, user, expiryDate);
        return tokenRepository.save(token);
    }
    
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    
    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    
    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOptional = tokenRepository.findByToken(token);
    
        if (!resetTokenOptional.isPresent()) {
            return Optional.empty();
        }
    
        PasswordResetToken resetToken = resetTokenOptional.get();
    
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken);
            return Optional.empty();
        }
    
        return resetTokenOptional;
    }
    
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
                user.setPassword(newPassword);
                userRepository.save(user);
                tokenRepository.delete(passwordResetToken);
                return true;
            }
        }
        return false;
    }
}

