package kz.iitu.quick_pay.service.transaction;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.TransactionDto;
import kz.iitu.quick_pay.enitity.*;
import kz.iitu.quick_pay.exception.cashbox.CashBoxNotFoundException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.product.ProductNotFoundException;
import kz.iitu.quick_pay.repository.CashBoxRepository;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.repository.ProductRepository;
import kz.iitu.quick_pay.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final OrganizationRepository organizationRepository;
    private final CashBoxRepository cashBoxRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Long createTransaction(TransactionDto dto) {
        CashBoxEntity cashBox = cashBoxRepository.findByCashBoxId(dto.getCashboxId())
                .orElseThrow(() -> new CashBoxNotFoundException("Cashbox not found"));

        List<TransactionProductEntity> productLinks = new ArrayList<>();

        OrganizationEntity organization = organizationRepository.findById(dto.getOrganizationId())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found"));


        TransactionEntity transaction = TransactionEntity.builder()
                .cashBox(cashBox)
                .paymentMethod(dto.getPaymentMethod())
                .totalAmount(dto.getTotalAmount())
                .organization(organization)
                .build();

        for (Long productId : dto.getProductIds()) {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
            productLinks.add(TransactionProductEntity.builder()
                    .transaction(transaction)
                    .product(product)
                    .quantity(1)
                    .build());
        }

        transaction.setProducts(productLinks);
        return transactionRepository.save(transaction).getId();
    }
}