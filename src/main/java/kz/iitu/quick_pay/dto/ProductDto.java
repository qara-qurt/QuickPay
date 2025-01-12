package kz.iitu.quick_pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kz.iitu.quick_pay.enitity.ProductEntity;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotBlank(message = "rfid_token is required")
    @JsonProperty("rfid_token")
    String rfidToken;

    @NotBlank(message = "name is required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    String name;

    @NotNull(message = "price is required")
    int price;

    String size;

    String color;

    String image;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    public static ProductDto convertTo(ProductEntity entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .organizationId(entity.getId())
                .rfidToken(entity.getRfidToken())
                .name(entity.getName())
                .price(entity.getPrice())
                .size(entity.getSize())
                .color(entity.getColor())
                .image(entity.getImage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
