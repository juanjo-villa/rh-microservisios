package com.rh_systems.performance_service.dto;
import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for Employee information.
 */
public class EmployeeDTO {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotBlank(message = "DNI cannot be blank")
    @Size(max = 20, message = "DNI cannot exceed 20 characters")
    private String dni;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9]{9}$", message = "Phone must be 9 digits")
    private String phone;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @NotBlank(message = "Position cannot be blank")
    @Size(max = 100, message = "Position cannot exceed 100 characters")
    private String position;

    @NotBlank(message = "Department cannot be blank")
    @Size(max = 100, message = "Department cannot exceed 100 characters")
    private String department;

    /**
     * Default constructor
     */
    public EmployeeDTO() {
    }

    /**
     * Constructor with parameters
     *
     * @param id         Employee ID
     * @param dni        DNI number
     * @param name       First name
     * @param lastName   Last name
     * @param email      Email address
     * @param phone      Phone number
     * @param address    Home address
     * @param position   Job position
     * @param department Department
     */
    public EmployeeDTO(Long id, String dni, String name, String lastName, String email,
                       String phone, String address, String position, String department) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.position = position;
        this.department = department;
    }

    /**
     * Gets the employee ID
     *
     * @return Employee ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the employee ID
     *
     * @param id Employee ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the DNI number
     *
     * @return DNI number
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets the DNI number
     *
     * @param dni DNI number to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Gets the first name
     *
     * @return First name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first name
     *
     * @param name First name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the last name
     *
     * @return Last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     *
     * @param lastName Last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address
     *
     * @return Email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address
     *
     * @param email Email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number
     *
     * @return Phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number
     *
     * @param phone Phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the address
     *
     * @return Home address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address
     *
     * @param address Home address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the job position
     *
     * @return Job position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the job position
     *
     * @param position Job position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Gets the department
     *
     * @return Department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department
     *
     * @param department Department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }
}
