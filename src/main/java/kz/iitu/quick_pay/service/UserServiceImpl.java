package kz.iitu.quick_pay.service;

import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.exception.UserAlreadyExistsException;
import kz.iitu.quick_pay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Long createUser(UserDto userDto) {

        // Checking if user with this username or email already exists
        boolean username = userRepository.existsByUsername(userDto.getUsername());
        boolean email = userRepository.existsByEmail(userDto.getEmail());

        if(username) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        if (email) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }


        return userRepository.save(
                UserEntity.builder()
                        .name(userDto.getName())
                        .surname(userDto.getSurname())
                        .isActive(true)
                        .username(userDto.getUsername())
                        .email(userDto.getEmail())
                        .password(userDto.getPassword())
                        .build()
        ).getId();
    }
}
