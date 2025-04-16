package kz.iitu.quick_pay.service.product_rfid;

import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.dto.ProductRfidDto;
import kz.iitu.quick_pay.enitity.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductRfidService {
    boolean checkRfidIsExist(String rfid);
    ProductDto getProductByRfid(String rfid);
    void bindRfidToProduct(ProductRfidDto productRfidDto);
    List<String> getRfidTagsByProductId(Long productId);
    void deleteRfidTag(Long productId, String rfidTag);
}
