package kz.iitu.quick_pay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Surname is required")
    String surname;
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 6 characters")
    String username;

    String isActive;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;
}
