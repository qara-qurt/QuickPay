package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.CreateTransactionDto;
import kz.iitu.quick_pay.dto.PagedResponse;
import kz.iitu.quick_pay.dto.TransactionDto;
import kz.iitu.quick_pay.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String,Long>> createTransaction(@RequestBody CreateTransactionDto dto) {
        Long transactionId = transactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id",transactionId));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<TransactionDto>> getTransactionsByOrganizationId(
            @RequestParam("organization_id") Long organizationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(value = "cashbox_id", required = false, defaultValue = "") String cashboxId
    ) {
        return ResponseEntity.ok(
                transactionService.getByOrganizationId(organizationId, cashboxId, page, limit, sort, order)
        );
    }

}
