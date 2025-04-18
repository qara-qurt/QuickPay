package kz.iitu.quick_pay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 6 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 6 characters")
    private String password;
}
