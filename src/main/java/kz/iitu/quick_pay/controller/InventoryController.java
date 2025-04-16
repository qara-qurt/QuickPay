package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.InventoryDto;
import kz.iitu.quick_pay.dto.InventoryGetDto;
import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.service.inventory.InventoryService;
import kz.iitu.quick_pay.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(InventoryController.BASE_URL)
public class  InventoryController {

    // Base URL
    public static final String BASE_URL = "api/inventory";

    // Endpoints
    public static final String INVENTORY_BY_ORGANIZATION_ID= "/organization/{id}";
    public static final String INVENTORY_BY_ID= "/{id}";

    InventoryService inventoryService;

    @PostMapping()
    public ResponseEntity<Map<String,Long>> createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        return ResponseEntity.ok(Map.of("id", inventoryService.createInventory(inventoryDto)));
    }

    @GetMapping(INVENTORY_BY_ORGANIZATION_ID)
    public List<InventoryGetDto> getInventoryByOrganizationId(
            @PathVariable Long id,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10")  int limit){

        return inventoryService.getInventoryByProductId(id, page, limit);
    }

    @PatchMapping(INVENTORY_BY_ID)
    public InventoryGetDto updateInventory(@PathVariable Long id, @RequestBody Map<String,Integer> updates) {
        int total_count = updates.get("total_count");
        return inventoryService.updateInventory(id,total_count);
    }
}
