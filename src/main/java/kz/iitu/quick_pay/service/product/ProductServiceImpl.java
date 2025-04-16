package kz.iitu.quick_pay.service.product;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.ProductDto;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.ProductEntity;
import kz.iitu.quick_pay.enitity.Role;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.product.ProductNotFoundException;
import kz.iitu.quick_pay.exception.user.UserNotFoundException;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.repository.ProductRepository;
import kz.iitu.quick_pay.service.user.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    OrganizationRepository organizationRepository;


    @Transactional
    @Override
    public Page<ProductDto> getProducts(
            int page,
            int limit,
            String sort,
            String order,
            String search,
            Long organizationId
    ) {
        Specification<ProductEntity> spec = ProductSpecification.hasSearchAndOrganization(search, organizationId);

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        return productRepository.findAll(spec, pageable).map(ProductDto::convertTo);
    }

    @Override
    public Long createProduct(ProductDto productDto) {
        OrganizationEntity organization = organizationRepository.findById(productDto.getOrganizationId())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with ID " + productDto.getOrganizationId() + " not found"));

        return productRepository.save(ProductEntity.builder()
                        .id(productDto.getId())
                        .organization(organization)
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .sizes(productDto.getSizes())
                        .colors(productDto.getColors())
                        .description(productDto.getDescription())
                        .image(productDto.getImage())
                        .updatedAt(productDto.getUpdatedAt())
                        .createdAt(productDto.getCreatedAt())
                        .build()).getId();
    }

    @Transactional
    @Override
    public ProductDto updateProduct(Long id, Map<String,Object> updates) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(()->
                        new ProductNotFoundException(String.format("Product with id %s not found", id))
                );

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    product.setName((String) value);
                    break;
                case "description":
                    product.setDescription((String) value);
                    break;
                case "price":
                    product.setPrice((int) value);
                    break;
                case "sizes":
                    product.setSizes((List<String>) value);
                    break;
                case "colors":
                    product.setColors((List<String>) value);
                    break;
                case "image":
                    product.setImage((String) value);
                    break;
                case "organization_id":
                    Long organizationId = Long.valueOf(value.toString());
                    OrganizationEntity organization = organizationRepository.findById(organizationId)
                            .orElseThrow(() -> new OrganizationNotFoundException("Organization with ID " + organizationId + " not found"));
                    product.setOrganization(organization);
                    break;

            }
        });

        ProductEntity productEntity = productRepository.save(product);
        return ProductDto.convertTo(productEntity);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
        productRepository.deleteById(id);
    }
}
