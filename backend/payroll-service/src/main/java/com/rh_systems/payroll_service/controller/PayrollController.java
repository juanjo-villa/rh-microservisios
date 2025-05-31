package com.rh_systems.payroll_service.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh_systems.payroll_service.dto.PayrollDTO;
import com.rh_systems.payroll_service.dto.PayrollDTOGetPostPut;
import com.rh_systems.payroll_service.service.PayrollService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired 
    private PayrollService payrollService;

    /**
     * Gets all payroll records.
     * Note: This endpoint should be accessible only to administrators.
     * @return a list of PayrollDTOGetPostPut
     */
    @GetMapping
    public List<PayrollDTOGetPostPut> getAllPayroll() {
        return payrollService.getAllPayroll();
    }

    /**
     * Gets a payroll record by its ID.
     * @param id the payroll ID
     * @return a ResponseEntity with the PayrollDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<PayrollDTOGetPostPut> getPayrollById(@PathVariable Long id) {
        Optional<PayrollDTOGetPostPut> payrollDTO = payrollService.getPayrollById(id);
        return payrollDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets a payroll record by its status.
     * @param status the payroll status
     * @return a ResponseEntity with the PayrollDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("status/{status}")
    public ResponseEntity<PayrollDTOGetPostPut> getPayrollByStatus(@PathVariable String status) {
        Optional<PayrollDTOGetPostPut> payrollDTO = payrollService.getPayrollByStatus(status);
        return payrollDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new payroll record.
     * Note: This endpoint should be accessible only to administrators.
     * @param payrollDTO the payroll data
     * @return a ResponseEntity with the created PayrollDTOGetPostPut, or 400 if a payroll with the same status exists
     */
    @PostMapping
    public ResponseEntity<PayrollDTOGetPostPut> createPayroll(@Valid @RequestBody PayrollDTO payrollDTO) {
        Optional<PayrollDTOGetPostPut> savedPayroll = payrollService.createPayroll(payrollDTO);
        return savedPayroll.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Creates a new payroll record with employee data validation.
     * Note: This endpoint should be accessible only to administrators.
     * @param payrollDTO the payroll data
     * @return a ResponseEntity with the created PayrollDTOGetPostPut, or 400 if validation fails
     */
    @PostMapping("/validate")
    public ResponseEntity<PayrollDTOGetPostPut> createPayrollWithEmployeeValidation(@Valid @RequestBody PayrollDTO payrollDTO) {
        Optional<PayrollDTOGetPostPut> savedPayroll = payrollService.createPayrollWithEmployeeValidation(payrollDTO);
        return savedPayroll.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Updates an existing payroll record.
     * Note: This endpoint should be accessible only to administrators.
     * @param id the payroll ID
     * @param payrollDTO the payroll data to update
     * @return a ResponseEntity with the updated PayrollDTOGetPostPut, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<PayrollDTOGetPostPut> updatePayroll(@PathVariable long id, @Valid @RequestBody PayrollDTO payrollDTO) {
        Optional<PayrollDTOGetPostPut> updatedPayroll = payrollService.updatePayroll(id, payrollDTO);
        return updatedPayroll.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a payroll record by its ID.
     * Note: This endpoint should be accessible only to administrators.
     * @param id the payroll ID
     * @return a ResponseEntity with status 200 if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
        if (payrollService.deletePayroll(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generates a PDF for a payroll record.
     * @param id the payroll ID
     * @return a ResponseEntity with the PDF content if found, or 404 otherwise
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPayrollPdf(@PathVariable Long id) {
        Optional<byte[]> pdfContent = payrollService.generatePayrollPdf(id);
        if (pdfContent.isPresent()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
