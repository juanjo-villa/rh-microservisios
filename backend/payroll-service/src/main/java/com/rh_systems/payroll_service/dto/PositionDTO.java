package com.rh_systems.payroll_service.dto;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for Position information.
 */
public class PositionDTO {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Base salary cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Base salary must be positive")
    private float baseSalary;

    /**
     * Default constructor
     */
    public PositionDTO() {
    }

    /**
     * Constructor with parameters
     *
     * @param id         Position ID
     * @param name       Position name
     * @param baseSalary Base salary for the position
     */
    public PositionDTO(Long id, String name, float baseSalary) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
    }

    /**
     * Gets the position ID
     *
     * @return Position ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the position ID
     *
     * @param id Position ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the position name
     *
     * @return Position name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the position name
     *
     * @param name Position name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the base salary
     *
     * @return Base salary
     */
    public float getBaseSalary() {
        return baseSalary;
    }

    /**
     * Sets the base salary
     *
     * @param baseSalary Base salary to set
     */
    public void setBaseSalary(float baseSalary) {
        this.baseSalary = baseSalary;
    }
}