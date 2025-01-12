package kz.iitu.quick_pay.service.product;

import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.ProductEntity;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.product.ProductWithThisRfidAlreadyExist;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    OrganizationRepository organizationRepository;


    @Override
    public List<ProductDto> getProductsByOrganizationId(Long organizationId) {
        return productRepository.findByOrganizationId(organizationId)
                .stream()
                .map(ProductDto::convertTo)
                .toList();
    }

    @Override
    public Long createProduct(ProductDto productDto) {
        productRepository.findByRfidToken(productDto.getRfidToken())
                .ifPresent(product -> {
                    throw new ProductWithThisRfidAlreadyExist("Product with rfid_token " + productDto.getRfidToken() + " already exist");
                });

        OrganizationEntity organization = organizationRepository.findById(productDto.getOrganizationId())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with ID " + productDto.getOrganizationId() + " not found"));

        return productRepository.save(ProductEntity.builder()
                        .id(productDto.getId())
                        .organization(organization)
                        .rfidToken(productDto.getRfidToken())
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .size(productDto.getSize())
                        .color(productDto.getColor())
                        .image(productDto.getImage())
                        .updatedAt(productDto.getUpdatedAt())
                        .createdAt(productDto.getCreatedAt())
                        .build()).getId();
    }
}
