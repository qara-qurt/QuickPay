package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.ProductRfidDto;
import kz.iitu.quick_pay.dto.RFIDDto;
import kz.iitu.quick_pay.service.product_rfid.ProductRfidService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(ProductRfidController.BASE_URL)
public class ProductRfidController {

    SimpMessagingTemplate messagingTemplate;
    ProductRfidService productRfidService;

    // Base URL
    public static final String BASE_URL = "api/rfid";

    public static final String BIND = "/bind";

    @PostMapping
    public void detectRfid(@RequestBody RFIDDto rfid) {
        if (rfid != null) {
            boolean exist = productRfidService.checkRfidIsExist(rfid.getRfidTag());
            try {
                if (exist) {
                    messagingTemplate.convertAndSend("/topic/cash-box", rfid);
                } else {
                    messagingTemplate.convertAndSend("/topic/register-mark", rfid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping(BIND)
    public ResponseEntity<Map<String,String>>bindRfidToProduct(@RequestBody ProductRfidDto productRfidDto) {
        productRfidService.bindRfidToProduct(productRfidDto);
        return ResponseEntity.ok(Map.of("message", "Successfully rfid is bound"));
    }

    @GetMapping("/{productId}/rfid-tags")
    public ResponseEntity<List<String>> getRfidTags(@PathVariable Long productId) {
        List<String> rfidTags = productRfidService.getRfidTagsByProductId(productId);
        return ResponseEntity.ok(rfidTags);
    }

    @DeleteMapping("/{productId}/rfid-tags/{rfidTag}")
    public ResponseEntity<Map<String,String>> deleteRfidTag(@PathVariable Long productId, @PathVariable String rfidTag) {
        productRfidService.deleteRfidTag(productId, rfidTag);
        return ResponseEntity.ok(Map.of("message", "Successfully rfid is deleted from product"));
    }

}
