package cmpt276.project.marketmimic.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    
    PasswordResetToken findByUser(User user);
    Optional<PasswordResetToken> findByToken(String token);
    
}

