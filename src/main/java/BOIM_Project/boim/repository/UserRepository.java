package BOIM_Project.boim.repository;

import BOIM_Project.boim.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAccount(String userAccount);
    boolean existsByUserAccount(String userAccount);
}
