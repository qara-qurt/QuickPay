package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.iitu.quick_pay.enitity.ProductEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    Long id;

    @NotNull(message = "organization_id is required")
    @JsonProperty("organization_id")
    Long organizationId;

    @NotBlank(message = "name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    String name;

    @NotNull(message = "price is required")
    int price;

    List<String> sizes;

    List<String> colors;

    String image;

    String description;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public static ProductDto convertTo(ProductEntity entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .organizationId(entity.getOrganization().getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .sizes(entity.getSizes())
                .colors(entity.getColors())
                .image(entity.getImage())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
