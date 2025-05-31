package com.rh_systems.schedule_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rh_systems.schedule_service.dto.EmployeeDTO;

/**
 * Feign client for employee-service.
 */
@FeignClient(name = "employee-service", url = "${employee-service.url}")
public interface EmployeeClient {
    
    /**
     * Gets an employee by its ID.
     * @param id the employee ID
     * @return the EmployeeDTO
     */
    @GetMapping("/api/employees/{id}")
    EmployeeDTO getEmployeeById(@PathVariable Long id);
}