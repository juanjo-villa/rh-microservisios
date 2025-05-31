package com.rh_systems.schedule_service.dto;

import java.time.LocalDate;

import com.rh_systems.schedule_service.Entity.CountEmployeeSchedule;

public class CountEmployeeScheduleDTOGetPostPut {
    private Long id;
    private LocalDate workDate;
    private Float workHours;
    private Long employeeScheduleId;

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

    /**
     * Converts a CountEmployeeSchedule entity to a DTO.
     * @param countEmployeeScheduleEntity the CountEmployeeSchedule entity
     */
    public void convertToCountEmployeeScheduleDTO(CountEmployeeSchedule countEmployeeScheduleEntity) {
        this.setId(countEmployeeScheduleEntity.getId());
        this.setWorkDate(countEmployeeScheduleEntity.getWorkDate());
        this.setWorkHours(countEmployeeScheduleEntity.getWorkHours());
        this.setEmployeeScheduleId(countEmployeeScheduleEntity.getEmployeeSchedule().getId());
    }  
}
