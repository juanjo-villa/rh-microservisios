package com.rh_systems.payroll_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh_systems.payroll_service.Entity.PayrollAdjustments;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;

/**
 * Repository interface for PayrollAdjustments entity.
 */
@Repository
public interface PayrollAdjustmentsRepository extends JpaRepository<PayrollAdjustments, Long> {
    /**
     * Finds a PayrollAdjustments by its type.
     * @param type the payroll adjustment type
     * @return an Optional containing the PayrollAdjustments if found, or empty otherwise
     */
    Optional<PayrollAdjustments> findByType(AdjustmentType type);

    /**
     * Finds a PayrollAdjustments by its type string.
     * @param type the payroll adjustment type as a string
     * @return an Optional containing the PayrollAdjustments if found, or empty otherwise
     */
    default Optional<PayrollAdjustments> findByPayrollAdjustmentsType(String type) {
        try {
            return findByType(AdjustmentType.valueOf(type));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
