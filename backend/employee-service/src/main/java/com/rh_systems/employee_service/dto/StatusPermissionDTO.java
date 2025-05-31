package com.rh_systems.employee_service.dto;

import jakarta.validation.constraints.*;

public class StatusPermissionDTO {

    @NotBlank(message = "A status permission name must be provided")
    @Size(min = 3, max = 8, message = "The name must be between 3 and 8 characters long")
    private String name;

    @NotBlank(message = "A description must be provided")
    @Size(min = 2, max = 250, message = "The description must be between 2 and 250 characters long")
    private String description;

    /**
     * Empty constructor
     */
    public StatusPermissionDTO() {
    }

    /**
     * Constructor with all fields
     *
     * @param name        Status Permission name
     * @param description Status Permission description
     */
    public StatusPermissionDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the status permission name
     *
     * @return Status Permission name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the status permission name
     *
     * @param name Status Permission name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the status permission description
     *
     * @return Status Permission description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the status permission description
     *
     * @param description Status Permission description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}