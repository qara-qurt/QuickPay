package kz.iitu.quick_pay.enitity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "inventory")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne()
    @JoinColumn(name = "organization_id", nullable = false)
    OrganizationEntity organization;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity product;

    @Column(name = "total_count", nullable = false)
    int totalCount;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    LocalDateTime updatedAt;
}

