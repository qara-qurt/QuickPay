package kz.iitu.quick_pay.service.user;

import kz.iitu.quick_pay.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {
     Long createUser(UserDto userDto);
     UserDto getUserById(Long id);
     void deleteUser(Long id);
     UserDto updateUser(Long id, Map<String,Object> updates);
}
