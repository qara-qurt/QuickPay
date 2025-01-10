package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto convertTo(UserEntity userEntity){
        return UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole())
                .isActive(userEntity.isActive())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

}
