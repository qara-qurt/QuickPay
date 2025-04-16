package kz.iitu.quick_pay.enitity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transaction_products")
public class TransactionProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_id")
    TransactionEntity transaction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    ProductEntity product;

    @Column(nullable = false)
    Integer quantity;
}