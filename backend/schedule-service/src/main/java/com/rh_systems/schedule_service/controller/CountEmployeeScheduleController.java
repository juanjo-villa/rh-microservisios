package com.rh_systems.schedule_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh_systems.schedule_service.dto.CountEmployeeScheduleDTO;
import com.rh_systems.schedule_service.dto.CountEmployeeScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.service.CountEmployeeScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing count employee schedules.
 */
@RestController
@RequestMapping("/api/count-schedule")
@Tag(name = "Conteo de Horas", description = "Gestion del conteo de horas trabajadas por empleado")
public class CountEmployeeScheduleController {
    @Autowired
    private CountEmployeeScheduleService countEmployeeScheduleService;

    /**
     * Gets all count employee schedules.
     * @return a list of CountEmployeeScheduleDTOGetPostPut
     */
    @GetMapping
    public List<CountEmployeeScheduleDTOGetPostPut> getAllCountEmployeeSchedule() {
        return countEmployeeScheduleService.getAllEmployeeSchedule();
    }

    /**
     * Gets a count employee schedule by its ID.
     * @param id the count employee schedule ID
     * @return a ResponseEntity with the CountEmployeeScheduleDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountEmployeeScheduleDTOGetPostPut> getCountEmployeeScheduleById(@PathVariable Long id) {
        Optional<CountEmployeeScheduleDTOGetPostPut> countEmployeeScheduleDTO = countEmployeeScheduleService.getCountEmployeeScheduleById(id);
        return countEmployeeScheduleDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets a count employee schedule by work hours.
     * @param workHours the work hours
     * @return a ResponseEntity with the CountEmployeeScheduleDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/work-hours/{workHours}")
    public ResponseEntity<CountEmployeeScheduleDTOGetPostPut> getCountEmployeeScheduleByWorkHours(@PathVariable Float workHours) {
        Optional<CountEmployeeScheduleDTOGetPostPut> countEmployeeScheduleDTO = countEmployeeScheduleService.getCountEmployeeScheduleByWorkHours(workHours);
        return countEmployeeScheduleDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new count employee schedule.
     * @param countEmployeerScheduleDTO the count employee schedule data
     * @return a ResponseEntity with the created CountEmployeeScheduleDTOGetPostPut, or 400 if a schedule with the same work hours exists
     */
    @PostMapping
    public ResponseEntity<CountEmployeeScheduleDTOGetPostPut> createCountEmployeeSchedule(@Valid @RequestBody CountEmployeeScheduleDTO countEmployeerScheduleDTO) {
        Optional<CountEmployeeScheduleDTOGetPostPut> savedCountEmployeeSchedule = countEmployeeScheduleService.createCountEmployeeSchedule(countEmployeerScheduleDTO);
        return savedCountEmployeeSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Updates an existing count employee schedule.
     * @param id the count employee schedule ID
     * @param countEmployeerScheduleDTO the count employee schedule data to update
     * @return a ResponseEntity with the updated CountEmployeeScheduleDTOGetPostPut, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<CountEmployeeScheduleDTOGetPostPut> updateCountEmployeeSchedule(@PathVariable long id, @Valid @RequestBody CountEmployeeScheduleDTO countEmployeerScheduleDTO) {
        Optional<CountEmployeeScheduleDTOGetPostPut> updatedCountEmployeeSchedule = countEmployeeScheduleService.updateCountEmployeeSchedule(id, countEmployeerScheduleDTO);
        return updatedCountEmployeeSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a count employee schedule by its ID.
     * @param id the count employee schedule ID
     * @return a ResponseEntity with status 200 if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountEmployeeSchedule(@PathVariable Long id) {
        if (countEmployeeScheduleService.deleteCountEmployeeSchedule(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Registers hours worked for an employee.
     * @param employeeId the employee ID
     * @param hours the hours worked
     * @return a ResponseEntity with status 200 if registered successfully
     */
    @Operation(summary = "Registrar horas trabajadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horas registradas correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/{employeeId}")
    public ResponseEntity<Void> addHours(
        @PathVariable Long employeeId,
        @RequestParam float hours
    ) {
        if (hours <= 0) {
            return ResponseEntity.badRequest().build();
        }

        countEmployeeScheduleService.registerWorkHours(employeeId, hours);
        return ResponseEntity.ok().build();
    }
}
