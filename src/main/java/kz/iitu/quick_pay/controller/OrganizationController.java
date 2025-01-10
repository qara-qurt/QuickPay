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
public class OrganizationController {
    OrganizationService organizationService;

    // REST API - routes
    public static final String CREATE_ORGANIZATION = "api/organizations";
    public static final String GET_ORGANIZATION = "api/organizations/{id}";
    public static final String DELETE_ORGANIZATION = "api/organizations/{id}";
    public static final String UPDATE_ORGANIZATION = "api/organizations/{id}";

    @PostMapping(CREATE_ORGANIZATION)
    public ResponseEntity<Map<String,Long>> createOrganization(@Valid @RequestBody OrganizationDto organizationDto) {
        Long organizationId = organizationService.createOrganization(organizationDto);
        return ResponseEntity.ok(Map.of("id", organizationId));
    }

    @GetMapping(GET_ORGANIZATION)
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long id) {
        OrganizationDto organization = organizationService.getOrganization(id);
        return ResponseEntity.ok(organization);
    }

    @PatchMapping(UPDATE_ORGANIZATION)
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long id, @RequestBody Map<String,String> updates) {
        OrganizationDto organization = organizationService.updateOrganization(id,updates);
        return ResponseEntity.ok(organization);
    }

    @DeleteMapping(DELETE_ORGANIZATION)
    public ResponseEntity<Map<String,String>> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(Map.of("message", "Organization deleted"));
    }

}
