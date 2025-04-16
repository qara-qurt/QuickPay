package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.dto.UserLoginDto;
import kz.iitu.quick_pay.service.user.UserService;
import kz.iitu.quick_pay.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
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
    public static final String ME = "/me";

    UserService userService;
    JwtTokenUtils jwtTokenUtils;
    AuthenticationManager authenticationManager;

    @PostMapping(REGISTER)
    public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody UserDto userDto) {
        Long userId = userService.createUser(userDto);
        return ResponseEntity.ok(Map.of("id", userId));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        // Аутентификация пользователя
        authenticateUser(userLoginDto);
        UserDetails user = userService.loadUserByUsername(userLoginDto.getUsername());
        // Генерация токена
        String token = jwtTokenUtils.generateToken(user);

       UserDto userDto = userService.getByUsername(userLoginDto.getUsername());

        return ResponseEntity.ok(Map.of(
                "user", userDto,
                "token", token

        ));
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

    @GetMapping(ME)
    public ResponseEntity<UserDto>  me() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDto userDto = userService.getByUsername(user.getUsername());
        return ResponseEntity.ok( userDto);
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort", defaultValue = "updatedAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "organization_id", required = false) Long organization_id
    ) {

        Page<UserDto> data = userService.getUsers(page, limit, sort, order, search, organization_id);
        return ResponseEntity.ok(Map.of("data", data));
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