package kz.iitu.quick_pay.service.transaction;

import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.CreateTransactionDto;
import kz.iitu.quick_pay.dto.PagedResponse;
import kz.iitu.quick_pay.dto.ProductDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Long createTransaction(CreateTransactionDto dto) {
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

    @Override
    public PagedResponse<TransactionDto> getByOrganizationId(Long organizationId, String cashboxId, int page, int limit, String sort, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        Page<TransactionEntity> transactions;

        if (cashboxId == null || cashboxId.isBlank()) {
            transactions = transactionRepository.findByOrganization_Id(organizationId, pageable);
        } else {
            transactions = transactionRepository.findByOrganizationIdAndCashBox_CashBoxId(organizationId, cashboxId, pageable);
        }

        List<TransactionDto> transactionDtos = transactions.stream()
                .map(transaction -> TransactionDto.builder()
                        .id(transaction.getId())
                        .cashboxId(transaction.getCashBox().getCashBoxId())
                        .organizationId(transaction.getOrganization().getId())
                        .paymentMethod(transaction.getPaymentMethod())
                        .totalAmount(transaction.getTotalAmount())
                        .createdAt(transaction.getCreatedAt())
                        .updatedAt(transaction.getUpdatedAt())
                        .products(transaction.getProducts().stream()
                                .map(p -> ProductDto.convertTo(p.getProduct()))
                                .toList())
                        .build())
                .toList();

        return new PagedResponse<>(transactions.getTotalElements(), transactionDtos);
    }


}