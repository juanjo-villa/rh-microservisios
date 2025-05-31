package com.rh_systems.employee_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh_systems.employee_service.controller.StatusController;
import com.rh_systems.employee_service.dto.StatusDTO;
import com.rh_systems.employee_service.dto.StatusDTOGetPostPut;
import com.rh_systems.employee_service.service.StatusService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StatusControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private StatusService statusService;

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statusController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For Date serialization
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindAll() throws Exception {
        // Arrange
        List<StatusDTOGetPostPut> statusList = new ArrayList<>();
        StatusDTOGetPostPut status1 = new StatusDTOGetPostPut();
        status1.setId(1L);
        status1.setType("VACATION");
        status1.setStartDate(new Date());
        status1.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        status1.setPaid(1.0f);
        status1.setDescription("Annual vacation");
        status1.setEmployeeId(1L);
        status1.setStatusPermissionId(1L);
        statusList.add(status1);

        when(statusService.findAll()).thenReturn(statusList);

        // Act & Assert
        mockMvc.perform(get("/api/status")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statusList)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById_Success() throws Exception {
        // Arrange
        Long id = 1L;
        StatusDTOGetPostPut status = new StatusDTOGetPostPut();
        status.setId(id);
        status.setType("VACATION");
        status.setStartDate(new Date());
        status.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        status.setPaid(1.0f);
        status.setDescription("Annual vacation");
        status.setEmployeeId(1L);
        status.setStatusPermissionId(1L);

        when(statusService.findById(id)).thenReturn(Optional.of(status));

        // Act & Assert
        mockMvc.perform(get("/api/status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(status)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById_NotFound() throws Exception {
        // Arrange
        Long id = 999L;
        when(statusService.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSave_Success() throws Exception {
        // Arrange
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setType("VACATION");
        statusDTO.setStartDate(new Date());
        statusDTO.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        statusDTO.setPaid(1.0f);
        statusDTO.setDescription("Annual vacation");
        statusDTO.setEmployeeId(1L);
        statusDTO.setStatusPermissionId(1L);

        StatusDTOGetPostPut savedStatus = new StatusDTOGetPostPut();
        savedStatus.setId(1L);
        savedStatus.setType(statusDTO.getType());
        savedStatus.setStartDate(statusDTO.getStartDate());
        savedStatus.setEndDate(statusDTO.getEndDate());
        savedStatus.setPaid(statusDTO.getPaid());
        savedStatus.setDescription(statusDTO.getDescription());
        savedStatus.setEmployeeId(statusDTO.getEmployeeId());
        savedStatus.setStatusPermissionId(statusDTO.getStatusPermissionId());

        when(statusService.save(any(StatusDTO.class))).thenReturn(Optional.of(savedStatus));

        // Act & Assert
        mockMvc.perform(post("/api/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedStatus)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSave_BadRequest() throws Exception {
        // Arrange
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setType("VACATION");
        statusDTO.setStartDate(new Date());
        statusDTO.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        statusDTO.setPaid(1.0f);
        statusDTO.setDescription("Annual vacation");
        statusDTO.setEmployeeId(1L);
        statusDTO.setStatusPermissionId(1L);

        when(statusService.save(any(StatusDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate_Success() throws Exception {
        // Arrange
        Long id = 1L;
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setType("SICK_LEAVE");
        statusDTO.setStartDate(new Date());
        statusDTO.setEndDate(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000)); // 3 days later
        statusDTO.setPaid(1.0f);
        statusDTO.setDescription("Sick leave");
        statusDTO.setEmployeeId(1L);
        statusDTO.setStatusPermissionId(2L);

        StatusDTOGetPostPut updatedStatus = new StatusDTOGetPostPut();
        updatedStatus.setId(id);
        updatedStatus.setType(statusDTO.getType());
        updatedStatus.setStartDate(statusDTO.getStartDate());
        updatedStatus.setEndDate(statusDTO.getEndDate());
        updatedStatus.setPaid(statusDTO.getPaid());
        updatedStatus.setDescription(statusDTO.getDescription());
        updatedStatus.setEmployeeId(statusDTO.getEmployeeId());
        updatedStatus.setStatusPermissionId(statusDTO.getStatusPermissionId());

        when(statusService.update(anyLong(), any(StatusDTO.class))).thenReturn(Optional.of(updatedStatus));

        // Act & Assert
        mockMvc.perform(put("/api/status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedStatus)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate_NotFound() throws Exception {
        // Arrange
        Long id = 999L;
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setType("SICK_LEAVE");
        statusDTO.setStartDate(new Date());
        statusDTO.setEndDate(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000)); // 3 days later
        statusDTO.setPaid(1.0f);
        statusDTO.setDescription("Sick leave");
        statusDTO.setEmployeeId(1L);
        statusDTO.setStatusPermissionId(2L);

        when(statusService.update(anyLong(), any(StatusDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteById_Success() throws Exception {
        // Arrange
        Long id = 1L;
        when(statusService.deleteById(id)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteById_NotFound() throws Exception {
        // Arrange
        Long id = 999L;
        when(statusService.deleteById(id)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/status/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}