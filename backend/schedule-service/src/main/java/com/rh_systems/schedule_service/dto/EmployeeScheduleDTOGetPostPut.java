package com.rh_systems.schedule_service.dto;

import com.rh_systems.schedule_service.Entity.EmployeeSchedule;

public class EmployeeScheduleDTOGetPostPut {
    private Long employeeId;
    private Long scheduleId;
    private Long id;

    /**
     * Gets the employee ID.
     * @return the employee ID
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the schedule ID.
     * @return the schedule ID
     */
    public Long getScheduleId() {
        return scheduleId;
    }

    /**
     * Sets the schedule ID.
     * @param scheduleId the schedule ID to set
     */
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

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
     * Converts an EmployeeSchedule entity to a DTO.
     * @param employeeScheduleEntity the EmployeeSchedule entity
     */
    public void convertToEmployeeScheduleDTO(EmployeeSchedule employeeScheduleEntity) {
        this.setId(employeeScheduleEntity.getId());
        this.setEmployeeId(employeeScheduleEntity.getEmployeeId());
        this.setScheduleId(employeeScheduleEntity.getSchedule().getId());
    }
}
