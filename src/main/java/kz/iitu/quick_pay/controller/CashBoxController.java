package kz.iitu.quick_pay.controller;


import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.CashBoxDto;
import kz.iitu.quick_pay.service.cashbox.CashBoxService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    public static final String BASE_URL = "api/cash-box";

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

    @GetMapping()
    public CashBoxDto getCashBoxByCashBoxId(@RequestParam String cashbox_id ){
        return cashBoxService.getCashBoxByCashBoxId(cashbox_id);
    }

    // WebSocket
    @MessageMapping("/test")
    @SendTo("/topic/cash-boxes")
    public String processMessage(String message){
        return "Hello";
    }
}
