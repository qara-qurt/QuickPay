package kz.iitu.quick_pay.service.organization;

import kz.iitu.quick_pay.enitity.OrganizationEntity;
import org.springframework.data.jpa.domain.Specification;

public class OrganizationSpecification {

    public static Specification<OrganizationEntity> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<OrganizationEntity> hasBin(String bin) {
        return (root, query, criteriaBuilder) ->
                bin == null ? null : criteriaBuilder.equal(root.get("bin"), bin);
    }

    public static Specification<OrganizationEntity> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }
}
