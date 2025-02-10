package kz.iitu.quick_pay.enitity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    @ManyToOne()
    @JoinColumn(name = "organization_id", nullable = false)
    OrganizationEntity organization;

    @Column(nullable = false, unique = true)
    String rfidToken;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    int price;

    String size;

    String color;

    String image;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    LocalDateTime updatedAt;
}
