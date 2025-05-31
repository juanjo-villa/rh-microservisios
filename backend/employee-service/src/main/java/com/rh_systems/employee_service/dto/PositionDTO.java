package com.rh_systems.employee_service.dto;

import jakarta.validation.constraints.*;

public class PositionDTO {

    /**
     * Position name with validation constraints
     * Must be between 3 and 8 characters long
     */
    @NotBlank(message = "A position must be provided")
    @Size(min = 3, max = 8, message = "The name must be between 3 and 8 characters long")
    private String name;

    /**
     * Position description with validation constraints
     * Must be between 2 and 250 characters long
     */
    @NotBlank(message = "A description must be provided")
    @Size(min = 2, max = 250, message = "The description must be between 2 and 250 characters long")
    private String description;

    /**
     * Position salary with validation constraint
     * Must be greater than 0
     */
    @NotBlank(message = "A salary must be provided")
    @Min(value = 1, message = "The salary must be greater than 0")
    private float salary;

    /**
     * Empty constructor
     */
    public PositionDTO() {
    }

    /**
     * Constructor with all fields
     *
     * @param name        Position name
     * @param description Position description
     * @param salary      Position salary
     */
    public PositionDTO(String name, String description, float salary) {
        this.name = name;
        this.description = description;
        this.salary = salary;
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
     * @param name Position name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the position description
     *
     * @return Position description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the position description
     *
     * @param description Position description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the position salary
     *
     * @return Position salary
     */
    public float getSalary() {
        return salary;
    }

    /**
     * Sets the position salary
     *
     * @param salary Position salary
     */
    public void setSalary(float salary) {
        this.salary = salary;
    }
}
