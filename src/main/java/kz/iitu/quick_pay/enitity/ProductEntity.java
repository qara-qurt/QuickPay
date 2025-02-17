package kz.iitu.quick_pay.enitity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import kz.iitu.quick_pay.utils.StringListConverter;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    OrganizationEntity organization;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    int price;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    List<String> sizes;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "TEXT")
    List<String> colors;

    String image;

    @JsonProperty("description")
    String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductRFIDEntity> rfidTags = new ArrayList<>();
}

