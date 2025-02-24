package kz.iitu.quick_pay.service.product_rfid;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.dto.ProductRfidDto;
import kz.iitu.quick_pay.enitity.ProductEntity;
import kz.iitu.quick_pay.enitity.ProductRFIDEntity;
import kz.iitu.quick_pay.exception.product.ProductNotFoundException;
import kz.iitu.quick_pay.repository.ProductRepository;
import kz.iitu.quick_pay.repository.ProductRfidRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRfidServiceImpl implements ProductRfidService {

    ProductRfidRepository productRfidRepository;
    ProductRepository productRepository;

    @Override
    public boolean checkRfidIsExist(String rfid) {
        return productRfidRepository.existsByRfidToken(rfid);
    }

    @Override
    public ProductDto getProductByRfid(String rfid) {
        ProductRFIDEntity productRFIDEntity = productRfidRepository.findByRfidToken(rfid);
        return ProductDto.convertTo(productRFIDEntity.getProduct());
    }

    @Transactional
    @Override
    public void bindRfidToProduct(ProductRfidDto productRfidDto) {
        Optional<ProductEntity> productOpt = productRepository.findById(productRfidDto.getProductId());

        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found with ID: " + productRfidDto.getProductId());
        }

        ProductEntity product = productOpt.get();

        List<ProductRFIDEntity> rfidEntities = productRfidDto.getRfidTags().stream()
                .map(tag -> ProductRFIDEntity.builder()
                        .rfidToken(tag)
                        .product(product)
                        .build()
                ).collect(Collectors.toList());

        productRfidRepository.saveAll(rfidEntities);
    }

    @Transactional
    public List<String> getRfidTagsByProductId(Long productId) {
        List<ProductRFIDEntity> rfidEntities = productRfidRepository.findByProductId(productId);
        return rfidEntities.stream().map(ProductRFIDEntity::getRfidToken).collect(Collectors.toList());
    }

    @Override
    public void deleteRfidTag(Long productId, String rfidTag) {
        Optional<ProductEntity> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            ProductEntity product = productOpt.get();
            product.getRfidTags().removeIf(tag -> tag.getRfidToken().equals(rfidTag));
            productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
    }

}
