package com.rh_systems.payroll_service.dto;

import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;
import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for payroll adjustments.
 */
public class PayrollAdjustmentsDTO {
    @NotNull(message = "Payroll adjustment type cannot be null")
    private AdjustmentType type;

    @Size(max = 255, message = "Payroll adjustment description cannot exceed 255 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be positive")
    private float amount;

    @NotNull(message = "Payroll ID cannot be null")
    @Min(value = 1, message = "Payroll ID must be at least 1")
    private Long payrollId;

    /**
     * Gets the type.
     * @return the type
     */
    public AdjustmentType getType() {
        return type;
    }

    /**
     * Sets the type.
     * @param type the type to set
     */
    public void setType(AdjustmentType type) {
        this.type = type;
    }

    /**
     * Sets the type using a string.
     * @param typeStr the type string to set
     */
    public void setType(String typeStr) {
        this.type = AdjustmentType.valueOf(typeStr);
    }

    /**
     * Gets the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
    }

    /**
     * Gets the payroll ID.
     * @return the payroll ID
     */
    public Long getPayrollId() {
        return payrollId;
    }

    /**
     * Sets the payroll ID.
     * @param payrollId the payroll ID to set
     */
    public void setPayrollId(Long payrollId) {
        this.payrollId = payrollId;
    }
}
