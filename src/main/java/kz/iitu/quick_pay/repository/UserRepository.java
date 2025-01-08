package kz.iitu.quick_pay.repository;

import jakarta.validation.constraints.NotNull;
import kz.iitu.quick_pay.enitity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
