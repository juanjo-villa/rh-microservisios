package com.rh_systems.payroll_service.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
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
import com.rh_systems.payroll_service.Controller.PayrollController;
import com.rh_systems.payroll_service.dto.PayrollDTO;
import com.rh_systems.payroll_service.dto.PayrollDTOGetPostPut;
import com.rh_systems.payroll_service.service.PayrollService;

@ExtendWith(MockitoExtension.class)
public class PayrollControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PayrollService payrollService;

    @InjectMocks
    private PayrollController payrollController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(payrollController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testCreatePayroll_Success() throws Exception {
        // Arrange
        PayrollDTO payrollDTO = new PayrollDTO();
        payrollDTO.setStatus("PENDING");
        payrollDTO.setPaymentDate(new Date());
        payrollDTO.setAmount(1000.0f);
        payrollDTO.setEmployeeId(1L);

        PayrollDTOGetPostPut savedPayroll = new PayrollDTOGetPostPut();
        savedPayroll.setId(1L);
        savedPayroll.setStatus("PENDING");
        savedPayroll.setPaymentDate(new Date());
        savedPayroll.setAmount(1000.0f);
        savedPayroll.setEmployeeId(1L);

        when(payrollService.createPayroll(any(PayrollDTO.class))).thenReturn(Optional.of(savedPayroll));

        // Act & Assert
        mockMvc.perform(post("/api/payrolls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payrollDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreatePayroll_BadRequest() throws Exception {
        // Arrange
        PayrollDTO payrollDTO = new PayrollDTO();
        payrollDTO.setStatus("PENDING");
        payrollDTO.setPaymentDate(new Date());
        payrollDTO.setAmount(1000.0f);
        payrollDTO.setEmployeeId(1L);

        when(payrollService.createPayroll(any(PayrollDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/payrolls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payrollDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPayrollById_Success() throws Exception {
        // Arrange
        Long payrollId = 1L;
        PayrollDTOGetPostPut payroll = new PayrollDTOGetPostPut();
        payroll.setId(payrollId);
        payroll.setStatus("PENDING");
        payroll.setPaymentDate(new Date());
        payroll.setAmount(1000.0f);
        payroll.setEmployeeId(1L);

        when(payrollService.getPayrollById(payrollId)).thenReturn(Optional.of(payroll));

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/{id}", payrollId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetPayrollById_NotFound() throws Exception {
        // Arrange
        Long payrollId = 1L;
        when(payrollService.getPayrollById(payrollId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/{id}", payrollId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPayrollPdf_Success() throws Exception {
        // Arrange
        Long payrollId = 1L;
        byte[] pdfContent = "PDF Content".getBytes();

        when(payrollService.generatePayrollPdf(payrollId)).thenReturn(Optional.of(pdfContent));

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/{id}/pdf", payrollId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void testGetPayrollPdf_NotFound() throws Exception {
        // Arrange
        Long payrollId = 1L;

        when(payrollService.generatePayrollPdf(payrollId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/payrolls/{id}/pdf", payrollId))
                .andExpect(status().isNotFound());
    }
}
