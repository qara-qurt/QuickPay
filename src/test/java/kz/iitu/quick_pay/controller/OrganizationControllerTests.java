package kz.iitu.quick_pay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.iitu.quick_pay.dto.OrganizationDto;
import kz.iitu.quick_pay.exception.organization.OrganizationAlreadyExistException;
import kz.iitu.quick_pay.exception.organization.OrganizationNotFoundException;
import kz.iitu.quick_pay.service.organization.OrganizationService;
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

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationControllerTests {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(organizationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrganization_Success() throws Exception {
        Long mockOrganizationId = 1L;
        Mockito.when(organizationService.createOrganization(any(OrganizationDto.class))).thenReturn(mockOrganizationId);

        String json = objectMapper.writeValueAsString(OrganizationDto.builder()
                .name("Test Organization")
                .bin("123456789012")
                .isActive(true)
                .build());

        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockOrganizationId));
    }

    @Test
    void testGetOrganization_Success() throws Exception {
        OrganizationDto mockOrganization = OrganizationDto.builder()
                .id(1L)
                .name("Test Organization")
                .bin("123456789012")
                .isActive(true)
                .build();

        Mockito.when(organizationService.getOrganization(anyLong())).thenReturn(mockOrganization);

        mockMvc.perform(get("/api/organizations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Organization"))
                .andExpect(jsonPath("$.bin").value("123456789012"))
                .andExpect(jsonPath("$.is_active").value(true));
    }

    @Test
    void testGetOrganization_NotFound() throws Exception {
        Mockito.when(organizationService.getOrganization(anyLong())).thenThrow(new OrganizationNotFoundException("Organization not found"));

        mockMvc.perform(get("/api/organizations/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.messages[0]").value("Organization not found"));
    }

    @Test
    void testUpdateOrganization_Success() throws Exception {
        OrganizationDto updatedOrganization = OrganizationDto.builder()
                .id(1L)
                .name("Updated Organization")
                .bin("987654321098")
                .isActive(false)
                .build();

        Mockito.when(organizationService.updateOrganization(anyLong(), any(Map.class))).thenReturn(updatedOrganization);

        String json = objectMapper.writeValueAsString(Map.of("name", "Updated Organization"));

        mockMvc.perform(patch("/api/organizations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Organization"))
                .andExpect(jsonPath("$.bin").value("987654321098"))
                .andExpect(jsonPath("$.is_active").value(false));
    }

    @Test
    void testUpdateOrganization_NotFound() throws Exception {
        Mockito.when(organizationService.updateOrganization(anyLong(), any(Map.class))).thenThrow(new OrganizationNotFoundException("Organization not found"));

        String json = objectMapper.writeValueAsString(Map.of("name", "Updated Organization"));

        mockMvc.perform(patch("/api/organizations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.messages[0]").value("Organization not found"));
    }

    @Test
    void testDeleteOrganization_Success() throws Exception {
        mockMvc.perform(delete("/api/organizations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Organization deleted"));
    }

    @Test
    void testDeleteOrganization_NotFound() throws Exception {
        doThrow(new OrganizationNotFoundException("Organization not found")).when(organizationService).deleteOrganization(anyLong());

        mockMvc.perform(delete("/api/organizations/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.messages[0]").value("Organization not found"));
    }

    @Test
    void testCreateOrganization_AlreadyExists() throws Exception {
        Mockito.when(organizationService.createOrganization(any(OrganizationDto.class)))
                .thenThrow(new OrganizationAlreadyExistException("Organization already exists"));

        String json = objectMapper.writeValueAsString(OrganizationDto.builder()
                .name("Existing Organization")
                .bin("123456789012")
                .isActive(true)
                .build());

        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.messages[0]").value("Organization already exists"));
    }

    @Test
    void testCreateOrganization_ValidationError() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of("name", ""));

        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }
}
