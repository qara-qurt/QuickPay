package kz.iitu.quick_pay.service.cashbox;
import kz.iitu.quick_pay.enitity.CashBoxEntity;
import org.springframework.data.jpa.domain.Specification;

public class CashBoxSpecification {
    public static Specification<CashBoxEntity> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<CashBoxEntity> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }
}
