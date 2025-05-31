package com.rh_systems.employee_service.Entity;

import jakarta.persistence.*;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// import java.util.List;

/**
 * Entity class representing an Employee in the system
 */

@Entity
@Table(name = "employee",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email", name = "uk_employee_email"),
                @UniqueConstraint(columnNames = "phone", name = "uk_employee_phone"),
                @UniqueConstraint(columnNames = "dni", name = "uk_employee_dni")
        },
        indexes = {
                @Index(name = "idx_employee_email", columnList = "email"),
                @Index(name = "idx_employee_dni", columnList = "dni")
        }
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String dni;

    @Column(length = 100)
    private String name;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 150)
    private String address;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 15, unique = true)
    private String phone;

    @Column(length = 100)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_position")
    private Position position;


    /**
     * Constructor with all fields
     */
    public Employee(Long id, String dni, String name, String lastName, String address, String email, String phone,
                    String password, Position position) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.position = position;
    }

    /**
     * Constructor void
     */
    public Employee() {

    }

    /**
     * Gets the employee ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the employee ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the employee DNI
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets the employee DNI
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Gets the employee name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the employee last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the employee address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the employee address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the employee email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the employee email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the employee phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the employee phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the employee password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the employee position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the employee position
     */
    public void setPosition(Position position) {
        this.position = position;
    }


}