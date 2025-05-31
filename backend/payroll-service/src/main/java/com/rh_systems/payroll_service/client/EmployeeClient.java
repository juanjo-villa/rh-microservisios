package com.rh_systems.payroll_service.client;

import com.rh_systems.payroll_service.config.FeignClientConfig;
import com.rh_systems.payroll_service.dto.EmployeeDTO;
import com.rh_systems.payroll_service.dto.EmployeeSaveDTO;
import com.rh_systems.payroll_service.dto.PositionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "employee-service", url = "${employee-service.url}", configuration = FeignClientConfig.class)
public interface EmployeeClient {

    @GetMapping("/api/employee/{id}")
    EmployeeDTO getEmployeeById(@PathVariable("id") Long id);

    @GetMapping("/api/position/{id}")
    PositionDTO getPositionById(@PathVariable("id") Long id);

    @GetMapping("/api/position/name/{name}")
    PositionDTO getPositionByName(@PathVariable("name") String name);

    @PostMapping("/api/employee")
    EmployeeDTO saveEmployee(@RequestBody EmployeeSaveDTO employeeSaveDTO);
}
