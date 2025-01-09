package kz.iitu.quick_pay.service;

import kz.iitu.quick_pay.dto.UserDto;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {
     Long createUser(UserDto userDto);
     UserDto getUserById(Long id);
     void deleteUser(Long id);
     UserDto updateUser(Long id, Map<String,Object> updates);
}
