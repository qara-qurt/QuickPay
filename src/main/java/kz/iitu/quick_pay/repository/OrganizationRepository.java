package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
    boolean existsByName(String name);
    boolean existsByBin(String bin);
}
