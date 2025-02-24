package kz.iitu.quick_pay.controller;


import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.CashBoxDto;
import kz.iitu.quick_pay.dto.OrganizationDto;
import kz.iitu.quick_pay.service.cashbox.CashBoxService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(CashBoxController.BASE_URL)
public class CashBoxController {

    // Base URL
    public static final String BASE_URL = "api/cash-boxes";

    // Endpoints
    public static final String CASH_BOXES_BY_ORGANIZATION_ID= "/organization/{id}";
    public static final String CASH_BOX_BY_ID= "/{id}";

    CashBoxService cashBoxService;

    @PostMapping()
    public ResponseEntity<Map<String,Long>> createCashBox(@Valid @RequestBody CashBoxDto cashBoxDto) {
        return ResponseEntity.ok(Map.of("id", cashBoxService.createCashBox(cashBoxDto)));
    }

    @GetMapping(CASH_BOXES_BY_ORGANIZATION_ID)
    public List<CashBoxDto> getCashBoxByOrganizationId(@PathVariable Long id){
        return cashBoxService.getCashBoxesByOrganizationId(id);
    }

    @GetMapping(CASH_BOX_BY_ID)
    public CashBoxDto getCashBoxById(@PathVariable Long id){
        return cashBoxService.getCashBoxById(id);
    }

    @PatchMapping(CASH_BOX_BY_ID)
    public ResponseEntity<CashBoxDto> getOrganization(@PathVariable Long id, @RequestBody Map<String,String> updates) {
        CashBoxDto cashBox = cashBoxService.updateCashBox(id,updates);
        return ResponseEntity.ok(cashBox);
    }

    @DeleteMapping(CASH_BOX_BY_ID)
    public ResponseEntity<String> deleteCashBox(@PathVariable Long id){
        cashBoxService.deleteCashBox(id);
        return ResponseEntity.ok("CashBox with id " + id + " deleted");
    }

//    @GetMapping()
//    public CashBoxDto getCashBoxByCashBoxId(@RequestParam String cashbox_id ){
//        return cashBoxService.getCashBoxByCashBoxId(cashbox_id);
//    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getOrganizations(
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10")  int limit,
            @RequestParam(value = "sort", defaultValue = "updatedAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "is_active", required = false) Boolean is_active,
            @RequestParam(value = "organization_id", required = false) Long organization_id){

        Page<CashBoxDto> data = cashBoxService.getCashBoxes(page, limit, sort, order, name, is_active,organization_id);
        return ResponseEntity.ok(Map.of("data", data));
    }

}
