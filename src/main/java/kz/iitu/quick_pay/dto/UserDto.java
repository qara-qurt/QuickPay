package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.Role;
import kz.iitu.quick_pay.enitity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    Long id;
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Surname is required")
    String surname;
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    String username;

    @JsonProperty("is_active")
    boolean isActive;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;

    List<Role> roles;

    @NotNull(message = "Organization ID is required")
    @JsonProperty("organization_id")
    private Long organizationId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static UserDto convertTo(UserEntity userEntity, OrganizationEntity organization) {
        return UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(null)
                .roles(userEntity.getRole())
                .isActive(userEntity.isActive())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .organizationId(organization != null ? organization.getId() : null)
                .build();
    }
}
