package kz.iitu.quick_pay.service.cashbox;

import kz.iitu.quick_pay.dto.CashBoxDto;
import kz.iitu.quick_pay.enitity.CashBoxEntity;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.exception.cashbox.CashBoxAlreadyExistException;
import kz.iitu.quick_pay.exception.cashbox.CashBoxNotFoundException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.exception.product.ProductAlreadyExist;
import kz.iitu.quick_pay.repository.CashBoxRepository;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

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
                        .createdAt(cashBoxDto.getCreatedAt())
                        .updatedAt(cashBoxDto.getUpdatedAt())
                .build()).getId();
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
}
