package com.rh_systems.employee_service.dto;

import jakarta.validation.constraints.*;

/**
 * DTO class for Employee login
 */
public class EmployeeLoginDTO {

    @Email(message = "The email must be a valid email address")
    @NotBlank(message = "An employee email must be provided")
    private String email;

    @NotBlank(message = "An employee password must be provided")
    private String password;

    /**
     * Empty constructor
     */
    public EmployeeLoginDTO() {
    }

    /**
     * Constructor with all fields
     *
     * @param email    Employee email
     * @param password Employee password
     */
    public EmployeeLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the employee email
     *
     * @return Employee email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the employee email
     *
     * @param email Employee email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the employee password
     *
     * @return Employee password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee password
     *
     * @param password Employee password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
