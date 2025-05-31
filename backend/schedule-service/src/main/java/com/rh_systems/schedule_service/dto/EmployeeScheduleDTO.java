package com.rh_systems.schedule_service.dto;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for Employee Schedule.
 */
public class EmployeeScheduleDTO {
    @Min(1) // Employee ID must be at least 1
    private Long employeeId;

    @Min(1) // Schedule ID must be at least 1
    private Long scheduleId;

    /**
     * Gets the employee ID.
     * @return the employee ID.
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     * @param employeeId the employee ID to set.
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the schedule ID.
     * @return the schedule ID.
     */
    public Long getScheduleId() {
        return scheduleId;
    }

    /**
     * Sets the schedule ID.
     * @param scheduleId the schedule ID to set.
     */
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
