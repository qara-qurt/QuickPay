package kz.iitu.quick_pay.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrganizationUsersDto {

    OrganizationDto organization;
    List<UserDto> users;
}
