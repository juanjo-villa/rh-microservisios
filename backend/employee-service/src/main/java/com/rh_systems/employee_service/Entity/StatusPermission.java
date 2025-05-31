package com.rh_systems.employee_service.Entity;

import jakarta.persistence.*;

// import java.util.List;

/**
 * Entity class representing a Status Permission in the system
 */
@Entity
@Table(name = "status_permission")
public class StatusPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 250)
    private String description;


    /**
     * Constructor with all fields
     */
    public StatusPermission(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Empty constructor
     */
    public StatusPermission() {
    }

    /**
     * Gets the status permission ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the status permission ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}