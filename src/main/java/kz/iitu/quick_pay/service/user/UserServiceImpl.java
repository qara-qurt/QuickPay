package kz.iitu.quick_pay.service.user;

import jakarta.transaction.Transactional;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.dto.UserLoginDto;
import kz.iitu.quick_pay.enitity.Role;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.exception.user.UserAlreadyExistsException;
import kz.iitu.quick_pay.exception.user.UserNotFoundException;
import kz.iitu.quick_pay.repository.UserRepository;
import kz.iitu.quick_pay.utils.JwtTokenUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
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
                        .isActive(true) // DEFAULT is_active is TRUE
                        .username(userDto.getUsername())
                        .email(userDto.getEmail())
                        .password(passwordEncoder.encode(userDto.getPassword()))
                        .role(List.of(Role.USER)) // DEFAULT role is ROLE_USER
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
        return UserDto.convertTo(user);
    }

    @Override
    public UserDto getByUsername(String username) {
       UserEntity userEntity = userRepository.findByUsername(username)
               .orElseThrow(()->
                       new UserNotFoundException(String.format("User with username %s not found", username))
               );

       return UserDto.convertTo(userEntity);

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

        UserEntity userEntity = userRepository.save(user);
        return UserDto.convertTo(userEntity);
    }

    // Helper method to validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && email.matches(emailRegex);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity  userEntity = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("User with username " + username + " not found")
                );

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().stream().map(Enum::name).toArray(String[]::new))
                .build();
    }
}
