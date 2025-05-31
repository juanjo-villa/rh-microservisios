package com.rh_systems.payroll_service.dto;

import com.rh_systems.payroll_service.Entity.PayrollAdjustments;
import com.rh_systems.payroll_service.Entity.PayrollAdjustments.AdjustmentType;

/**
 * Data Transfer Object for getting, posting, and putting payroll adjustments.
 */
public class PayrollAdjustmentsDTOGetPostPut {
    private Long id;
    private AdjustmentType type;
    private String description;
    private float amount;
    private Long payrollId;

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

    /**
     * Converts a PayrollAdjustments entity to this DTO.
     * @param payrollAdjustmentEntity the entity to convert
     */
    public void convertToPayrollAdjustmentDTO(PayrollAdjustments payrollAdjustmentEntity) {
        this.setId(payrollAdjustmentEntity.getId());
        this.setType(payrollAdjustmentEntity.getType());
        this.setDescription(payrollAdjustmentEntity.getDescription());
        this.setAmount(payrollAdjustmentEntity.getAmount());
        this.setPayrollId(payrollAdjustmentEntity.getPayroll().getId());
    }
}
