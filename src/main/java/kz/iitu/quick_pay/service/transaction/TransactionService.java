package kz.iitu.quick_pay.service.transaction;

import kz.iitu.quick_pay.dto.CreateTransactionDto;
import kz.iitu.quick_pay.dto.PagedResponse;
import kz.iitu.quick_pay.dto.TransactionDto;

import java.util.List;


public interface TransactionService {
    Long createTransaction(CreateTransactionDto dto);
    PagedResponse<TransactionDto> getByOrganizationId(
            Long organizationId,
            String cashboxId,
            int page,
            int limit,
            String sort,
            String order);
}