package com.rh_systems.payroll_service.Entity;

import jakarta.persistence.*;

/**
 * Entity representing payroll adjustments.
 */
@Entity
@Table(name = "payroll_adjustments")
public class PayrollAdjustments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AdjustmentType type;
    private String description;
    private float amount;

    /**
     * Enum for adjustment types.
     */
    public enum AdjustmentType {
        BONUS,
        DISCOUNT
    }

    @ManyToOne
    @JoinColumn(name = "id_payroll")
    private Payroll payroll;

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
     * Gets the payroll.
     * @return the payroll
     */
    public Payroll getPayroll() {
        return payroll;
    }

    /**
     * Sets the payroll.
     * @param payroll the payroll to set
     */
    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }
}
