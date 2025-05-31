package com.rh_systems.employee_service.dto;

public class JwtResponse {

    private String token;
    private String name;
    private String lastName;
    private String email;

    /**
     * Empty constructor
     */
    public JwtResponse() {
    }

    /**
     * Constructor with all fields
     */
    public JwtResponse(String token, String name, String lastName, String email) {
        this.token = token;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Gets the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token
     */
    public void setToken(String token) {
        this.token = token;
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
     * Gets the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}