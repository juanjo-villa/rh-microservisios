package com.rh_systems.schedule_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for counting employee schedules.
 */
public class CountEmployeeScheduleDTO {
    @NotNull(message = "Date cannot be blank") // Use @NotNull for LocalDate
    private LocalDate workDate;

    @NotNull(message = "Work hours cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Work hours must be positive")
    private Float workHours;

    @NotNull(message = "Employee Schedule ID cannot be null")
    @Min(value = 1, message = "Employee Schedule ID must be at least 1")
    private Long employeeScheduleId;
    
    /**
     * Gets the work date.
     * @return the work date
     */
    public LocalDate getWorkDate() {
        return workDate;
    }

    /**
     * Sets the work date.
     * @param workDate the work date to set
     */
    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    /**
     * Gets the work hours.
     * @return the work hours
     */
    public Float getWorkHours() {
        return workHours;
    }

    /**
     * Sets the work hours.
     * @param workHours the work hours to set
     */
    public void setWorkHours(Float workHours) {
        this.workHours = workHours;
    }

    /**
     * Gets the employee schedule ID.
     * @return the employee schedule ID
     */
    public Long getEmployeeScheduleId() {
        return employeeScheduleId;
    }

    /**
     * Sets the employee schedule ID.
     * @param employeeScheduleId the employee schedule ID to set
     */
    public void setEmployeeScheduleId(Long employeeScheduleId) {
        this.employeeScheduleId = employeeScheduleId;
    }
}
