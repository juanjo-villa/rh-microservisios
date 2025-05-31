package com.rh_systems.payroll_service.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rh_systems.payroll_service.Controller.PayrollAdjustmentsController;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTO;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTOGetPostPut;
import com.rh_systems.payroll_service.service.PayrollAdjustmentsService;

@ExtendWith(MockitoExtension.class)
public class PayrollAdjustmentsControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PayrollAdjustmentsService payrollAdjustmentsService;

    @InjectMocks
    private PayrollAdjustmentsController payrollAdjustmentsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(payrollAdjustmentsController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testGetAllPayrollAdjustments_Success() throws Exception {
        // Arrange
        List<PayrollAdjustmentsDTOGetPostPut> adjustments = new ArrayList<>();
        PayrollAdjustmentsDTOGetPostPut adjustment = new PayrollAdjustmentsDTOGetPostPut();
        adjustment.setId(1L);
        adjustment.setType("BONUS");
        adjustment.setDescription("Annual bonus");
        adjustment.setAmount(500.0f);
        adjustment.setPayrollId(1L);
        adjustments.add(adjustment);

        when(payrollAdjustmentsService.getAllPayrollAdjustments()).thenReturn(adjustments);

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/adjustments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetPayrollAdjustmentById_Success() throws Exception {
        // Arrange
        Long adjustmentId = 1L;
        PayrollAdjustmentsDTOGetPostPut adjustment = new PayrollAdjustmentsDTOGetPostPut();
        adjustment.setId(adjustmentId);
        adjustment.setType("BONUS");
        adjustment.setDescription("Annual bonus");
        adjustment.setAmount(500.0f);
        adjustment.setPayrollId(1L);

        when(payrollAdjustmentsService.getPayrollAdjustmentsById(adjustmentId)).thenReturn(Optional.of(adjustment));

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/adjustments/{id}", adjustmentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetPayrollAdjustmentById_NotFound() throws Exception {
        // Arrange
        Long adjustmentId = 1L;
        when(payrollAdjustmentsService.getPayrollAdjustmentsById(adjustmentId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/adjustments/{id}", adjustmentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPayrollAdjustmentByType_Success() throws Exception {
        // Arrange
        String typeStr = "BONUS";
        AdjustmentType type = AdjustmentType.BONUS;
        PayrollAdjustmentsDTOGetPostPut adjustment = new PayrollAdjustmentsDTOGetPostPut();
        adjustment.setId(1L);
        adjustment.setType(type);
        adjustment.setDescription("Annual bonus");
        adjustment.setAmount(500.0f);
        adjustment.setPayrollId(1L);

        when(payrollAdjustmentsService.getPayrollAdjustmentsByType(type)).thenReturn(Optional.of(adjustment));

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/adjustments/type/{type}", typeStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetPayrollAdjustmentByType_NotFound() throws Exception {
        // Arrange
        String typeStr = "BONUS";
        AdjustmentType type = AdjustmentType.BONUS;
        when(payrollAdjustmentsService.getPayrollAdjustmentsByType(type)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/adjustments/type/{type}", typeStr))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePayrollAdjustment_Success() throws Exception {
        // Arrange
        Long payrollId = 1L;
        PayrollAdjustmentsDTO adjustmentDTO = new PayrollAdjustmentsDTO();
        adjustmentDTO.setType("BONUS");
        adjustmentDTO.setDescription("Annual bonus");
        adjustmentDTO.setAmount(500.0f);
        adjustmentDTO.setPayrollId(payrollId);

        PayrollAdjustmentsDTOGetPostPut savedAdjustment = new PayrollAdjustmentsDTOGetPostPut();
        savedAdjustment.setId(1L);
        savedAdjustment.setType("BONUS");
        savedAdjustment.setDescription("Annual bonus");
        savedAdjustment.setAmount(500.0f);
        savedAdjustment.setPayrollId(payrollId);

        when(payrollAdjustmentsService.createPayrollAdjustments(any(PayrollAdjustmentsDTO.class)))
                .thenReturn(Optional.of(savedAdjustment));

        // Act & Assert
        mockMvc.perform(post("/api/payrolls/{payrollId}/adjustments", payrollId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adjustmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreatePayrollAdjustment_BadRequest() throws Exception {
        // Arrange
        Long payrollId = 1L;
        PayrollAdjustmentsDTO adjustmentDTO = new PayrollAdjustmentsDTO();
        adjustmentDTO.setType("BONUS");
        adjustmentDTO.setDescription("Annual bonus");
        adjustmentDTO.setAmount(500.0f);
        adjustmentDTO.setPayrollId(payrollId);

        when(payrollAdjustmentsService.createPayrollAdjustments(any(PayrollAdjustmentsDTO.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/payrolls/{payrollId}/adjustments", payrollId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adjustmentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePayrollAdjustment_Success() throws Exception {
        // Arrange
        Long payrollId = 1L;
        Long adjustmentId = 1L;
        PayrollAdjustmentsDTO adjustmentDTO = new PayrollAdjustmentsDTO();
        adjustmentDTO.setType("BONUS");
        adjustmentDTO.setDescription("Updated annual bonus");
        adjustmentDTO.setAmount(600.0f);
        adjustmentDTO.setPayrollId(payrollId);

        PayrollAdjustmentsDTOGetPostPut updatedAdjustment = new PayrollAdjustmentsDTOGetPostPut();
        updatedAdjustment.setId(adjustmentId);
        updatedAdjustment.setType("BONUS");
        updatedAdjustment.setDescription("Updated annual bonus");
        updatedAdjustment.setAmount(600.0f);
        updatedAdjustment.setPayrollId(payrollId);

        when(payrollAdjustmentsService.updatePayrollAdjustment(anyLong(), any(PayrollAdjustmentsDTO.class)))
                .thenReturn(Optional.of(updatedAdjustment));

        // Act & Assert
        mockMvc.perform(put("/api/payrolls/{payrollId}/adjustments/{adjustmentId}", payrollId, adjustmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adjustmentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdatePayrollAdjustment_NotFound() throws Exception {
        // Arrange
        Long payrollId = 1L;
        Long adjustmentId = 1L;
        PayrollAdjustmentsDTO adjustmentDTO = new PayrollAdjustmentsDTO();
        adjustmentDTO.setType("BONUS");
        adjustmentDTO.setDescription("Updated annual bonus");
        adjustmentDTO.setAmount(600.0f);
        adjustmentDTO.setPayrollId(payrollId);

        when(payrollAdjustmentsService.updatePayrollAdjustment(anyLong(), any(PayrollAdjustmentsDTO.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/payrolls/{payrollId}/adjustments/{adjustmentId}", payrollId, adjustmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adjustmentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePayrollAdjustment_Success() throws Exception {
        // Arrange
        Long payrollId = 1L;
        Long adjustmentId = 1L;
        when(payrollAdjustmentsService.deletePayrollAdjustment(adjustmentId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/payrolls/{payrollId}/adjustments/{adjustmentId}", payrollId, adjustmentId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePayrollAdjustment_NotFound() throws Exception {
        // Arrange
        Long payrollId = 1L;
        Long adjustmentId = 1L;
        when(payrollAdjustmentsService.deletePayrollAdjustment(adjustmentId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/payrolls/{payrollId}/adjustments/{adjustmentId}", payrollId, adjustmentId))
                .andExpect(status().isNotFound());
    }
}
