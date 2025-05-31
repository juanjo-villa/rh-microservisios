package com.rh_systems.payroll_service.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rh_systems.payroll_service.Entity.Payroll;
import com.rh_systems.payroll_service.client.EmployeeClient;
import com.rh_systems.payroll_service.dto.EmployeeDTO;
import com.rh_systems.payroll_service.dto.PayrollDTO;
import com.rh_systems.payroll_service.dto.PayrollDTOGetPostPut;
import com.rh_systems.payroll_service.dto.PositionDTO;
import com.rh_systems.payroll_service.repository.PayrollRepository;
import com.rh_systems.payroll_service.service.PayrollService;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {

    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private PayrollService payrollService;

    private PayrollDTO payrollDTO;
    private Payroll payroll;
    private EmployeeDTO employeeDTO;
    private PositionDTO positionDTO;

    @BeforeEach
    void setUp() {
        // Setup common test data
        payrollDTO = new PayrollDTO();
        payrollDTO.setStatus("PENDING");
        payrollDTO.setPaymentDate(new Date());
        payrollDTO.setAmount(1000.0f);
        payrollDTO.setEmployeeId(1L);

        payroll = new Payroll();
        payroll.setId(1L);
        payroll.setStatus("PENDING");
        payroll.setPaymentDate(new Date());
        payroll.setAmount(1000.0f);
        payroll.setEmployeeId(1L);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setPosition("Developer");

        positionDTO = new PositionDTO();
        positionDTO.setId(1L);
        positionDTO.setName("Developer");
        positionDTO.setBaseSalary(1000.0f);
    }

    @Test
    void testCreatePayrollWithEmployeeValidation_Success() {
        // Arrange
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(positionDTO);
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.empty());
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayrollWithEmployeeValidation(payrollDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(payroll.getId(), result.get().getId());
        assertEquals(payroll.getStatus(), result.get().getStatus());
        assertEquals(payroll.getAmount(), result.get().getAmount());
    }

    @Test
    void testCreatePayrollWithEmployeeValidation_EmployeeNotFound() {
        // Arrange
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(null);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayrollWithEmployeeValidation(payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePayrollWithEmployeeValidation_PositionNotFound() {
        // Arrange
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(null);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayrollWithEmployeeValidation(payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePayrollWithEmployeeValidation_StatusConflict() {
        // Arrange
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(positionDTO);
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.of(payroll));

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayrollWithEmployeeValidation(payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllPayroll() {
        // Arrange
        List<Payroll> payrolls = new ArrayList<>();
        payrolls.add(payroll);
        when(payrollRepository.findAll()).thenReturn(payrolls);

        // Act
        List<PayrollDTOGetPostPut> result = payrollService.getAllPayroll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(payroll.getId(), result.get(0).getId());
        assertEquals(payroll.getStatus(), result.get(0).getStatus());
    }

    @Test
    void testGetPayrollById_Found() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.getPayrollById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(payroll.getId(), result.get().getId());
        assertEquals(payroll.getStatus(), result.get().getStatus());
    }

    @Test
    void testGetPayrollById_NotFound() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.getPayrollById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetPayrollByStatus_Found() {
        // Arrange
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.of(payroll));

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.getPayrollByStatus("PENDING");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(payroll.getId(), result.get().getId());
        assertEquals(payroll.getStatus(), result.get().getStatus());
    }

    @Test
    void testGetPayrollByStatus_NotFound() {
        // Arrange
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.getPayrollByStatus("PENDING");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePayroll_Success() {
        // Arrange
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(positionDTO);
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.empty());
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayroll(payrollDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(payroll.getId(), result.get().getId());
        assertEquals(payroll.getStatus(), result.get().getStatus());
    }

    @Test
    void testCreatePayroll_StatusConflict() {
        // Arrange
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.of(payroll));

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayroll(payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePayroll_EmployeeNotFound() {
        // Arrange
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.empty());
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(null);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayroll(payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePayroll_PositionNotFound() {
        // Arrange
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.empty());
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(null);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.createPayroll(payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayroll_Success() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(positionDTO);
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.updatePayroll(1L, payrollDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(payroll.getId(), result.get().getId());
        assertEquals(payroll.getStatus(), result.get().getStatus());
    }

    @Test
    void testUpdatePayroll_NotFound() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.updatePayroll(1L, payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayroll_StatusConflict() {
        // Arrange
        Payroll existingPayroll = new Payroll();
        existingPayroll.setId(1L);
        existingPayroll.setStatus("COMPLETED");

        Payroll conflictPayroll = new Payroll();
        conflictPayroll.setId(2L);
        conflictPayroll.setStatus("PENDING");

        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(existingPayroll));
        when(payrollRepository.findByStatus(anyString())).thenReturn(Optional.of(conflictPayroll));

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.updatePayroll(1L, payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayroll_EmployeeNotFound() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(null);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.updatePayroll(1L, payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayroll_PositionNotFound() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));
        when(employeeClient.getEmployeeById(anyLong())).thenReturn(employeeDTO);
        when(employeeClient.getPositionByName(anyString())).thenReturn(null);

        // Act
        Optional<PayrollDTOGetPostPut> result = payrollService.updatePayroll(1L, payrollDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testDeletePayroll_Success() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));

        // Act
        boolean result = payrollService.deletePayroll(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeletePayroll_NotFound() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = payrollService.deletePayroll(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void testGeneratePayrollPdf_Success() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));

        // Act
        Optional<byte[]> result = payrollService.generatePayrollPdf(1L);

        // Assert
        assertTrue(result.isPresent());
        assertTrue(result.get().length > 0);
    }

    @Test
    void testGeneratePayrollPdf_NotFound() {
        // Arrange
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<byte[]> result = payrollService.generatePayrollPdf(1L);

        // Assert
        assertFalse(result.isPresent());
    }
}
