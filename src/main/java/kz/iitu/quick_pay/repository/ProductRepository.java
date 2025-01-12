package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    List<ProductEntity> findByOrganizationId(Long organizationId);
    Optional<ProductEntity> findByRfidToken(String rfid_token);
}
