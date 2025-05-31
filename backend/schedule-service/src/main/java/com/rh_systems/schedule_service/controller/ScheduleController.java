package com.rh_systems.schedule_service.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rh_systems.schedule_service.dto.ScheduleDTO;
import com.rh_systems.schedule_service.dto.ScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing schedules.
 */
@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedule", description = "Schedule management endpoints")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * Gets all schedules.
     * @return a list of ScheduleDTOGetPostPut
     */
    @GetMapping
    public List<ScheduleDTOGetPostPut> getAllSchedule() {
        return scheduleService.getAllSchedule();
    }

    /**
     * Gets a schedule by its ID.
     * @param id the schedule ID
     * @return a ResponseEntity with the ScheduleDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTOGetPostPut> getScheduleById(@PathVariable Long id) {
        Optional<ScheduleDTOGetPostPut> scheduleDTO = scheduleService.getScheduleById(id);
        return scheduleDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets a schedule by its date.
     * @param date the schedule date
     * @return a ResponseEntity with the ScheduleDTOGetPostPut if found, or 404 otherwise
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<ScheduleDTOGetPostPut> getScheduleByDate(@PathVariable LocalDate date) {
        Optional<ScheduleDTOGetPostPut> scheduleDTO = scheduleService.getScheduleByDate(date);
        return scheduleDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new schedule.
     * @param scheduleDTO the schedule data
     * @return a ResponseEntity with the created ScheduleDTOGetPostPut, or 400 if a schedule with the same date exists
     */
    @PostMapping("/simple")
    @Operation(summary = "Create a new schedule without employee associations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Schedule created successfully"),
        @ApiResponse(responseCode = "400", description = "Schedule with the same date already exists")
    })
    public ResponseEntity<ScheduleDTOGetPostPut> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        Optional<ScheduleDTOGetPostPut> savedSchedule = scheduleService.createSchedule(scheduleDTO);
        return savedSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Creates a new schedule with employee associations.
     * @param scheduleDTO the schedule data, including a list of employee IDs
     * @return a ResponseEntity with the created ScheduleDTOGetPostPut, or 400 if a schedule with the same date exists
     */
    @PostMapping
    @Operation(summary = "Create a new schedule with employee associations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Schedule created successfully with employee associations"),
        @ApiResponse(responseCode = "400", description = "Schedule with the same date already exists")
    })
    public ResponseEntity<ScheduleDTOGetPostPut> createScheduleWithEmployees(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        Optional<ScheduleDTOGetPostPut> savedSchedule = scheduleService.createScheduleWithEmployees(scheduleDTO);
        return savedSchedule.map(schedule -> ResponseEntity.status(HttpStatus.CREATED).body(schedule))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Updates an existing schedule.
     * @param id the schedule ID
     * @param scheduleDTO the schedule data to update
     * @return a ResponseEntity with the updated ScheduleDTOGetPostPut, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTOGetPostPut> updateSchedule(@PathVariable long id, @Valid @RequestBody ScheduleDTO scheduleDTO) {
        Optional<ScheduleDTOGetPostPut> updatedSchedule = scheduleService.updatedSchedule(id, scheduleDTO);
        return updatedSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a schedule by its ID.
     * @param id the schedule ID
     * @return a ResponseEntity with status 200 if deleted, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        if (scheduleService.deleteSchedule(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
