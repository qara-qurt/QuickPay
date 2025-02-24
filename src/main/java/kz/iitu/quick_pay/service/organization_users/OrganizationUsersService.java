package kz.iitu.quick_pay.service.organization_users;

import kz.iitu.quick_pay.dto.OrganizationUsersDto;
import kz.iitu.quick_pay.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizationUsersService {
    OrganizationUsersDto getAllUsersByOrganizationId(Long organizationId);
}
