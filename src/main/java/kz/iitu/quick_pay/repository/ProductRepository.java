package kz.iitu.quick_pay.repository;

import kz.iitu.quick_pay.enitity.ProductEntity;
import kz.iitu.quick_pay.enitity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{
    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable pageable);
}
