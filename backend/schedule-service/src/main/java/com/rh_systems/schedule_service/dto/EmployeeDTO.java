package com.rh_systems.schedule_service.dto;

/**
 * Data Transfer Object for Employee from employee-service.
 */
public class EmployeeDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String position;

    /**
     * Default constructor.
     */
    public EmployeeDTO() {
    }

    /**
     * Constructor with all fields.
     * 
     * @param id the employee ID
     * @param name the employee name
     * @param lastName the employee last name
     * @param email the employee email
     * @param position the employee position
     */
    public EmployeeDTO(Long id, String name, String lastName, String email, String position) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.position = position;
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
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the last name.
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the position.
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the position.
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }
}