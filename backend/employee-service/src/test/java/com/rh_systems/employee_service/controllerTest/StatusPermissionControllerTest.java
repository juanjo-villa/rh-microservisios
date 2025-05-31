package com.rh_systems.employee_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh_systems.employee_service.controller.StatusPermissionController;
import com.rh_systems.employee_service.dto.StatusPermissionDTOGetPostPut;
import com.rh_systems.employee_service.service.StatusPermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StatusPermissionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private StatusPermissionService statusPermissionService;

    @InjectMocks
    private StatusPermissionController statusPermissionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statusPermissionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindAll() throws Exception {
        // Arrange
        List<StatusPermissionDTOGetPostPut> permissionList = new ArrayList<>();
        StatusPermissionDTOGetPostPut permission1 = new StatusPermissionDTOGetPostPut();
        permission1.setId(1L);
        permission1.setName("VACATION");
        permission1.setDescription("Vacation permission");
        permissionList.add(permission1);

        when(statusPermissionService.findAll()).thenReturn(permissionList);

        // Act & Assert
        mockMvc.perform(get("/api/status-permissions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(permissionList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById_Success() throws Exception {
        // Arrange
        Long id = 1L;
        StatusPermissionDTOGetPostPut permission = new StatusPermissionDTOGetPostPut();
        permission.setId(id);
        permission.setName("VACATION");
        permission.setDescription("Vacation permission");

        when(statusPermissionService.findById(id)).thenReturn(Optional.of(permission));

        // Act & Assert
        mockMvc.perform(get("/api/status-permissions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(permission)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById_NotFound() throws Exception {
        // Arrange
        Long id = 999L;
        when(statusPermissionService.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/status-permissions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSave_Success() throws Exception {
        // Arrange
        StatusPermissionDTOGetPostPut permissionDTO = new StatusPermissionDTOGetPostPut();
        permissionDTO.setName("VACATION");
        permissionDTO.setDescription("Vacation permission");

        StatusPermissionDTOGetPostPut savedPermission = new StatusPermissionDTOGetPostPut();
        savedPermission.setId(1L);
        savedPermission.setName(permissionDTO.getName());
        savedPermission.setDescription(permissionDTO.getDescription());

        when(statusPermissionService.save(any(StatusPermissionDTOGetPostPut.class))).thenReturn(Optional.of(savedPermission));

        // Act & Assert
        mockMvc.perform(post("/api/status-permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permissionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedPermission)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSave_BadRequest() throws Exception {
        // Arrange
        StatusPermissionDTOGetPostPut permissionDTO = new StatusPermissionDTOGetPostPut();
        permissionDTO.setName("VACATION");
        permissionDTO.setDescription("Vacation permission");

        when(statusPermissionService.save(any(StatusPermissionDTOGetPostPut.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/status-permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permissionDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate_Success() throws Exception {
        // Arrange
        Long id = 1L;
        StatusPermissionDTOGetPostPut permissionDTO = new StatusPermissionDTOGetPostPut();
        permissionDTO.setName("SICK_LEAVE");
        permissionDTO.setDescription("Sick leave permission");

        StatusPermissionDTOGetPostPut updatedPermission = new StatusPermissionDTOGetPostPut();
        updatedPermission.setId(id);
        updatedPermission.setName(permissionDTO.getName());
        updatedPermission.setDescription(permissionDTO.getDescription());

        when(statusPermissionService.update(anyLong(), any(StatusPermissionDTOGetPostPut.class))).thenReturn(Optional.of(updatedPermission));

        // Act & Assert
        mockMvc.perform(put("/api/status-permissions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permissionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedPermission)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate_NotFound() throws Exception {
        // Arrange
        Long id = 999L;
        StatusPermissionDTOGetPostPut permissionDTO = new StatusPermissionDTOGetPostPut();
        permissionDTO.setName("SICK_LEAVE");
        permissionDTO.setDescription("Sick leave permission");

        when(statusPermissionService.update(anyLong(), any(StatusPermissionDTOGetPostPut.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/status-permissions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permissionDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteById_Success() throws Exception {
        // Arrange
        Long id = 1L;
        when(statusPermissionService.deleteById(id)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/status-permissions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteById_NotFound() throws Exception {
        // Arrange
        Long id = 999L;
        when(statusPermissionService.deleteById(id)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/status-permissions/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}