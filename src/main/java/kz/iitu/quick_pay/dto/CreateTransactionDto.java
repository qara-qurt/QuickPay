package kz.iitu.quick_pay.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransactionDto {
    @NotBlank(message = "Cashbox ID is required")
    private String cashboxId;

    @NotEmpty(message = "Product list must not be empty")
    private List<@NotNull Long> productIds;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Integer totalAmount;

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
}
