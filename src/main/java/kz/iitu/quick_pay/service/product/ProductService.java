package kz.iitu.quick_pay.service.product;

import kz.iitu.quick_pay.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    List<ProductDto> getProductsByOrganizationId(Long organizationId, int page, int size);
    Long createProduct(ProductDto productDto);
}
