package com.rh_systems.employee_service.Entity;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Entity class representing an Employee Status in the system
 */
@Entity
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String type;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    private float paid;

    @Column(length = 250)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "id_statusPermission")
    private StatusPermission statusPermission;

    /**
     * Constructor with all fields
     */
    public Status(Long id, String type, Date startDate, Date endDate, float paid, String description,
                  Employee employee, StatusPermission statusPermission) {
        this.id = id;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paid = paid;
        this.description = description;
        this.employee = employee;
        this.statusPermission = statusPermission;
    }

    /**
     * Empty constructor
     */
    public Status() {
    }

    /**
     * Gets the status ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the status ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the status type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the status type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the paid amount
     */
    public float getPaid() {
        return paid;
    }

    /**
     * Sets the paid amount
     */
    public void setPaid(float paid) {
        this.paid = paid;
    }

    /**
     * Gets the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Gets the status permission
     */
    public StatusPermission getStatusPermission() {
        return statusPermission;
    }

    /**
     * Sets the status permission
     */
    public void setStatusPermission(StatusPermission statusPermission) {
        this.statusPermission = statusPermission;
    }
}