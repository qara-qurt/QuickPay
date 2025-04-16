package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.service.product.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
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
    ProductService productService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort", defaultValue = "updatedAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "organization_id", required = false) Long organization_id
    ) {

        Page<ProductDto> data = productService.getProducts(page, limit, sort, order, search, organization_id);
        return ResponseEntity.ok(Map.of("data", data));
    }
    @PostMapping()
    public ResponseEntity<Map<String,Long>> createProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(Map.of("id", productService.createProduct(productDto)));
    }

    @PatchMapping(PRODUCT_BY_ID)
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        ProductDto product = productService.updateProduct(id, updates);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping(PRODUCT_BY_ID)
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
