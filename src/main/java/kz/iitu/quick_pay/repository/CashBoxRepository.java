package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.CashBoxEntity;
import kz.iitu.quick_pay.enitity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashBoxRepository extends JpaRepository<CashBoxEntity, Long> {
    List<CashBoxEntity> findByOrganizationId(Long organizationId);
    Optional<CashBoxEntity> findByCashBoxId(String cashBoxId);
}
