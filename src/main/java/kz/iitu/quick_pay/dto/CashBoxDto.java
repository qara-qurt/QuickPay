package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import kz.iitu.quick_pay.enitity.CashBoxEntity;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CashBoxDto {
    Long id;

    @NotNull(message = "name is required")
    String name;

    @NotNull(message = "cashbox_id is required")
    @JsonProperty("cashbox_id")
    String cashboxId;

    @NotNull(message = "organization_id is required")
    @JsonProperty("organization_id")
    Long organizationId;

    @JsonProperty("is_active")
    boolean isActive = true;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static CashBoxDto convertTo(CashBoxEntity entity) {
        return CashBoxDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isActive(entity.isActive())
                .cashboxId(entity.getCashBoxId())
                .organizationId(entity.getOrganization().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
