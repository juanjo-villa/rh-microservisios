package com.rh_systems.schedule_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.rh_systems.schedule_service.Entity.Schedule;

public class ScheduleDTOGetPostPut {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime exitTime;
    private Float totalHours;
    private Float deductedHours;

    /**
     * Gets the ID.
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID.
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

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
     * Converts a Schedule entity to a ScheduleDTOGetPostPut object.
     * @param scheduleEntity the Schedule entity to convert
     */
    public void convertToScheduleDTO(Schedule scheduleEntity) {
        this.setId(scheduleEntity.getId());
        this.setDate(scheduleEntity.getDate());
        this.setStartTime(scheduleEntity.getStartTime()); 
        this.setExitTime(scheduleEntity.getExitTime());
        this.setTotalHours(scheduleEntity.getTotalHours());
        this.setDeductedHours(scheduleEntity.getDeductedHours());
    }
}
