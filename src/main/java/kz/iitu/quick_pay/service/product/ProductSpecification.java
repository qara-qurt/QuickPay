package kz.iitu.quick_pay.service.product;

import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

public class ProductSpecification {
    public static Specification<ProductEntity> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likeSearch = "%" + search.toLowerCase() + "%";

            Join<ProductEntity, OrganizationEntity> organizationJoin = root.join("organization");

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(organizationJoin.get("name")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("sizes")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("colors")), likeSearch)
            );
        };
    }
}
