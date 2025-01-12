package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.OrganizationDto;
import kz.iitu.quick_pay.service.organization.OrganizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(OrganizationController.BASE_URL)
public class OrganizationController {

    OrganizationService organizationService;

    // Base URL
    public static final String BASE_URL = "api/organizations";

    // Endpoints
    public static final String ORGANIZATION_BY_ID = "/{id}";

    @PostMapping()
    public ResponseEntity<Map<String,Long>> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        Long organizationId = organizationService.createOrganization(organizationDto);
        return ResponseEntity.ok(Map.of("id", organizationId));
    }

    @GetMapping(ORGANIZATION_BY_ID)
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long id) {
        OrganizationDto organization = organizationService.getOrganization(id);
        return ResponseEntity.ok(organization);
    }

    @PatchMapping(ORGANIZATION_BY_ID)
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long id, @RequestBody Map<String,String> updates) {
        OrganizationDto organization = organizationService.updateOrganization(id,updates);
        return ResponseEntity.ok(organization);
    }

    @DeleteMapping(ORGANIZATION_BY_ID)
    public ResponseEntity<Map<String,String>> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(Map.of("message", "Organization deleted"));
    }

}
