package com.rh_systems.employee_service.Entity;

import jakarta.persistence.*;
// import lombok.Data;
// import lombok.NoArgsConstructor;

/**
 * Entity class representing a Position in the system
 */

@Entity
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 250)
    private String description;

    private float salary;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private java.util.List<com.rh_systems.employee_service.Entity.Employee> employees;

    /**
     * Constructor with all fields
     */
    public Position(Long id, String name, String description, float salary, java.util.List<com.rh_systems.employee_service.Entity.Employee> employees) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.salary = salary;
        this.employees = employees;
    }

    /**
     * Constructor void
     * */
    public Position() {
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
     * Gets the list of employees in this position
     */
    public java.util.List<com.rh_systems.employee_service.Entity.Employee> getEmployees() {
        return employees;
    }

    /**
     * Sets the list of employees in this position
     */
    public void setEmployees(java.util.List<com.rh_systems.employee_service.Entity.Employee> employees) {
        this.employees = employees;
    }
}