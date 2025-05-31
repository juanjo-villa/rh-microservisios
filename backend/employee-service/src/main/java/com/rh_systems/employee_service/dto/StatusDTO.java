package com.rh_systems.employee_service.dto;

import jakarta.validation.constraints.*;

import java.util.Date;

public class StatusDTO {

    @NotBlank(message = "A status type must be provided")
    @Size(min = 1, max = 10, message = "The ID must be between 1 and 10 characters long")
    private String type;

    @NotNull(message = "A start date must be provided")
    @PastOrPresent(message = "The start date must be in the past or present")
    private Date startDate;

    @NotNull(message = "An end date must be provided")
    private Date endDate;

    @NotNull(message = "A status paid must be provided")
    @Min(value = 0, message = "The paid must be greater than or equal to 0")
    private float paid;

    @NotBlank(message = "A status description must be provided")
    @Size(min = 1, max = 250, message = "The description must be between 1 and 250 characters long")
    private String description;

    @Min(value = 1, message = "The employee ID must be greater than 0")
    private Long employeeId;

    @Min(value = 1, message = "The status permission ID must be greater than 0")
    private Long statusPermissionId;

    /**
     * Empty constructor
     */
    public StatusDTO() {
    }

    /**
     * Constructor with all fields
     *
     * @param type        Status type
     * @param startDate   Status start date
     * @param endDate     Status end date
     * @param paid        Status paid amount
     * @param description Status description
     */
    public StatusDTO(String type, Date startDate, Date endDate, float paid, String description, Long employeeId, Long statusPermissionId) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paid = paid;
        this.description = description;
        this.employeeId = employeeId;
        this.statusPermissionId = statusPermissionId;
    }

    /**
     * Gets the status type
     *
     * @return Status type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the status type
     *
     * @param type Status type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the status start date
     *
     * @return Status start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the status start date
     *
     * @param startDate Status start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the status end date
     *
     * @return Status end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the status end date
     *
     * @param endDate Status end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the status paid amount
     *
     * @return Status paid amount
     */
    public float getPaid() {
        return paid;
    }

    /**
     * Sets the status paid amount
     *
     * @param paid Status paid amount
     */
    public void setPaid(float paid) {
        this.paid = paid;
    }

    /**
     * Gets the status description
     *
     * @return Status description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the status description
     *
     * @param description Status description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the status employee ID
     *
     * @return Status employee ID
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Set the status employee ID
     *
     * @param employeeId Status employee ID
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Get the status statusPermission ID
     *
     * @return status statusPermission ID
     */
    public Long getStatusPermissionId() {
        return statusPermissionId;
    }

    /**
     * Sets the status statusPermission ID
     *
     * @param statusPermissionId Status statusPosition ID
     */
    public void setStatusPermissionId(Long statusPermissionId) {
        this.statusPermissionId = statusPermissionId;
    }
}
