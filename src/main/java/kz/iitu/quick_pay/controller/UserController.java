package kz.iitu.quick_pay.controller;

import jakarta.validation.Valid;
import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.enitity.UserEntity;
import kz.iitu.quick_pay.service.UserServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserServiceImpl userService;

    // REST API - routes
    public static final String CREATE_USER = "api/users";
    public static final String GET_USERS = "api/users";

    @PostMapping(CREATE_USER)
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        Long userId = userService.createUser(userDto);
        return ResponseEntity.ok(Map.of("id", userId));
    }
}
