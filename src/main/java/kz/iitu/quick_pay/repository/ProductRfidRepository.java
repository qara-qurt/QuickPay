package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.ProductRFIDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRfidRepository extends JpaRepository<ProductRFIDEntity, Long> {
    boolean existsByRfidToken(String rfidToken);
    List<ProductRFIDEntity> findByProductId(Long productId);
    ProductRFIDEntity findByRfidToken(String rfidToken);
}
