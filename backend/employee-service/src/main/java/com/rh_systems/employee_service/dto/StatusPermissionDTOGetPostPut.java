package com.rh_systems.employee_service.dto;

import com.rh_systems.employee_service.Entity.StatusPermission;

public class StatusPermissionDTOGetPostPut {

    private Long id;
    private String name;
    private String description;

    /**
     * Empty constructor
     */
    public StatusPermissionDTOGetPostPut() {
    }

    /**
     * Constructor with all fields
     *
     * @param id          Status Permission ID
     * @param name        Status Permission name
     * @param description Status Permission description
     */
    public StatusPermissionDTOGetPostPut(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the status permission ID
     *
     * @return Status Permission ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the status permission ID
     *
     * @param id Status Permission ID
     */
    public void setId(Long id) {
        this.id = id;
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

    /**
     * Converts a StatusPermission entity to this DTO
     *
     * @param statusPermission The StatusPermission entity to convert
     */
    public void convertToStatusPermissionDTO(StatusPermission statusPermission) {
        this.setId(statusPermission.getId());
        this.setName(statusPermission.getName());
        this.setDescription(statusPermission.getDescription());
    }
}
