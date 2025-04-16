package kz.iitu.quick_pay.service.product;

import kz.iitu.quick_pay.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    Page<ProductDto> getProducts(
            int page,
            int limit,
            String sort,
            String order,
            String search,
            Long organizationId
    );
    Long createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, Map<String,Object> updates);
    void deleteProduct(Long id);
}
