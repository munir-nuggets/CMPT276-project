package cmpt276.project.marketmimic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByUsername(String username);
    Optional<Role> findByRoleName(String roleName);
}
