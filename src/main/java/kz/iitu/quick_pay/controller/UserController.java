package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.dto.UserLoginDto;
import kz.iitu.quick_pay.service.user.UserService;
import kz.iitu.quick_pay.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(UserController.BASE_URL)
public class UserController {

    // Base URL
    public static final String BASE_URL = "api/users";

    // Endpoints
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String USER_BY_ID = "/{id}";

    UserService userService;
    JwtTokenUtils jwtTokenUtils;
    AuthenticationManager authenticationManager;

    @PostMapping(REGISTER)
    public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody UserDto userDto) {
        Long userId = userService.createUser(userDto);
        return ResponseEntity.ok(Map.of("id", userId));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        // Аутентификация пользователя
        authenticateUser(userLoginDto);
        UserDetails user = userService.loadUserByUsername(userLoginDto.getUsername());

        // Генерация токена
        String token = jwtTokenUtils.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping(USER_BY_ID)
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(USER_BY_ID)
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User successfully deleted"));
    }

    @PatchMapping(USER_BY_ID)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        UserDto user = userService.updateUser(id, updates);
        return ResponseEntity.ok(user);
    }

    // Для тестирования админских ресурсов
    @GetMapping("/admin")
    public ResponseEntity<Map<String, String>> admin() {
        return ResponseEntity.ok(Map.of("message", "Welcome to the Admin Page"));
    }

    // ========================
    // Вспомогательные методы
    // ========================

    private void authenticateUser(UserLoginDto userLoginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword())
        );
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}