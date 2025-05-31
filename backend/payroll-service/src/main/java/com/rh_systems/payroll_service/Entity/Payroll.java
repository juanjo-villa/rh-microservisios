package com.rh_systems.payroll_service.Entity;

import java.util.Date;

import jakarta.persistence.*;

/**
 * Entity representing a payroll record.
 */
@Entity
@Table(name = "payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issueDate")
    private Date issueDate;

    private float baseSalary;
    private float totalAdjustments;
    private float netSalary;
    private String status;
    private Long employeeId;

    // For backward compatibility with tests
    @Transient
    private Date paymentDate;
    @Transient
    private float amount;

    /**
     * Default constructor.
     */
    public Payroll() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id               the ID
     * @param issueDate        the issue date
     * @param baseSalary       the base salary
     * @param totalAdjustments the total adjustments
     * @param netSalary        the net salary
     * @param status           the status
     * @param employeeId       the employee ID
     */
    public Payroll(Long id, Date issueDate, float baseSalary, float totalAdjustments, float netSalary, String status, Long employeeId) {
        this.id = id;
        this.issueDate = issueDate;
        this.baseSalary = baseSalary;
        this.totalAdjustments = totalAdjustments;
        this.netSalary = netSalary;
        this.status = status;
        this.employeeId = employeeId;
    }

    /**
     * Gets the employee ID.
     *
     * @return the employee ID
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     *
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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
     * Gets the payment date.
     * @return the payment date
     */
    public Date getPaymentDate() {
        return paymentDate != null ? paymentDate : issueDate;
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
        return amount > 0 ? amount : baseSalary;
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
