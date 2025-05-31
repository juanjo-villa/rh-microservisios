package com.rh_systems.payroll_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh_systems.payroll_service.Entity.Payroll;

/**
 * Repository interface for Payroll entity.
 */
@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    /**
     * Finds a Payroll by its status.
     * @param status the payroll status
     * @return an Optional containing the Payroll if found, or empty otherwise
     */
    Optional<Payroll> findByStatus(String status);
}
