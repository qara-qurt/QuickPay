package kz.iitu.quick_pay.service.user;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import kz.iitu.quick_pay.enitity.OrganizationUsersEntity;
import kz.iitu.quick_pay.enitity.UserEntity;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {
    public static Specification<UserEntity> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + search.toLowerCase() + "%";

            assert query != null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<OrganizationUsersEntity> organizationUsersRoot = subquery.from(OrganizationUsersEntity.class);
            subquery.select(organizationUsersRoot.get("user").get("id"))
                    .where(criteriaBuilder.like(criteriaBuilder.lower(organizationUsersRoot.get("organization").get("name")), pattern));

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), pattern),
                    criteriaBuilder.in(root.get("id")).value(subquery)
            );
        };
    }
}
