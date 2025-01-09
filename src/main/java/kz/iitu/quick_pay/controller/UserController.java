package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    // REST API - routes
    public static final String CREATE_USER = "api/users";
    public static final String GET_USER = "api/users/{id}";
    public static final String DELETE_USER = "api/users/{id}";
    public static final String UPDATE_USER = "api/users/{id}";

    @PostMapping(CREATE_USER)
    public ResponseEntity<Map<String,Long>> createUser(@Valid @RequestBody UserDto userDto) {
        Long userId = userService.createUser(userDto);
        return ResponseEntity.ok(Map.of("id", userId));
    }

    @GetMapping(GET_USER)
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping(DELETE_USER)
    public ResponseEntity<Map<String,String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    @PatchMapping(UPDATE_USER)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody Map<String,Object> updates) {
        UserDto user = userService.updateUser(id, updates);
        return ResponseEntity.ok(user);
    }

}
