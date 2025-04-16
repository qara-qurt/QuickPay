package kz.iitu.quick_pay.service;

import kz.iitu.quick_pay.dto.OrganizationDto;
import kz.iitu.quick_pay.enitity.OrganizationEntity;
import kz.iitu.quick_pay.exception.organization.OrganizationAlreadyExistException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.repository.OrganizationRepository;
import kz.iitu.quick_pay.service.organization.OrganizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTests {

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @Test
    void testCreateOrganization() {
        Mockito.when(organizationRepository.existsByName("Test Organization")).thenReturn(false);
        Mockito.when(organizationRepository.existsByBin("123456789012")).thenReturn(false);

        OrganizationEntity mockOrganizationEntity = OrganizationEntity.builder()
                .id(99L)
                .name("Test Organization")
                .bin("123456789012")
                .isActive(true)
                .build();

        Mockito.when(organizationRepository.save(Mockito.any(OrganizationEntity.class))).thenReturn(mockOrganizationEntity);

        Long id = organizationService.createOrganization(OrganizationDto.builder()
                .name("Test Organization")
                .bin("123456789012")
                .build());

        Assertions.assertEquals(99L, id);
    }

    @Test
    void testCreateOrganizationWithExistingName() {
        Mockito.when(organizationRepository.existsByName("Existing Organization")).thenReturn(true);

        OrganizationDto organizationDto = OrganizationDto.builder()
                .name("Existing Organization")
                .bin("123456789012")
                .build();

        Assertions.assertThrows(OrganizationAlreadyExistException.class, () -> organizationService.createOrganization(organizationDto));
    }

    @Test
    void testCreateOrganizationWithExistingBin() {
        Mockito.when(organizationRepository.existsByBin("123456789012")).thenReturn(true);

        OrganizationDto organizationDto = OrganizationDto.builder()
                .name("Test Organization")
                .bin("123456789012")
                .build();

        Assertions.assertThrows(OrganizationAlreadyExistException.class, () -> organizationService.createOrganization(organizationDto));
    }

    @Test
    void testGetOrganization() {
        Long id = 1L;

        OrganizationEntity organization = OrganizationEntity.builder()
                .id(id)
                .name("Test Organization")
                .bin("123456789012")
                .isActive(true)
                .build();

        Mockito.when(organizationRepository.findById(id)).thenReturn(Optional.of(organization));

        OrganizationDto result = organizationService.getOrganization(id);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("Test Organization", result.getName());
    }

    @Test
    void testGetOrganizationNotFound() {
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrganizationNotFoundException.class, () -> organizationService.getOrganization(1L));
    }

    @Test
    void testDeleteOrganization() {
        Long id = 1L;

        OrganizationEntity organization = OrganizationEntity.builder()
                .id(id)
                .name("Test Organization")
                .bin("123456789012")
                .isActive(true)
                .build();

        Mockito.when(organizationRepository.findById(id)).thenReturn(Optional.of(organization));

        organizationService.deleteOrganization(id);

        Assertions.assertFalse(organization.isActive());
    }

    @Test
    void testDeleteOrganizationNotFound() {
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrganizationNotFoundException.class, () -> organizationService.deleteOrganization(1L));
    }

    @Test
    void testUpdateOrganization() {
        Long id = 1L;

        OrganizationEntity organization = OrganizationEntity.builder()
                .id(id)
                .name("Test Organization")
                .bin("123456789012")
                .isActive(true)
                .build();

        Mockito.when(organizationRepository.findById(id)).thenReturn(Optional.of(organization));

        OrganizationDto result = organizationService.updateOrganization(id, Map.of("name", "Updated Organization"));

        Assertions.assertEquals("Updated Organization", result.getName());
        Mockito.verify(organizationRepository).save(organization);
    }

    @Test
    void testUpdateOrganizationNotFound() {
        Mockito.when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(OrganizationNotFoundException.class, () -> organizationService.updateOrganization(1L, Map.of("name", "Updated Organization")));
    }
}
