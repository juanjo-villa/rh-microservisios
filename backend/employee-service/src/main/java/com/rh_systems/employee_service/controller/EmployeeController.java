package com.rh_systems.employee_service.controller;

import com.rh_systems.employee_service.dto.EmployeeDTO;
import com.rh_systems.employee_service.dto.EmployeeDTOGetPostPut;
import com.rh_systems.employee_service.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST Controller for managing Employee operations
 */
@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Constructor for dependency injection
     *
     * @param employeeService the employee service
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Get all employees
     *
     * @return list of all employees
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmployeeDTOGetPostPut> findAll() {
        return employeeService.findAll();
    }

    /**
     * Find employee by ID
     *
     * @param id employee ID
     * @return employee if found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> findById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Find employee by DNI
     *
     * @param dni employee DNI
     * @return employee if found
     */
    @GetMapping("/dni/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> findByDni(@PathVariable String dni) {
        return employeeService.findByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Find employee by email
     *
     * @param email employee email
     * @return employee if found
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> findByEmail(@PathVariable String email) {
        return employeeService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Find employee by phone number
     *
     * @param phone employee phone number
     * @return employee if found
     */
    @GetMapping("/phone/{phone}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> findByPhone(@PathVariable String phone) {
        return employeeService.findByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save new employee
     *
     * @param employeeDTO employee data
     * @return saved employee
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> save(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.save(employeeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Update employee by ID
     *
     * @param id          employee ID
     * @param employeeDTO updated employee data
     * @return updated employee
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> updateById(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateById(id, employeeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete employee by ID
     *
     * @param id employee ID
     * @return true if deleted successfully
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return employeeService.deleteById(id) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Delete employee by DNI
     *
     * @param dni employee DNI
     * @return true if deleted successfully
     */
    @DeleteMapping("/dni/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteByDni(@PathVariable String dni) {
        return employeeService.deleteByDni(dni) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    /**
     * Update employee by DNI
     *
     * @param dni         employee DNI
     * @param employeeDTO updated employee data
     * @return updated employee
     */
    @PutMapping("/dni/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> updateByDni(@PathVariable String dni, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateByDni(dni, employeeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee by email
     *
     * @param email       employee email
     * @param employeeDTO updated employee data
     * @return updated employee
     */
    @PutMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> updateByEmail(@PathVariable String email, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateByEmail(email, employeeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee by phone number
     *
     * @param phone       employee phone number
     * @param employeeDTO updated employee data
     * @return updated employee
     */
    @PutMapping("/phone/{phone}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTOGetPostPut> updateByPhone(@PathVariable String phone, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateByPhone(phone, employeeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}