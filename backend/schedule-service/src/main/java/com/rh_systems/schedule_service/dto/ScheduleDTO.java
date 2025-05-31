package com.rh_systems.schedule_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for Schedule.
 */
public class ScheduleDTO {
    /**
     * The date of the schedule.
     */
    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    /**
     * The start time of the schedule.
     */
    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    /**
     * The exit time of the schedule.
     */
    @NotNull(message = "Exit time cannot be null")
    private LocalTime exitTime;

    /**
     * The total hours of the schedule.
     */
    @NotNull(message = "Total hours cannot be null")
    @PositiveOrZero(message = "Total hours must be zero or positive")
    @Max(value = 24, message = "Total hours cannot exceed 24")
    private Float totalHours;

    /**
     * The deducted hours of the schedule.
    */
    @NotNull(message = "Deducted hours cannot be null")
    @PositiveOrZero(message = "Deducted hours must be zero or positive")
    @Max(value = 24, message = "Deducted hours cannot exceed 24")
    private Float deductedHours; 

    /**
     * The list of employee IDs associated with this schedule.
     */
    private List<Long> employeeIds;


    /**
     * Gets the date.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    } 

    /**
     * Gets the start time.
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     * @param startTime the start time to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the exit time.
     * @return the exit time
     */
    public LocalTime getExitTime() {
        return exitTime;
    }

    /**
     * Sets the exit time.
     * @param exitTime the exit time to set
     */
    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Gets the total hours.
     * @return the total hours
     */
    public Float getTotalHours() {
        return totalHours;
    }

    /**
     * Sets the total hours.
     * @param totalHours the total hours to set
     */
    public void setTotalHours(Float totalHours) {
        this.totalHours = totalHours;
    }

    /**
     * Gets the deducted hours.
     * @return the deducted hours
     */
    public Float getDeductedHours() {
        return deductedHours;
    }

    /**
     * Sets the deducted hours.
     * @param deductedHours the deducted hours to set
     */
    public void setDeductedHours(Float deductedHours) {
        this.deductedHours = deductedHours;
    }

    /**
     * Gets the employee IDs.
     * @return the list of employee IDs
     */
    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    /**
     * Sets the employee IDs.
     * @param employeeIds the list of employee IDs to set
     */
    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
