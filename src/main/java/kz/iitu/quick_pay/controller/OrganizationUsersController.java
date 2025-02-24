package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.OrganizationUsersDto;
import kz.iitu.quick_pay.service.organization_users.OrganizationUsersService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(OrganizationUsersController.BASE_URL)
public class OrganizationUsersController {

    // Base URL
    public static final String BASE_URL = "api/organization-users";

    // Endpoints
    public static final String ORGANIZATION_USERS_BY_ID = "/{id}";

    OrganizationUsersService organizationUsersService;

    @GetMapping(ORGANIZATION_USERS_BY_ID)
    public OrganizationUsersDto getAllUsersByOrganizationId(@PathVariable Long id) {
        return organizationUsersService.getAllUsersByOrganizationId(id);
    }

//    @PostMapping()
//    public ResponseEntity<Map<String,String>> createOrganizationUsers(@RequestBody OrganizationUsersDto organizationUsersDto) {
//        return organizationUsersService.createOrganizationUsers(organizationUsersDto);
//    }
}
