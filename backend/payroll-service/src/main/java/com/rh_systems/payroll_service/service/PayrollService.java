package com.rh_systems.payroll_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rh_systems.payroll_service.client.EmployeeClient;
import com.rh_systems.payroll_service.dto.EmployeeDTO;
import com.rh_systems.payroll_service.dto.PositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh_systems.payroll_service.Entity.Payroll;
import com.rh_systems.payroll_service.dto.PayrollDTO;
import com.rh_systems.payroll_service.dto.PayrollDTOGetPostPut;
import com.rh_systems.payroll_service.repository.PayrollRepository;

@Service
public class PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    @Autowired
    private EmployeeClient employeeClient;

    /**
     * Creates a new payroll record with employee data validation.
     *
     * @param payrollDTO the payroll data
     * @return an Optional containing the created PayrollDTOGetPostPut, or empty if validation fails
     * */
    public Optional<PayrollDTOGetPostPut> createPayrollWithEmployeeValidation(PayrollDTO payrollDTO) {
        EmployeeDTO employee;
        PositionDTO position;
        try {
            employee = employeeClient.getEmployeeById(payrollDTO.getEmployeeId());
            if (employee == null) {
                return Optional.empty();
            }
            position = employeeClient.getPositionByName(employee.getPosition());
            if (position == null) {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee or position data: " + e.getMessage());
        }

        if (payrollRepository.findByStatus(payrollDTO.getStatus()).isPresent()) {
            return Optional.empty();
        }

        Payroll payroll = new Payroll();
        payroll.setStatus(payrollDTO.getStatus());
        payroll.setIssueDate(payrollDTO.getIssueDate());
        payroll.setBaseSalary(position.getBaseSalary());
        payroll.setTotalAdjustments(0); // Will be updated when adjustments are added
        payroll.setNetSalary(position.getBaseSalary()); // Initially, net salary equals base salary
        payroll.setEmployeeId(employee.getId());

        Payroll savePayroll = payrollRepository.save(payroll);

        PayrollDTOGetPostPut payrollDTOGetPostPut = new PayrollDTOGetPostPut();
        payrollDTOGetPostPut.convertToPayrollDTO(savePayroll);
        return Optional.of(payrollDTOGetPostPut);
    }

    /**
     * Gets all payroll records.
     * @return a list of PayrollDTOGetPostPut
     */
    public List<PayrollDTOGetPostPut> getAllPayroll() {
        List<PayrollDTOGetPostPut> payrollToReturn = new ArrayList<>();
        List<Payroll> payroll = payrollRepository.findAll();
        for (Payroll p : payroll) {
            PayrollDTOGetPostPut payrollDTO = new PayrollDTOGetPostPut();
            payrollDTO.convertToPayrollDTO(p);
            payrollToReturn.add(payrollDTO);
        }
        return payrollToReturn;
    }

    /**
     * Gets a payroll record by its ID.
     * @param id the payroll ID
     * @return an Optional containing the PayrollDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PayrollDTOGetPostPut> getPayrollById(Long id) {
        Optional<Payroll> payroll = payrollRepository.findById(id);
        if (payroll.isPresent()) {
            PayrollDTOGetPostPut payrollDTO = new PayrollDTOGetPostPut();
            payrollDTO.convertToPayrollDTO(payroll.get());
            return Optional.of(payrollDTO);
        }
        return Optional.empty();
    }

    /**
     * Gets a payroll record by its status.
     * @param status the payroll status
     * @return an Optional containing the PayrollDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PayrollDTOGetPostPut> getPayrollByStatus(String status) {
        Optional<Payroll> payroll = payrollRepository.findByStatus(status);
        if (payroll.isPresent()) {
            PayrollDTOGetPostPut payrollDTO = new PayrollDTOGetPostPut();
            payrollDTO.convertToPayrollDTO(payroll.get());
            return Optional.of(payrollDTO);
        }
        return Optional.empty();
    }

    /**
     * Creates a new payroll record.
     * @param payrollDTO the payroll data
     * @return an Optional containing the created PayrollDTOGetPostPut, or empty if a payroll with the same status exists
     */
    public Optional<PayrollDTOGetPostPut> createPayroll(PayrollDTO payrollDTO) {
        if (payrollRepository.findByStatus(payrollDTO.getStatus()).isPresent()) {
            return Optional.empty();
        }

        EmployeeDTO employee;
        PositionDTO position;
        try {
            employee = employeeClient.getEmployeeById(payrollDTO.getEmployeeId());
            if (employee == null) {
                return Optional.empty();
            }
            position = employeeClient.getPositionByName(employee.getPosition());
            if (position == null) {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee or position data: " + e.getMessage());
        }

        Payroll payroll = new Payroll();
        payroll.setStatus(payrollDTO.getStatus());
        payroll.setIssueDate(payrollDTO.getIssueDate());
        payroll.setBaseSalary(position.getBaseSalary());
        payroll.setTotalAdjustments(0); // Will be updated when adjustments are added
        payroll.setNetSalary(position.getBaseSalary()); // Initially, net salary equals base salary
        payroll.setEmployeeId(payrollDTO.getEmployeeId());

        PayrollDTOGetPostPut savedPayroll = new PayrollDTOGetPostPut();
        savedPayroll.convertToPayrollDTO(payrollRepository.save(payroll));
        return Optional.of(savedPayroll);
    }

    /**
     * Updates an existing payroll record.
     * @param id the payroll ID
     * @param payrollDTO the payroll data to update
     * @return an Optional containing the updated PayrollDTOGetPostPut, or empty if not found or status conflict
     */
    public Optional<PayrollDTOGetPostPut> updatePayroll(Long id, PayrollDTO payrollDTO) {
        Optional<Payroll> payroll = payrollRepository.findById(id);
        if (payroll.isPresent()) {
            if (!payroll.get().getStatus().equalsIgnoreCase(payrollDTO.getStatus())) {
                if (payrollRepository.findByStatus(payrollDTO.getStatus()).isPresent()) {
                    return Optional.empty();
                }
            }

            EmployeeDTO employee;
            PositionDTO position;
            try {
                employee = employeeClient.getEmployeeById(payrollDTO.getEmployeeId());
                if (employee == null) {
                    return Optional.empty();
                }
                position = employeeClient.getPositionByName(employee.getPosition());
                if (position == null) {
                    return Optional.empty();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error getting employee or position data: " + e.getMessage());
            }

            Payroll payrollToUpdate = payroll.get();
            payrollToUpdate.setStatus(payrollDTO.getStatus());
            payrollToUpdate.setIssueDate(payrollDTO.getIssueDate());
            payrollToUpdate.setBaseSalary(position.getBaseSalary());
            // Keep existing total adjustments
            // Recalculate net salary
            payrollToUpdate.setNetSalary(position.getBaseSalary() + payrollToUpdate.getTotalAdjustments());
            payrollToUpdate.setEmployeeId(payrollDTO.getEmployeeId());

            PayrollDTOGetPostPut updatedPayrollDTO = new PayrollDTOGetPostPut();
            updatedPayrollDTO.convertToPayrollDTO(payrollRepository.save(payrollToUpdate));
            return Optional.of(updatedPayrollDTO);
        }
        return Optional.empty();
    }

    /**
     * Deletes a payroll record by its ID.
     * @param id the payroll ID
     * @return true if the payroll was deleted, false otherwise
     */
    public boolean deletePayroll(Long id) {
        if (payrollRepository.findById(id).isPresent()) {
            payrollRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Generates a PDF for a payroll record.
     * 
     * @param id the payroll ID
     * @return an Optional containing the PDF content as a byte array if the payroll exists, or empty otherwise
     */
    public Optional<byte[]> generatePayrollPdf(Long id) {
        Optional<Payroll> payrollOpt = payrollRepository.findById(id);
        if (payrollOpt.isPresent()) {
            Payroll payroll = payrollOpt.get();

            // In a real implementation, this would use a PDF library like iText or OpenPDF
            // For testing purposes, we'll just create a simple byte array
            String content = "Payroll ID: " + payroll.getId() + "\n" +
                             "Status: " + payroll.getStatus() + "\n" +
                             "Issue Date: " + payroll.getIssueDate() + "\n" +
                             "Base Salary: " + payroll.getBaseSalary() + "\n" +
                             "Total Adjustments: " + payroll.getTotalAdjustments() + "\n" +
                             "Net Salary: " + payroll.getNetSalary() + "\n" +
                             "Employee ID: " + payroll.getEmployeeId();

            return Optional.of(content.getBytes());
        }
        return Optional.empty();
    }
}
