package com.rh_systems.employee_service.dto;

import com.rh_systems.employee_service.Entity.Status;

import java.util.Date;

public class StatusDTOGetPostPut {

    private Long id;
    private String type;
    private Date startDate;
    private Date endDate;
    private String description;
    private float paid;
    private Long employeeId;
    private Long statusPermissionId;

    /**
     * Empty constructor
     */
    public StatusDTOGetPostPut() {
    }

    /**
     * Constructor with all fields
     */
    public StatusDTOGetPostPut(Long id, String type, Date startDate, Date endDate,
                               String description, float paid, Long employeeId, Long statusPermissionId) {
        this.id = id;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.paid = paid;
        this.employeeId = employeeId;
        this.statusPermissionId = statusPermissionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPaid() {
        return paid;
    }

    public void setPaid(float paid) {
        this.paid = paid;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getStatusPermissionId() {
        return statusPermissionId;
    }

    public void setStatusPermissionId(Long statusPermissionId) {
        this.statusPermissionId = statusPermissionId;
    }

    public void convertToStatus(Status status) {
        this.setId(status.getId());
        this.setType(status.getType());
        this.setStartDate(status.getStartDate());
        this.setEndDate(status.getEndDate());
        this.setDescription(status.getDescription());
        this.setPaid(status.getPaid());
        this.setEmployeeId(status.getEmployee().getId());
        this.setStatusPermissionId(status.getStatusPermission().getId());
    }
}