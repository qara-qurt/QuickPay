package kz.iitu.quick_pay.service.organization_users;

import kz.iitu.quick_pay.dto.OrganizationDto;
import kz.iitu.quick_pay.dto.OrganizationUsersDto;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.OrganizationUsersEntity;
import kz.iitu.quick_pay.repository.OrganizationUsersRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrganizationUsersServiceImpl implements OrganizationUsersService {

    OrganizationUsersRepository organizationUsersRepository;

    @Override
    public OrganizationUsersDto getAllUsersByOrganizationId(Long organizationId) {
        List<OrganizationUsersEntity> organizationUsersEntities = organizationUsersRepository.findByOrganizationId(organizationId);

        if(organizationUsersEntities.isEmpty()) {
            return null;
        }

        OrganizationDto organization = OrganizationDto
                .builder()
                .id(organizationUsersEntities.getFirst().getOrganization().getId())
                .name(organizationUsersEntities.getFirst().getOrganization().getName())
                .bin(organizationUsersEntities.getFirst().getOrganization().getBin())
                .isActive(organizationUsersEntities.getFirst().getOrganization().isActive())
                .createdAt(organizationUsersEntities.getFirst().getOrganization().getCreatedAt())
                .updatedAt(organizationUsersEntities.getFirst().getOrganization().getUpdatedAt())
                .build();

        List<UserDto> users = organizationUsersEntities
                .stream()
                .map(user -> UserDto.convertTo(user.getUser(),user.getOrganization()))
                .toList();

        return OrganizationUsersDto
                .builder()
                .organization(organization)
                .users(users)
                .build();
    }
}
