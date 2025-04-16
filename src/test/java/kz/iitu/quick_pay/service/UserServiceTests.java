package kz.iitu.quick_pay.service;

import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.exception.user.UserAlreadyExistsException;
import kz.iitu.quick_pay.exception.user.UserNotFoundException;
import kz.iitu.quick_pay.repository.UserRepository;
import kz.iitu.quick_pay.service.user.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testCreateUser() {
        Mockito.when(userRepository.existsByUsername("test1")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("test1@gmail.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        UserEntity mockUserEntity = UserEntity.builder()
                .id(99L)
                .name("Test1")
                .surname("Test1")
                .username("test1")
                .password("hashedPassword")
                .email("test1@gmail.com")
                .isActive(true)
                .build();

        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(mockUserEntity);

        Long id = userService.createUser(UserDto.builder()
                .name("Test1")
                .surname("Test1")
                .username("test1")
                .email("test1@gmail.com")
                .password("password")
                .build());

        Assertions.assertEquals(99L, id);
    }

    @Test
    void testCreateUserWithExistingUsername() {
        String existingUsername = "test";

        Mockito.when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        UserDto userDto = UserDto.builder()
                .name("Test1")
                .surname("Test1")
                .username(existingUsername)
                .email("test@gmail.com")
                .build();

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto));
    }

    @Test
    void testCreateUserWithExistingEmail() {
        String existingEmail = "test@gmail.com";

        Mockito.when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        UserDto userDto = UserDto.builder()
                .name("Test1")
                .surname("Test1")
                .username("test1")
                .email(existingEmail)
                .build();

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto));
    }

    @Test
    void testGetUserById() {
        Long id = 1L;

        UserEntity user = UserEntity.builder()
                .id(id)
                .name("Test1")
                .surname("Test1")
                .username("test1")
                .email("test1@gmail.com")
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(id);

        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("Test1", result.getName());
    }

    @Test
    void testUserNotFoundById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;

        UserEntity user = UserEntity.builder()
                .id(id)
                .name("Test1")
                .surname("Test1")
                .username("test1")
                .email("test1@gmail.com")
                .isActive(true)
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        userService.deleteUser(id);

        Assertions.assertFalse(user.isActive());
    }

    @Test
    void testDeleteUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;

        UserEntity user = UserEntity.builder()
                .id(id)
                .name("Test1")
                .surname("Test1")
                .username("test1")
                .email("test1@gmail.com")
                .password("hashedPassword")
                .isActive(true)
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUser(id, Map.of("name", "UpdatedName"));

        Assertions.assertEquals("UpdatedName", result.getName());
    }

    @Test
    void testUpdateUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, Map.of("name", "UpdatedName")));
    }
}
