package com.rh_systems.employee_service.dto;
import com.rh_systems.employee_service.Entity.Position;

/**
 * DTO class for Position entity with GET, POST and PUT operations
 */
public class PositionDTOGetPostPut {

    private Long id;
    private String name;
    private String description;
    private float salary;

    /**
     * Empty constructor
     */
    public PositionDTOGetPostPut() {
    }

    /**
     * Constructor with all fields
     */
    public PositionDTOGetPostPut(Long id, String name, String description, float salary) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.salary = salary;
    }

    /**
     * Gets the position ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the position ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the position name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the position name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the position description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the position description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the position salary
     */
    public float getSalary() {
        return salary;
    }

    /**
     * Sets the position salary
     */
    public void setSalary(float salary) {
        this.salary = salary;
    }

    /**
     * Converts Position entity to DTO
     */
    public void convertToPositionDTO(Position position) {
        this.setId(position.getId());
        this.setName(position.getName());
        this.setDescription(position.getDescription());
        this.setSalary(position.getSalary());
    }
}

