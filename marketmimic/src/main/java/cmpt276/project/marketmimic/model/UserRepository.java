package cmpt276.project.marketmimic.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    List<User> findByUsername(String username);
}
