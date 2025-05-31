package com.rh_systems.schedule_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh_systems.schedule_service.dto.EmployeeScheduleDTO;
import com.rh_systems.schedule_service.dto.EmployeeScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.service.EmployeeScheduleService;

import jakarta.validation.Valid;

/**
 * REST controller for managing employee schedules.
 */
@RestController
@RequestMapping("/api/employee-schedule")
public class EmployeeScheduleController {
    @Autowired
    private EmployeeScheduleService employeeScheduleService;

    /**
     * Gets all employee schedules.
     * @return a list of EmployeeScheduleDTOGetPostPut
     */
    @GetMapping
    public List<EmployeeScheduleDTOGetPostPut> getAllEmployeeSchedule() {
        return employeeScheduleService.getAllEmployeeSchedule();
    }

    /**
     * Gets an employee schedule by its ID.
     * @param id the employee schedule ID
     * @return a ResponseEntity with the EmployeeScheduleDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeScheduleDTOGetPostPut> getEmployeeScheduleById(@PathVariable Long id) {
        Optional<EmployeeScheduleDTOGetPostPut> employeeScheduleDTO = employeeScheduleService.getEmployeeScheduleById(id);
        return employeeScheduleDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets an employee schedule by employee ID and schedule ID.
     * @param employeeId the employee ID
     * @param scheduleId the schedule ID
     * @return a ResponseEntity with the EmployeeScheduleDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/employee/{employeeId}/schedule/{scheduleId}")
    public ResponseEntity<EmployeeScheduleDTOGetPostPut> getEmployeeScheduleByEmployeeIdAndScheduleId(@PathVariable Long employeeId, @PathVariable Long scheduleId) {
        Optional<EmployeeScheduleDTOGetPostPut> employeeScheduleDTO = employeeScheduleService.getEmployeeScheduleByEmployeeIdAndScheduleId(employeeId, scheduleId);
        return employeeScheduleDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new employee schedule.
     * @param employeeScheduleDTO the employee schedule data
     * @return a ResponseEntity with the created EmployeeScheduleDTOGetPostPut, or 404 if a schedule with the same employee and schedule ID exists
     */
    @PostMapping("/new-employee-schedule")
    public ResponseEntity<EmployeeScheduleDTOGetPostPut> createEmployeeSchedule(@Valid @RequestBody EmployeeScheduleDTO employeeScheduleDTO) {
        Optional<EmployeeScheduleDTOGetPostPut> savedEmployeeSchedule = employeeScheduleService.createEmployeeSchedule(employeeScheduleDTO);
        return savedEmployeeSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes an employee schedule by its ID.
     * @param id the employee schedule ID
     * @return a ResponseEntity with status 200 if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeSchedule(@PathVariable long id) {
        if (employeeScheduleService.deleteEmployeeSchedule(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
