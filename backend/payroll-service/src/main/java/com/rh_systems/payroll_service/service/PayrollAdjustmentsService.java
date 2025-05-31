package com.rh_systems.payroll_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh_systems.payroll_service.Entity.Payroll;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTO;
import com.rh_systems.payroll_service.dto.PayrollAdjustmentsDTOGetPostPut;
import com.rh_systems.payroll_service.repository.PayrollAdjustmentsRepository;
import com.rh_systems.payroll_service.repository.PayrollRepository;

@Service
public class PayrollAdjustmentsService {
    @Autowired
    private PayrollAdjustmentsRepository payrollAdjustmentsRepository;

    @Autowired
    private PayrollRepository payrollRepository;

    /**
     * Gets all payroll adjustments.
     * @return a list of PayrollAdjustmentsDTOGetPostPut
     */
    public List<PayrollAdjustmentsDTOGetPostPut> getAllPayrollAdjustments() {
        List<PayrollAdjustmentsDTOGetPostPut> payrollAdjustmentToReturn = new ArrayList<>();
        List<PayrollAdjustments> payrollAdjustment = payrollAdjustmentsRepository.findAll();
        for (PayrollAdjustments pa : payrollAdjustment) {
            PayrollAdjustmentsDTOGetPostPut payrollAdjustmentDTO = new PayrollAdjustmentsDTOGetPostPut();
            payrollAdjustmentDTO.convertToPayrollAdjustmentDTO(pa);
            payrollAdjustmentToReturn.add(payrollAdjustmentDTO);
        }
        return payrollAdjustmentToReturn;
    }

    /**
     * Gets a payroll adjustment by its ID.
     * @param id the payroll adjustment ID
     * @return an Optional containing the PayrollAdjustmentsDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PayrollAdjustmentsDTOGetPostPut> getPayrollAdjustmentsById(Long id) {
        Optional<PayrollAdjustments> payrollAdjustment = payrollAdjustmentsRepository.findById(id);
        if (payrollAdjustment.isPresent()) {
            PayrollAdjustmentsDTOGetPostPut payrollAdjustmentDTO = new PayrollAdjustmentsDTOGetPostPut();
            payrollAdjustmentDTO.convertToPayrollAdjustmentDTO(payrollAdjustment.get());
            return Optional.of(payrollAdjustmentDTO);
        }
        return Optional.empty();
    }

    /**
     * Gets a payroll adjustment by its type.
     * @param type the payroll adjustment type
     * @return an Optional containing the PayrollAdjustmentsDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PayrollAdjustmentsDTOGetPostPut> getPayrollAdjustmentsByType(AdjustmentType type) {
        Optional<PayrollAdjustments> payrollAdjustment = payrollAdjustmentsRepository.findByType(type);
        if (payrollAdjustment.isPresent()) {
            PayrollAdjustmentsDTOGetPostPut payrollAdjustmentDTO = new PayrollAdjustmentsDTOGetPostPut();
            payrollAdjustmentDTO.convertToPayrollAdjustmentDTO(payrollAdjustment.get());
            return Optional.of(payrollAdjustmentDTO);
        }
        return Optional.empty();
    }

    /**
     * Gets a payroll adjustment by its type string.
     * @param typeStr the payroll adjustment type as a string
     * @return an Optional containing the PayrollAdjustmentsDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PayrollAdjustmentsDTOGetPostPut> getPayrollAdjustmentsByType(String typeStr) {
        try {
            AdjustmentType type = AdjustmentType.valueOf(typeStr);
            return getPayrollAdjustmentsByType(type);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new payroll adjustment.
     * @param payrollAdjustmentDTO the payroll adjustment data
     * @return an Optional containing the created PayrollAdjustmentsDTOGetPostPut, or empty if a payroll adjustment with the same type exists
     */
    public Optional<PayrollAdjustmentsDTOGetPostPut> createPayrollAdjustments(PayrollAdjustmentsDTO payrollAdjustmentDTO) {
        if (payrollAdjustmentsRepository.findByType(payrollAdjustmentDTO.getType()).isPresent()) {
            return Optional.empty();
        }

        // Get the payroll to update its totalAdjustments and netSalary
        Optional<Payroll> payrollOpt = payrollRepository.findById(payrollAdjustmentDTO.getPayrollId());
        if (!payrollOpt.isPresent()) {
            return Optional.empty();
        }

        Payroll payroll = payrollOpt.get();
        float adjustmentAmount = payrollAdjustmentDTO.getAmount();

        // Calculate the adjustment based on type
        if (payrollAdjustmentDTO.getType() == AdjustmentType.BONUS) {
            payroll.setTotalAdjustments(payroll.getTotalAdjustments() + adjustmentAmount);
        } else if (payrollAdjustmentDTO.getType() == AdjustmentType.DISCOUNT) {
            payroll.setTotalAdjustments(payroll.getTotalAdjustments() - adjustmentAmount);
        }

        // Update net salary
        payroll.setNetSalary(payroll.getBaseSalary() + payroll.getTotalAdjustments());
        payrollRepository.save(payroll);

        // Create the adjustment
        PayrollAdjustments payrollAdjustment = new PayrollAdjustments();
        payrollAdjustment.setType(payrollAdjustmentDTO.getType());
        payrollAdjustment.setDescription(payrollAdjustmentDTO.getDescription());
        payrollAdjustment.setAmount(payrollAdjustmentDTO.getAmount());
        payrollAdjustment.setPayroll(payroll);

        PayrollAdjustmentsDTOGetPostPut savedPayrollAdjustment = new PayrollAdjustmentsDTOGetPostPut();
        savedPayrollAdjustment.convertToPayrollAdjustmentDTO(payrollAdjustmentsRepository.save(payrollAdjustment));
        return Optional.of(savedPayrollAdjustment);
    }

    /**
     * Updates an existing payroll adjustment.
     * @param id the payroll adjustment ID
     * @param payrollAdjustmentDTO the payroll adjustment data to update
     * @return an Optional containing the updated PayrollAdjustmentsDTOGetPostPut, or empty if not found or type conflict
     */
    public Optional<PayrollAdjustmentsDTOGetPostPut> updatePayrollAdjustment(Long id, PayrollAdjustmentsDTO payrollAdjustmentDTO) {
        Optional<PayrollAdjustments> payrollAdjustment = payrollAdjustmentsRepository.findById(id);
        if (payrollAdjustment.isPresent()) {
            PayrollAdjustments payrollAdjustmentToUpdate = payrollAdjustment.get();

            // Check if type is changing and if the new type already exists
            if (payrollAdjustmentToUpdate.getType() != payrollAdjustmentDTO.getType()) {
                if (payrollAdjustmentsRepository.findByType(payrollAdjustmentDTO.getType()).isPresent()) {
                    return Optional.empty();
                }
            }

            // Get the payroll to update its totalAdjustments and netSalary
            Optional<Payroll> payrollOpt = payrollRepository.findById(payrollAdjustmentDTO.getPayrollId());
            if (!payrollOpt.isPresent()) {
                return Optional.empty();
            }

            Payroll payroll = payrollOpt.get();

            // Reverse the effect of the old adjustment
            if (payrollAdjustmentToUpdate.getType() == AdjustmentType.BONUS) {
                payroll.setTotalAdjustments(payroll.getTotalAdjustments() - payrollAdjustmentToUpdate.getAmount());
            } else if (payrollAdjustmentToUpdate.getType() == AdjustmentType.DISCOUNT) {
                payroll.setTotalAdjustments(payroll.getTotalAdjustments() + payrollAdjustmentToUpdate.getAmount());
            }

            // Apply the new adjustment
            if (payrollAdjustmentDTO.getType() == AdjustmentType.BONUS) {
                payroll.setTotalAdjustments(payroll.getTotalAdjustments() + payrollAdjustmentDTO.getAmount());
            } else if (payrollAdjustmentDTO.getType() == AdjustmentType.DISCOUNT) {
                payroll.setTotalAdjustments(payroll.getTotalAdjustments() - payrollAdjustmentDTO.getAmount());
            }

            // Update net salary
            payroll.setNetSalary(payroll.getBaseSalary() + payroll.getTotalAdjustments());
            payrollRepository.save(payroll);

            // Update the adjustment
            payrollAdjustmentToUpdate.setType(payrollAdjustmentDTO.getType());
            payrollAdjustmentToUpdate.setDescription(payrollAdjustmentDTO.getDescription());
            payrollAdjustmentToUpdate.setAmount(payrollAdjustmentDTO.getAmount());
            payrollAdjustmentToUpdate.setPayroll(payroll);

            PayrollAdjustmentsDTOGetPostPut updatedPayrollAdjustmentDTO = new PayrollAdjustmentsDTOGetPostPut();
            updatedPayrollAdjustmentDTO.convertToPayrollAdjustmentDTO(payrollAdjustmentsRepository.save(payrollAdjustmentToUpdate));
            return Optional.of(updatedPayrollAdjustmentDTO);
        }
        return Optional.empty();
    }

    /**
     * Deletes a payroll adjustment by its ID.
     * @param id the payroll adjustment ID
     * @return true if the payroll adjustment was deleted, false otherwise
     */
    public boolean deletePayrollAdjustment(Long id) {
        Optional<PayrollAdjustments> payrollAdjustmentOpt = payrollAdjustmentsRepository.findById(id);
        if (payrollAdjustmentOpt.isPresent()) {
            PayrollAdjustments payrollAdjustment = payrollAdjustmentOpt.get();
            Payroll payroll = payrollAdjustment.getPayroll();

            // Reverse the effect of the adjustment on the payroll
            if (payrollAdjustment.getType() == AdjustmentType.BONUS) {
                payroll.setTotalAdjustments(payroll.getTotalAdjustments() - payrollAdjustment.getAmount());
            } else if (payrollAdjustment.getType() == AdjustmentType.DISCOUNT) {
                payroll.setTotalAdjustments(payroll.getTotalAdjustments() + payrollAdjustment.getAmount());
            }

            // Update net salary
            payroll.setNetSalary(payroll.getBaseSalary() + payroll.getTotalAdjustments());
            payrollRepository.save(payroll);

            // Delete the adjustment
            payrollAdjustmentsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
