package kz.iitu.quick_pay.service.user;
import kz.iitu.quick_pay.enitity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import kz.iitu.quick_pay.enitity.Role;

public class UserSpecification {
    public static Specification<UserEntity> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Возвращаем всё, если search пустой
            }
            String likeSearch = "%" + search.toLowerCase() + "%";

            Join<UserEntity, Role> roleJoin = root.join("role");

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likeSearch),
                    criteriaBuilder.like(criteriaBuilder.lower(roleJoin.as(String.class)), likeSearch)
            );
        };
    }
}
