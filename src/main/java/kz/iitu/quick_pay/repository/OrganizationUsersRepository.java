package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.OrganizationUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationUsersRepository extends JpaRepository<OrganizationUsersEntity, Long> {
    List<OrganizationUsersEntity> findByOrganizationId(Long organizationId);
    Optional<OrganizationUsersEntity> findByUserId(Long userId);
}

