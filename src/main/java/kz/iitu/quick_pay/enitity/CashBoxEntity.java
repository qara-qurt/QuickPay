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
@Table(name = "cashboxes")
public class CashBoxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String name;

    @Column(name = "cashbox_id", nullable = false, unique = true)
    String cashBoxId;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    OrganizationEntity organization;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at",nullable = false)
    LocalDateTime updatedAt;
}
