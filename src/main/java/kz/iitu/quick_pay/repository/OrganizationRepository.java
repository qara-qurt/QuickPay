package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.InventoryEntity;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {
    boolean existsByName(String name);
    boolean existsByBin(String bin);
    Page<OrganizationEntity> findAll(Specification<OrganizationEntity> spec, Pageable pageable);
}
