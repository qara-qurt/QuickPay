package kz.iitu.quick_pay.service.inventory;

import kz.iitu.quick_pay.dto.InventoryDto;
import kz.iitu.quick_pay.dto.InventoryGetDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryService {
    Long createInventory(InventoryDto inventoryDto);
    List<InventoryGetDto> getInventoryByProductId(Long productId, int page, int size);
    InventoryGetDto updateInventory(Long id, int totalCount);
}
