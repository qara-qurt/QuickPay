package kz.iitu.quick_pay.service.organization;

import kz.iitu.quick_pay.dto.OrganizationDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface OrganizationService {
    Long createOrganization(OrganizationDto organizationDto);
    OrganizationDto getOrganization(Long id);
    Page<OrganizationDto> getAllOrganizations(int page, int limit, String sort, String order, String name, String bin, Boolean isActive);
    void deleteOrganization(Long id);
    OrganizationDto updateOrganization(Long id, Map<String,String> updates);
}
