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
public class InventoryGetDto {
    Long id;

    @NotNull(message = "organization_id is required")
    @JsonProperty("organization_id")
    Long organizationId;

    @NotNull(message = "product_id is required")
    @JsonProperty("product")
    ProductDto product;

    @NotNull(message = "totalCount is required")
    int totalCount;


    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static InventoryGetDto convertTo(InventoryEntity entity) {
        return InventoryGetDto.builder()
                .id(entity.getId())
                .organizationId(entity.getId())
                .product(ProductDto.convertTo(entity.getProduct()))
                .totalCount(entity.getTotalCount())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
