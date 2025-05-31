package com.rh_systems.payroll_service.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;

import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTO;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTOGetPostPut;
import com.rh_systems.payroll_service.service.PayrollAdjustmentsService;

import jakarta.validation.Valid;

/**
 * REST controller for managing payroll adjustments.
 */
@RestController
@RequestMapping("/api/payrolls")
@CrossOrigin(origins = "*")
public class PayrollAdjustmentsController {
    @Autowired
    private PayrollAdjustmentsService payrollAdjustmentsService;

    /**
     * Gets all payroll adjustments.
     * Note: This endpoint should be accessible only to administrators.
     * @return a list of PayrollAdjustmentsDTOGetPostPut
     */
    @GetMapping("/adjustments")
    public List<PayrollAdjustmentsDTOGetPostPut> getAllPayrollAdjustments() {
        return payrollAdjustmentsService.getAllPayrollAdjustments();
    }

    /**
     * Gets a payroll adjustment by its ID.
     * @param id the payroll adjustment ID
     * @return a ResponseEntity with the PayrollAdjustmentsDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/adjustments/{id}")
    public ResponseEntity<PayrollAdjustmentsDTOGetPostPut> getPayrollAdjustmentById(@PathVariable Long id) {
        Optional<PayrollAdjustmentsDTOGetPostPut> payrollAdjustmentDTO = payrollAdjustmentsService.getPayrollAdjustmentsById(id);
        return payrollAdjustmentDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets a payroll adjustment by its type.
     * @param type the payroll adjustment type (BONUS or DISCOUNT)
     * @return a ResponseEntity with the PayrollAdjustmentsDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/adjustments/type/{type}")
    public ResponseEntity<PayrollAdjustmentsDTOGetPostPut> getPayrollAdjustmentByType(@PathVariable String type) {
        try {
            AdjustmentType adjustmentType = AdjustmentType.valueOf(type.toUpperCase());
            Optional<PayrollAdjustmentsDTOGetPostPut> payrollAdjustmentDTO = payrollAdjustmentsService.getPayrollAdjustmentsByType(adjustmentType);
            return payrollAdjustmentDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Creates a new payroll adjustment for a specific payroll.
     * Note: This endpoint should be accessible only to administrators.
     * @param payrollId the payroll ID
     * @param payrollAdjustmentDTO the payroll adjustment data
     * @return a ResponseEntity with the created PayrollAdjustmentsDTOGetPostPut, or 400 if a payroll adjustment with the same type exists
     */
    @PostMapping("/{payrollId}/adjustments")
    public ResponseEntity<PayrollAdjustmentsDTOGetPostPut> createPayrollAdjustment(
            @PathVariable Long payrollId,
            @Valid @RequestBody PayrollAdjustmentsDTO payrollAdjustmentDTO) {
        // Set the payroll ID in the DTO if needed
        // payrollAdjustmentDTO.setPayrollId(payrollId);
        Optional<PayrollAdjustmentsDTOGetPostPut> savedPayrollAdjustment = payrollAdjustmentsService.createPayrollAdjustments(payrollAdjustmentDTO);
        return savedPayrollAdjustment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Updates an existing payroll adjustment.
     * Note: This endpoint should be accessible only to administrators.
     * @param payrollId the payroll ID
     * @param adjustmentId the adjustment ID
     * @param payrollAdjustmentsDTO the payroll adjustment data to update
     * @return a ResponseEntity with the updated PayrollAdjustmentsDTOGetPostPut, or 404 if not found
     */
    @PutMapping("/{payrollId}/adjustments/{adjustmentId}")
    public ResponseEntity<PayrollAdjustmentsDTOGetPostPut> updatePayrollAdjustment(
            @PathVariable Long payrollId,
            @PathVariable Long adjustmentId,
            @Valid @RequestBody PayrollAdjustmentsDTO payrollAdjustmentsDTO) {
        Optional<PayrollAdjustmentsDTOGetPostPut> updatedPayrollAdjustment = payrollAdjustmentsService.updatePayrollAdjustment(adjustmentId, payrollAdjustmentsDTO);
        return updatedPayrollAdjustment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a payroll adjustment by its ID.
     * Note: This endpoint should be accessible only to administrators.
     * @param payrollId the payroll ID
     * @param adjustmentId the adjustment ID
     * @return a ResponseEntity with status 200 if deleted, or 404 if not found
     */
    @DeleteMapping("/{payrollId}/adjustments/{adjustmentId}")
    public ResponseEntity<Void> deletePayrollAdjustment(
            @PathVariable Long payrollId,
            @PathVariable Long adjustmentId) {
        if (payrollAdjustmentsService.deletePayrollAdjustment(adjustmentId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
