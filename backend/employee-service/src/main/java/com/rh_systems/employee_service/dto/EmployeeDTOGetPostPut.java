package com.rh_systems.employee_service.dto;

import com.rh_systems.employee_service.Entity.Employee;

public class EmployeeDTOGetPostPut {

    private Long id;
    private String dni;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Long positionId;

    /**
     * Empty constructor
     */
    public EmployeeDTOGetPostPut() {
    }

    /**
     * Constructor with all fields
     *
     * @param id          Employee ID
     * @param dni         Employee DNI
     * @param name        Employee name
     * @param lastName    Employee last name
     * @param email       Employee email
     * @param phone       Employee phone number
     * @param address     Employee address
     * @param positionId  Employee position ID
     */
    public EmployeeDTOGetPostPut(Long id, String dni, String name, String lastName, String email, String phone, String address, Long positionId) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.positionId = positionId;
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
     * @param id Employee ID
     */
    public void setId(Long id) {
        this.id = id;
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

    public void convertToEmployee(Employee employee) {
        this.setId(employee.getId());
        this.setDni(employee.getDni());
        this.setName(employee.getName());
        this.setLastName(employee.getLastName());
        this.setEmail(employee.getEmail());
        this.setPhone(employee.getPhone());
        this.setAddress(employee.getAddress());
        this.setPositionId(employee.getPosition().getId());
    }
}
