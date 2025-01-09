package kz.iitu.quick_pay.service.organization;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.OrganizationDto;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.exception.organization.OrganizationAlreadyExistException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Transactional
    @Override
    public Long createOrganization(OrganizationDto organizationDto) {
        boolean name = organizationRepository.existsByName(organizationDto.getName());
        boolean bin = organizationRepository.existsByBin(organizationDto.getBin());

        if (name) {
            throw new OrganizationAlreadyExistException("Organization with this name already exists");
        }

        if (bin) {
            throw new OrganizationAlreadyExistException("Organization with this bin already exists");
        }

        return organizationRepository.save(
                OrganizationEntity.builder()
                        .name(organizationDto.getName())
                        .bin(organizationDto.getBin())
                        .isActive(true)
                        .build()
        ).getId();
    }

    @Transactional
    @Override
    public OrganizationDto getOrganization(Long id) {
        OrganizationEntity organization = organizationRepository
                .findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(String.format("Organization with id %s not found", id))
                );
        return OrganizationDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .bin(organization.getBin())
                .isActive(organization.isActive())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public void deleteOrganization(Long id) {
        OrganizationEntity organization = organizationRepository
                .findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(String.format("Organization with id %s not found", id))
                );

        organization.setActive(false);
    }

    @Transactional
    @Override
    public OrganizationDto updateOrganization(Long id, Map<String, String> updates) {

        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(String.format("Organization with id %s not found", id))
                );

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    organization.setName((String) value);
                    break;
                case "bin":
                    organization.setBin((String) value);
                    break;
            }
        });

        organizationRepository.save(organization);

        return OrganizationDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .bin(organization.getBin())
                .createdAt(organization.getCreatedAt())
                .updatedAt(organization.getUpdatedAt())
                .isActive(organization.isActive())
                .build();
    }
}
