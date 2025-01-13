package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.ProductDto;
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
@RequestMapping(ProductController.BASE_URL)
public class ProductController {

    // Base URL
    public static final String BASE_URL = "api/products";

    // Endpoints
    public static final String PRODUCT_BY_ID = "/{id}";
    public static final String PRODUCT_BY_ORGANIZATION_ID= "/organization/{id}";

    ProductService productService;

    @GetMapping(PRODUCT_BY_ORGANIZATION_ID)
    public List<ProductDto> getProductsByOrganizationId(
            @PathVariable Long id,
            @RequestParam(value = "page",defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10")  int limit) {
        return productService.getProductsByOrganizationId(id,page,limit);
    }

    @PostMapping()
    public ResponseEntity<Map<String,Long>> createProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(Map.of("id", productService.createProduct(productDto)));
    }

}
