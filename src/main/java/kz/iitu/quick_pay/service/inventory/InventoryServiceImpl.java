package kz.iitu.quick_pay.service.inventory;

import kz.iitu.quick_pay.dto.InventoryDto;
import kz.iitu.quick_pay.dto.InventoryGetDto;
import kz.iitu.quick_pay.enitity.InventoryEntity;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.enitity.ProductEntity;
import kz.iitu.quick_pay.exception.inventory.InventoryNotFoundException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.product.ProductAlreadyExist;
import kz.iitu.quick_pay.exception.product.ProductNotFoundException;
import kz.iitu.quick_pay.repository.InventoryRepository;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {

    InventoryRepository inventoryRepository;
    OrganizationRepository organizationRepository;
    ProductRepository productRepository;

    @Override
    public Long createInventory(InventoryDto inventoryDto) {
        Optional<OrganizationEntity> organization = organizationRepository
                .findById(inventoryDto.getOrganizationId());
        if (organization.isEmpty()) {
            throw new OrganizationNotFoundException("Organization with ID " + inventoryDto.getOrganizationId() + " not found");
        }

        Optional<ProductEntity> product = productRepository.findById(inventoryDto.getProductId());
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with ID " + inventoryDto.getProductId() + " not found");
        }

        boolean isExist = inventoryRepository.existsByProductId(inventoryDto.getProductId());
        if (isExist) {
            throw new ProductAlreadyExist("Inventory with product_id " + inventoryDto.getProductId() + " already exist");
        }

        return inventoryRepository.save(InventoryEntity.builder()
                .id(inventoryDto.getId())
                .organization(organization.get())
                .product(product.get())
                .totalCount(inventoryDto.getTotalCount())
                .createdAt(inventoryDto.getCreatedAt())
                .updatedAt(inventoryDto.getUpdatedAt())
                .build()).getId();
    }

    @Override
    public List<InventoryGetDto> getInventoryByProductId(Long productId, int page, int size) {
        Optional<OrganizationEntity> organization = organizationRepository
                .findById(productId);
        if (organization.isEmpty()) {
            throw new OrganizationNotFoundException("Organization with ID " + productId + " not found");
        }

        Pageable pageable = (Pageable) PageRequest.of(page - 1, size);

        return inventoryRepository.findByProductId(productId, pageable)
                .stream()
                .map(InventoryGetDto::convertTo)
                .toList();
    }

    @Override
    public InventoryGetDto updateInventory(Long id, int totalCount) {
        Optional<InventoryEntity> inventoryEntity = inventoryRepository.findById(id);
        if (inventoryEntity.isEmpty()) {
            throw new InventoryNotFoundException("Inventory with ID " + id + " not found");
        }


        return InventoryGetDto.convertTo(InventoryEntity.builder()
                .id(id)
                .organization(inventoryEntity.get().getOrganization())
                .product(inventoryEntity.get().getProduct())
                .totalCount(totalCount)
                .createdAt(inventoryEntity.get().getCreatedAt())
                .updatedAt(inventoryEntity.get().getUpdatedAt())
                .build());
    }
}
