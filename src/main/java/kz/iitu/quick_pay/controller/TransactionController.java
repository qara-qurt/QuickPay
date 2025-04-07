package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.TransactionDto;
import kz.iitu.quick_pay.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Map<String,Long>> createTransaction(@RequestBody TransactionDto dto) {
        Long transactionId = transactionService.createTransaction(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id",transactionId));
    }
}
