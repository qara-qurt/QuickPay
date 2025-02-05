package kz.iitu.quick_pay.service.user;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.dto.UserLoginDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService extends UserDetailsService {
     Long createUser(UserDto userDto);
     UserDto getUserById(Long id);
     UserDto getByUsername(String username);
     void deleteUser(Long id);
     UserDto updateUser(Long id, Map<String,Object> updates);
}
