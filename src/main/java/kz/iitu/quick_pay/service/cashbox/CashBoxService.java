package kz.iitu.quick_pay.service.cashbox;

import kz.iitu.quick_pay.dto.CashBoxDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CashBoxService {
    Long createCashBox(CashBoxDto cashBoxDto);
    List<CashBoxDto> getCashBoxesByOrganizationId(Long id);
    CashBoxDto getCashBoxById(Long id);
    CashBoxDto getCashBoxByCashBoxId(String id);
}
