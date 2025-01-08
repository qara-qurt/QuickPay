package kz.iitu.quick_pay.service;

import kz.iitu.quick_pay.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
     Long createUser(UserDto userDto);
}
