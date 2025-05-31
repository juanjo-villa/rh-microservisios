package com.rh_systems.payroll_service.dto;

import java.util.Date;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for payroll.
 */
public class PayrollDTO {
    @NotBlank(message = "Status cannot be null")
    @Size(max = 50, message = "Status cannot exceed 50 characters")
    private String status;

    @NotNull(message = "Issue date cannot be null")
    private Date issueDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Base salary must be positive")
    private float baseSalary;

    private float totalAdjustments;

    @DecimalMin(value = "0.0", inclusive = true, message = "Net salary must be positive")
    private float netSalary;

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be at least 1")
    private Long employeeId;

    private EmployeeDTO employee;

    // For backward compatibility with tests
    private Date paymentDate;
    private float amount;

    /**
     * Gets the status.
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the issue date.
     * @return the issue date
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * Sets the issue date.
     * @param issueDate the issue date to set
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * Gets the base salary.
     * @return the base salary
     */
    public float getBaseSalary() {
        return baseSalary;
    }

    /**
     * Sets the base salary.
     * @param baseSalary the base salary to set
     */
    public void setBaseSalary(float baseSalary) {
        this.baseSalary = baseSalary;
    }

    /**
     * Gets the total adjustments.
     * @return the total adjustments
     */
    public float getTotalAdjustments() {
        return totalAdjustments;
    }

    /**
     * Sets the total adjustments.
     * @param totalAdjustments the total adjustments to set
     */
    public void setTotalAdjustments(float totalAdjustments) {
        this.totalAdjustments = totalAdjustments;
    }

    /**
     * Gets the net salary.
     * @return the net salary
     */
    public float getNetSalary() {
        return netSalary;
    }

    /**
     * Sets the net salary.
     * @param netSalary the net salary to set
     */
    public void setNetSalary(float netSalary) {
        this.netSalary = netSalary;
    }

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
     * Gets the payment date.
     * @return the payment date
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets the payment date.
     * @param paymentDate the payment date to set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
        this.issueDate = paymentDate; // Map to issueDate for compatibility
    }

    /**
     * Gets the amount.
     * @return the amount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Sets the amount.
     * @param amount the amount to set
     */
    public void setAmount(float amount) {
        this.amount = amount;
        this.baseSalary = amount; // Map to baseSalary for compatibility
    }
}
