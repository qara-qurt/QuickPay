package kz.iitu.quick_pay.service;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.exception.UserAlreadyExistsException;
import kz.iitu.quick_pay.exception.UserNotFoundException;
import kz.iitu.quick_pay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


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
                        .password(passwordEncoder.encode(userDto.getPassword()))
                        .build()
        ).getId();
    }

    @Transactional
    @Override
    public UserDto getUserById(Long id) {
        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(String.format("User with id %s not found", id))
                );
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .isActive(user.isActive())
                .build();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(String.format("User with id %s not found", id))
                );
        // Make user inactive
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long id, Map<String,Object> updates) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(()->
                        new UserNotFoundException(String.format("User with id %s not found", id))
                );

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "surname":
                    user.setSurname((String) value);
                    break;
                case "username":
                    user.setUsername((String) value);
                    break;
                case "email":
                    String email = (String) value;
                    if (!isValidEmail(email)) {
                        throw new IllegalArgumentException("Invalid email format: " + email);
                    }
                    user.setEmail((String) value);
                    break;
                case "password":
                    user.setPassword(passwordEncoder.encode((String) value));
                    break;
            }
        });
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    // Helper method to validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && email.matches(emailRegex);
    }
}
