package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import kz.iitu.quick_pay.enitity.InventoryEntity;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InventoryDto {
    Long id;

    @NotNull(message = "organization_id is required")
    @JsonProperty("organization_id")
    Long organizationId;

    @NotNull(message = "product_id is required")
    @JsonProperty("product_id")
    Long productId;

    @NotNull(message = "totalCount is required")
    @JsonProperty("total_count")
    int totalCount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static InventoryDto convertTo(InventoryEntity entity) {
        return InventoryDto.builder()
                .id(entity.getId())
                .organizationId(entity.getId())
                .productId(entity.getProduct().getId())
                .totalCount(entity.getTotalCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
