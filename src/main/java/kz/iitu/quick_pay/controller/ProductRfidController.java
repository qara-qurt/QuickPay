package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.dto.ProductRfidDto;
import kz.iitu.quick_pay.dto.RFIDDto;
import kz.iitu.quick_pay.service.product_rfid.ProductRfidService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<Map<String, String>> detectRfid(@RequestBody RFIDDto rfid) {
        if (rfid == null || rfid.getRfidTag() == null || rfid.getCashbox_id() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "RFID data is missing"));
        }

        String rfidTag = rfid.getRfidTag();
        String cashboxId = rfid.getCashbox_id();

        boolean exist = productRfidService.checkRfidIsExist(rfidTag);

        try {
            if (exist) {
                ProductDto productDto = productRfidService.getProductByRfid(rfidTag);
                System.out.println("/topic/cash-box/" + cashboxId);
                messagingTemplate.convertAndSend("/topic/cash-box/" + cashboxId, productDto);
                return ResponseEntity.ok(Map.of("message", "Product sent to cash-box " + cashboxId));
            }

            System.out.println("/topic/register-mark/" + cashboxId);
            messagingTemplate.convertAndSend("/topic/register-mark/" + cashboxId, rfid);
            return ResponseEntity.ok(Map.of("message", "Product sent to register for cash-box " + cashboxId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error occurred: " + e.getMessage()));
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
