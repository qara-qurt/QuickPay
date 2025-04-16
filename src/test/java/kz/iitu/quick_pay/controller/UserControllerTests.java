package kz.iitu.quick_pay.controller;

import kz.iitu.quick_pay.dto.UserDto;
import kz.iitu.quick_pay.exception.user.UserAlreadyExistsException;
import kz.iitu.quick_pay.exception.user.UserNotFoundException;
import kz.iitu.quick_pay.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser_Success() throws Exception {
        Long mockUserId = 1L;
        Mockito.when(userService.createUser(any(UserDto.class))).thenReturn(mockUserId);

        String json = objectMapper.writeValueAsString(UserDto.builder()
                .name("Test")
                .surname("User")
                .username("testuser")
                .email("testuser@example.com")
                .password("password123")
                .build());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUserId));
    }

    @Test
    void testGetUser_Success() throws Exception {
        UserDto mockUser = UserDto.builder()
                .name("Test")
                .surname("User")
                .username("testuser")
                .email("testuser@example.com")
                .password("password123")
                .build();

        Mockito.when(userService.getUserById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.surname").value("User"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testCreateUser_AlreadyExists() throws Exception {
        Mockito.when(userService.createUser(any(UserDto.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        String json = objectMapper.writeValueAsString(UserDto.builder()
                .name("Existing User")
                .surname("Existing")
                .username("existinguser")
                .email("existing@example.com")
                .password("password123")
                .build());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.messages[0]").value("User already exists"));
    }

    @Test
    void testGetUser_NotFound() throws Exception {
        Mockito.when(userService.getUserById(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.messages[0]").value("User not found"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully deleted"));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.messages[0]").value("User not found"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        UserDto updatedUser = UserDto.builder()
                .name("Updated")
                .surname("User")
                .username("updateduser")
                .email("updated@example.com")
                .password("newpassword")
                .build();
        Mockito.when(userService.updateUser(anyLong(), any(Map.class))).thenReturn(updatedUser);

        String json = objectMapper.writeValueAsString(Map.of("name", "Updated"));

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        Mockito.when(userService.updateUser(anyLong(), any(Map.class))).thenThrow(new UserNotFoundException("User not found"));

        String json = objectMapper.writeValueAsString(Map.of("name", "Updated"));

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.messages[0]").value("User not found"));
    }

    @Test
    void testCreateUser_ValidationError() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of("name", ""));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }
}
