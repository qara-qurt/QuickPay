package kz.iitu.quick_pay.service.transaction;

import kz.iitu.quick_pay.dto.TransactionDto;

import java.util.List;


public interface TransactionService {
    Long createTransaction(TransactionDto dto);
    List<TransactionDto> getByOrganizationId(Long organizationId, int page, int limit, String sort, String order);
}