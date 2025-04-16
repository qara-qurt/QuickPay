package kz.iitu.quick_pay.service.cashbox;

import kz.iitu.quick_pay.dto.CashBoxDto;
import kz.iitu.quick_pay.dto.OrganizationDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CashBoxService {
    Long createCashBox(CashBoxDto cashBoxDto);
    CashBoxDto updateCashBox(Long id, Map<String,String> updates);
    void deleteCashBox(Long id);
    List<CashBoxDto> getCashBoxesByOrganizationId(Long id);
    CashBoxDto getCashBoxById(Long id);
    CashBoxDto getCashBoxByCashBoxId(String id);
    Page<CashBoxDto> getCashBoxes(
            int page,
            int limit,
            String sort,
            String order,
            String name,
            Boolean is_active,
            Long organization_id
    );
}
