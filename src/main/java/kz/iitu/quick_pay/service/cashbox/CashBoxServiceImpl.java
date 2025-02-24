package kz.iitu.quick_pay.service.cashbox;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.CashBoxDto;
import kz.iitu.quick_pay.enitity.CashBoxEntity;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.exception.cashbox.CashBoxAlreadyExistException;
import kz.iitu.quick_pay.exception.cashbox.CashBoxNotFoundException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.repository.CashBoxRepository;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CashBoxServiceImpl implements CashBoxService {

    CashBoxRepository cashBoxRepository;
    OrganizationRepository organizationRepository;

    @Override
    public Long createCashBox(CashBoxDto cashBoxDto) {
        cashBoxRepository.findByCashBoxId(cashBoxDto.getCashboxId())
                .ifPresent(product -> {
                    throw new CashBoxAlreadyExistException("Cashbox with cashbox_id " + cashBoxDto.getCashboxId() + " already exist");
                });

        OrganizationEntity organization = organizationRepository.findById(cashBoxDto.getOrganizationId())
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with ID " + cashBoxDto.getOrganizationId() + " not found"));

        return cashBoxRepository.save(CashBoxEntity.builder()
                        .id(cashBoxDto.getId())
                        .organization(organization)
                        .name(cashBoxDto.getName())
                        .cashBoxId(cashBoxDto.getCashboxId())
                        .isActive(true) // true by default
                        .createdAt(cashBoxDto.getCreatedAt())
                        .updatedAt(cashBoxDto.getUpdatedAt())
                .build()).getId();
    }

    @Transactional
    @Override
    public CashBoxDto updateCashBox(Long id, Map<String, String> updates) {

        CashBoxEntity cashBox = cashBoxRepository.findById(id)
                .orElseThrow(() ->
                        new CashBoxNotFoundException(String.format("CashBox with id %s not found", id))
                );

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    cashBox.setName(value);
                    break;
                case "is_active":
                    cashBox.setActive(Boolean.parseBoolean(value));
                    break;
            }
        });
        cashBoxRepository.save(cashBox);

        return CashBoxDto.builder()
                .id(cashBox.getId())
                .name(cashBox.getName())
                .cashboxId(cashBox.getCashBoxId())
                .isActive(cashBox.isActive())
                .createdAt(cashBox.getCreatedAt())
                .updatedAt(cashBox.getUpdatedAt())
                .build();
    }

    @Override
    public void deleteCashBox(Long id) {
        cashBoxRepository
                .findById(id)
                .orElseThrow(() ->
                        new CashBoxNotFoundException(String.format("CashBox with id %s not found", id))
                );

        cashBoxRepository.deleteById(id);
    }

    @Override
    public List<CashBoxDto> getCashBoxesByOrganizationId(Long id) {
        return cashBoxRepository.findByOrganizationId(id)
                .stream()
                .map(CashBoxDto::convertTo)
                .toList();
    }

    @Override
    public CashBoxDto getCashBoxById(Long id) {
        return cashBoxRepository.findById(id)
                .map(CashBoxDto::convertTo)
                .orElseThrow(() -> new CashBoxNotFoundException("Cashbox with ID " + id + " not found"));
    }

    @Override
    public CashBoxDto getCashBoxByCashBoxId(String id) {
        return cashBoxRepository.findByCashBoxId(id)
                .map(CashBoxDto::convertTo)
                .orElseThrow(() -> new CashBoxNotFoundException("Cashbox with cashbox_id " + id + " not found"));
    }

    @Override
    public Page<CashBoxDto> getCashBoxes(int page, int limit, String sort, String order, String name, Boolean is_active, Long organization_id) {
        Specification<CashBoxEntity> spec = Specification
                .where(CashBoxSpecification.hasName(name))
                .and(CashBoxSpecification.isActive(is_active)
                .and(CashBoxSpecification.hasOrganizationId(organization_id)));

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        return cashBoxRepository.findAll(spec, pageable).map(CashBoxDto::convertTo);
    }
}
