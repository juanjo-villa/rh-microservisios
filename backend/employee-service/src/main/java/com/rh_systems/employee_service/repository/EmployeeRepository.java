package com.rh_systems.employee_service.repository;

import com.rh_systems.employee_service.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Employee entity that provides CRUD operations and custom queries
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     * Finds an employee by their name
     */
    Optional<Employee> findByName(String name);

    /**
     * Finds an employee by their DNI (national ID)
     */
    Optional<Employee> findByDni(String dni);

    /**
     * Finds an employee by their email address
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Finds an employee by their phone number
     */
    Optional<Employee> findByPhone(String phone);

    /**
     * Checks if an employee exists with the given name
     */
    boolean existsByName(String name);

    /**
     * Checks if an employee exists with the given DNI
     */
    boolean existsByDni(String dni);

    /**
     * Checks if an employee exists with the given email
     */
    boolean existsByEmail(String email);

    /**
     * Checks if an employee exists with the given phone number
     */
    boolean existsByPhone(String phone);

    /**
     * Deletes an employee by their DNI
     */
    void deleteByDni(String dni);

    /**
     * Counts how many employees have the given DNI
     */
    Long countByDni(String dni);

    /**
     * Counts how many employees have the given email
     */
    Long countByEmail(String email);

}
