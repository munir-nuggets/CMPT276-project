package cmpt276.project.marketmimic.model;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    List<User> findByUserNameAndPassword(String userName, String password);
}