package com.rh_systems.payroll_service.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rh_systems.payroll_service.Entity.Payroll;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTO;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTOGetPostPut;
import com.rh_systems.payroll_service.repository.PayrollAdjustmentsRepository;
import com.rh_systems.payroll_service.repository.PayrollRepository;
import com.rh_systems.payroll_service.service.PayrollAdjustmentsService;

@ExtendWith(MockitoExtension.class)
public class PayrollAdjustmentsServiceTest {

    @Mock
    private PayrollAdjustmentsRepository payrollAdjustmentsRepository;

    @Mock
    private PayrollRepository payrollRepository;

    @InjectMocks
    private PayrollAdjustmentsService payrollAdjustmentsService;

    private PayrollAdjustmentsDTO adjustmentDTO;
    private PayrollAdjustments adjustment;
    private Payroll payroll;

    @BeforeEach
    void setUp() {
        // Setup common test data
        payroll = new Payroll();
        payroll.setId(1L);
        payroll.setStatus("PENDING");

        adjustmentDTO = new PayrollAdjustmentsDTO();
        adjustmentDTO.setType("BONUS");
        adjustmentDTO.setDescription("Annual bonus");
        adjustmentDTO.setAmount(500.0f);
        adjustmentDTO.setPayrollId(1L);

        adjustment = new PayrollAdjustments();
        adjustment.setId(1L);
        adjustment.setType(AdjustmentType.BONUS);
        adjustment.setDescription("Annual bonus");
        adjustment.setAmount(500.0f);
        adjustment.setPayroll(payroll);
    }

    @Test
    void testGetAllPayrollAdjustments() {
        // Arrange
        List<PayrollAdjustments> adjustments = new ArrayList<>();
        adjustments.add(adjustment);
        when(payrollAdjustmentsRepository.findAll()).thenReturn(adjustments);

        // Act
        List<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.getAllPayrollAdjustments();

        // Assert
        assertEquals(1, result.size());
        assertEquals(adjustment.getId(), result.get(0).getId());
        assertEquals(adjustment.getType(), result.get(0).getType());
        assertEquals(adjustment.getDescription(), result.get(0).getDescription());
        assertEquals(adjustment.getAmount(), result.get(0).getAmount());
    }

    @Test
    void testGetPayrollAdjustmentsById_Found() {
        // Arrange
        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.of(adjustment));

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.getPayrollAdjustmentsById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(adjustment.getId(), result.get().getId());
        assertEquals(adjustment.getType(), result.get().getType());
        assertEquals(adjustment.getDescription(), result.get().getDescription());
        assertEquals(adjustment.getAmount(), result.get().getAmount());
    }

    @Test
    void testGetPayrollAdjustmentsById_NotFound() {
        // Arrange
        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.getPayrollAdjustmentsById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetPayrollAdjustmentsByType_Found() {
        // Arrange
        when(payrollAdjustmentsRepository.findByType(any(AdjustmentType.class))).thenReturn(Optional.of(adjustment));

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.getPayrollAdjustmentsByType("BONUS");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(adjustment.getId(), result.get().getId());
        assertEquals(adjustment.getType(), result.get().getType());
        assertEquals(adjustment.getDescription(), result.get().getDescription());
        assertEquals(adjustment.getAmount(), result.get().getAmount());
    }

    @Test
    void testGetPayrollAdjustmentsByType_NotFound() {
        // Arrange
        when(payrollAdjustmentsRepository.findByType(any(AdjustmentType.class))).thenReturn(Optional.empty());

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.getPayrollAdjustmentsByType("BONUS");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreatePayrollAdjustments_Success() {
        // Arrange
        when(payrollAdjustmentsRepository.findByType(any(AdjustmentType.class))).thenReturn(Optional.empty());
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);
        when(payrollAdjustmentsRepository.save(any(PayrollAdjustments.class))).thenReturn(adjustment);

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.createPayrollAdjustments(adjustmentDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(adjustment.getId(), result.get().getId());
        assertEquals(adjustment.getType(), result.get().getType());
        assertEquals(adjustment.getDescription(), result.get().getDescription());
        assertEquals(adjustment.getAmount(), result.get().getAmount());
    }

    @Test
    void testCreatePayrollAdjustments_TypeConflict() {
        // Arrange
        when(payrollAdjustmentsRepository.findByType(any(AdjustmentType.class))).thenReturn(Optional.of(adjustment));

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.createPayrollAdjustments(adjustmentDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayrollAdjustment_Success() {
        // Arrange
        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.of(adjustment));
        when(payrollRepository.findById(anyLong())).thenReturn(Optional.of(payroll));
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);
        when(payrollAdjustmentsRepository.save(any(PayrollAdjustments.class))).thenReturn(adjustment);

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.updatePayrollAdjustment(1L, adjustmentDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(adjustment.getId(), result.get().getId());
        assertEquals(adjustment.getType(), result.get().getType());
        assertEquals(adjustment.getDescription(), result.get().getDescription());
        assertEquals(adjustment.getAmount(), result.get().getAmount());
    }

    @Test
    void testUpdatePayrollAdjustment_NotFound() {
        // Arrange
        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.updatePayrollAdjustment(1L, adjustmentDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePayrollAdjustment_TypeConflict() {
        // Arrange
        PayrollAdjustments existingAdjustment = new PayrollAdjustments();
        existingAdjustment.setId(1L);
        existingAdjustment.setType(AdjustmentType.DISCOUNT);
        existingAdjustment.setPayroll(payroll);

        PayrollAdjustments conflictAdjustment = new PayrollAdjustments();
        conflictAdjustment.setId(2L);
        conflictAdjustment.setType(AdjustmentType.BONUS);

        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.of(existingAdjustment));
        when(payrollAdjustmentsRepository.findByType(any(AdjustmentType.class))).thenReturn(Optional.of(conflictAdjustment));

        // Act
        Optional<PayrollAdjustmentsDTOGetPostPut> result = payrollAdjustmentsService.updatePayrollAdjustment(1L, adjustmentDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testDeletePayrollAdjustment_Success() {
        // Arrange
        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.of(adjustment));
        when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);

        // Act
        boolean result = payrollAdjustmentsService.deletePayrollAdjustment(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeletePayrollAdjustment_NotFound() {
        // Arrange
        when(payrollAdjustmentsRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = payrollAdjustmentsService.deletePayrollAdjustment(1L);

        // Assert
        assertFalse(result);
    }
}
