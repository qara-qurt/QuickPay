package kz.iitu.quick_pay.service.user;

import kz.iitu.quick_pay.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService extends UserDetailsService {
     Long createUser(UserDto userDto);
     Page<UserDto> getUsers(
             int page,
             int limit,
             String sort,
             String order,
             String search,
             Long organization_id
     );
     UserDto getUserById(Long id);
     UserDto getByUsername(String username);
     void deleteUser(Long id);
     UserDto updateUser(Long id, Map<String,Object> updates);
}
