package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Page<TransactionEntity> findByOrganization_Id(Long organizationId, Pageable pageable);
    Page<TransactionEntity> findByOrganizationIdAndCashBox_CashBoxId(Long organizationId, String cashBoxId, Pageable pageable);
}