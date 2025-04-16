    package kz.iitu.quick_pay.enitity;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Table(name = "transactions")
    public class TransactionEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        Long id;

        @ManyToOne(optional = false)
        @JoinColumn(name = "cashbox_id", referencedColumnName = "cashbox_id")
        CashBoxEntity cashBox;

        @Column(nullable = false)
        Integer totalAmount;

        @Column(nullable = false)
        String paymentMethod;

        @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
        List<TransactionProductEntity> products;

        @ManyToOne(optional = false)
        @JoinColumn(name = "organization_id")
        OrganizationEntity organization;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false)
        LocalDateTime updatedAt;
    }
