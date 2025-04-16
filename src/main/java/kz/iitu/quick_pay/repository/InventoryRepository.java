package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.InventoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    boolean existsByProductId(Long productId);
    List<InventoryEntity> findByProductId(Long productId, Pageable pageable);
}
