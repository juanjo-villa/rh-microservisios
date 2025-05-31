package com.rh_systems.employee_service.dto;

import jakarta.validation.constraints.*;

/**
 * DTO class for Employee entity
 */
public class EmployeeDTO {

    @NotBlank(message = "An employee ID must be provided")
    @Size(min = 1, max = 10, message = "The ID must be between 1 and 10 characters long")
    @Pattern(regexp = "^[0-9A-Za-z]+$", message = "The DNI must contain only alphanumeric characters")
    private String dni;

    @NotBlank(message = "An employee name must be provided")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters long")
    private String name;

    @NotBlank(message = "An employee last name must be provided")
    @Size(min = 3, max = 100, message = "The last name must be between 3 and 100 characters long")
    private String lastName;

    @NotBlank(message = "An employee address must be provided")
    @Size(min = 3, max = 100, message = "The address must be between 3 and 100 characters long")
    private String address;

    @Email(message = "The email must be a valid email address")
    @NotBlank(message = "An employee email must be provided")
    private String email;

    @NotBlank(message = "An employee phone number must be provided")
    @Size(max = 11, message = "The phone number must not exceed 11 digits")
    @Pattern(regexp = "^[0-9]+$", message = "The phone number must contain only numeric characters")
    private String phone;

    @NotBlank(message = "An employee password must be provided")
    @Size(min = 4, max = 100, message = "The password must be between 4 and 100 characters long")
    private String password;


    @Min(value = 1, message = "The position ID must be greater than 0")
    private Long positionId;


    /**
     * Empty constructor
     */
    public EmployeeDTO() {
    }

    /**
     * Constructor with all fields
     *
     * @param dni          Employee ID
     * @param name         Employee name
     * @param lastName     Employee last name
     * @param address      Employee address
     * @param email        Employee email
     * @param phone        Employee phone
     * @param password     Employee password
     * @param positionId   Employee position ID
     *
     */
    public EmployeeDTO(String dni, String name, String lastName, String address, String email,
                       String phone, String password, Long positionId) {
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.positionId = positionId;
    }

    /**
     * Gets the employee DNI
     *
     * @return Employee DNI
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets the employee DNI
     *
     * @param dni Employee DNI
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Gets the employee name
     *
     * @return Employee name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee name
     *
     * @param name Employee name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the employee last name
     *
     * @return Employee last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee last name
     *
     * @param lastName Employee last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the employee address
     *
     * @return Employee address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the employee address
     *
     * @param address Employee address
     */
    public void setAddress(String address) {
        this.address = address;
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
     * Gets the employee phone
     *
     * @return Employee phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the employee phone
     *
     * @param phone Employee phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
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


    /**
     * Gets the employee position ID
     *
     * @return Employee position ID
     */
    public Long getPositionId() {
        return positionId;
    }

    /**
     * Sets the employee position ID
     *
     * @param positionId Employee position ID
     */
    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

}
