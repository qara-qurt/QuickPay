package kz.iitu.quick_pay.service.organization;

import kz.iitu.quick_pay.dto.OrganizationDto;

import java.util.Map;

public interface OrganizationService {
    Long createOrganization(OrganizationDto organizationDto);
    OrganizationDto getOrganization(Long id);
    void deleteOrganization(Long id);
    OrganizationDto updateOrganization(Long id, Map<String,String> updates);
}
