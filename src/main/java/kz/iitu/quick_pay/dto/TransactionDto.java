package kz.iitu.quick_pay.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    @NotBlank(message = "Cashbox ID is required")
    String cashboxId;

    @NotEmpty(message = "Product list must not be empty")
    List<@NotNull ProductDto> products;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    Integer totalAmount;

    @NotNull(message = "Organization ID is required")
    Long organizationId;

    @NotBlank(message = "Payment method is required")
    String paymentMethod;
}
