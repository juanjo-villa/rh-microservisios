package com.rh_systems.performance_service.client;

import com.rh_systems.performance_service.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "${employee-service.url}")
public interface EmployeeClient {

    @GetMapping("/api/employee/{id}")
    EmployeeDTO getEmployeeById(@PathVariable Long id);
}
