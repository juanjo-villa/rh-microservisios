package com.rh_systems.employee_service.security;

import com.rh_systems.employee_service.Entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
// import java.util.stream.Collectors;

/**
 * Class that implements UserDetails for Spring Security authentication
 */
public class EmployeePrincipal implements UserDetails {

    private Long id;
    private String dni;
    private String name;
    private String lastName;
    private String address;
    private String email;
    private String phone;
    private String password;
    private String position;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor with all fields
     */
    public EmployeePrincipal(Long id, String dni,
                             String name, String lastName, String address,
                             String email, String phone, String password,
                             String position, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.position = position;
        this.authorities = authorities;
    }

    /**
     * Creates EmployeePrincipal from Employee entity
     */
    public static EmployeePrincipal create(Employee employee) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(employee.getPosition().getName())
        );

        return new EmployeePrincipal(
                employee.getId(),
                employee.getDni(),
                employee.getName(),
                employee.getLastName(),
                employee.getAddress(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getPassword(),
                employee.getPosition().getName(),
                authorities);
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
     * Gets the employee position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the employee position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
