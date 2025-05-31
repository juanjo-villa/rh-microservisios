package com.rh_systems.employee_service.service;

import com.rh_systems.employee_service.Entity.Employee;
import com.rh_systems.employee_service.Entity.Position;
import com.rh_systems.employee_service.dto.EmployeeDTO;
import com.rh_systems.employee_service.dto.EmployeeDTOGetPostPut;
import com.rh_systems.employee_service.repository.EmployeeRepository;
import com.rh_systems.employee_service.repository.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Employee management.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Finds all employees and returns a list of EmployeeDTOGetPostPut.
     *
     * @return List of EmployeeDTOGetPostPut objects.
     */
    public List<EmployeeDTOGetPostPut> findAll() {
        List<EmployeeDTOGetPostPut> employeesDTOList = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            EmployeeDTOGetPostPut employeeDTO = new EmployeeDTOGetPostPut();
            employeeDTO.convertToEmployee(employee);
            employeesDTOList.add(employeeDTO);
        }
        return employeesDTOList;
    }

    /**
     * Finds an employee by ID and returns an Optional of EmployeeDTOGetPostPut.
     *
     * @param id Employee ID.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    public Optional<EmployeeDTOGetPostPut> findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            EmployeeDTOGetPostPut employeeDTO = new EmployeeDTOGetPostPut();
            employeeDTO.convertToEmployee(employee.get());
            return Optional.of(employeeDTO);
        }
        return Optional.empty();
    }

    /**
     * Finds an employee by DNI and returns an Optional of EmployeeDTOGetPostPut.
     *
     * @param dni Employee DNI.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    public Optional<EmployeeDTOGetPostPut> findByDni(String dni) {
        Optional<Employee> employee = employeeRepository.findByDni(dni);
        if (employee.isPresent()) {
            EmployeeDTOGetPostPut employeeDTO = new EmployeeDTOGetPostPut();
            employeeDTO.convertToEmployee(employee.get());
            return Optional.of(employeeDTO);
        }
        return Optional.empty();
    }

    /**
     * Creates a new employee from the provided EmployeeDTO and returns the saved EmployeeDTOGetPostPut.
     *
     * @param employeeDTO Employee data to save.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    public Optional<EmployeeDTOGetPostPut> save(EmployeeDTO employeeDTO) {
        Optional<Position> position = positionRepository.findById(employeeDTO.getPositionId());
        if (position.isPresent()) {
            Employee employee = new Employee();
            employee.setDni(employeeDTO.getDni());
            employee.setName(employeeDTO.getName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setAddress(employeeDTO.getAddress());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
            employee.setPosition(position.get()); // Set the position based on the Position entity

            Employee savedEmployee = employeeRepository.save(employee);
            EmployeeDTOGetPostPut resultDTO = new EmployeeDTOGetPostPut();
            resultDTO.convertToEmployee(savedEmployee);
            return Optional.of(resultDTO);
        }
        return Optional.empty(); // Return empty if position is not found.
    }

    /**
     * Updates an existing employee by ID using EmployeeDTO and returns the updated EmployeeDTOGetPostPut.
     *
     * @param id          Employee ID.
     * @param employeeDTO Updated data for the employee.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    public Optional<EmployeeDTOGetPostPut> updateById(Long id, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        Optional<Position> positionOptional = positionRepository.findById(employeeDTO.getPositionId());
        if (employeeOptional.isPresent() && positionOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setDni(employeeDTO.getDni());
            employee.setName(employeeDTO.getName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setAddress(employeeDTO.getAddress());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
            employee.setPosition(positionOptional.get()); // Update the position

            Employee updatedEmployee = employeeRepository.save(employee);
            EmployeeDTOGetPostPut resultDTO = new EmployeeDTOGetPostPut();
            resultDTO.convertToEmployee(updatedEmployee);
            return Optional.of(resultDTO);
        }
        return Optional.empty(); // Return empty if either employee or position is not found
    }

    /**
     * Deletes an employee by ID.
     *
     * @param id Employee ID.
     * @return True if deletion was successful, false otherwise.
     */
    public boolean deleteById(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Deletes an employee by DNI.
     *
     * @param dni Employee DNI.
     * @return True if deletion was successful, false otherwise.
     */
    @Transactional
    public boolean deleteByDni(String dni) {
        Optional<Employee> employee = employeeRepository.findByDni(dni);
        if (employee.isPresent()) {
            employeeRepository.delete(employee.get());
            return true;
        }
        return false;
    }

    /**
     * Updates an existing employee by DNI using EmployeeDTO and returns the updated EmployeeDTOGetPostPut.
     *
     * @param dni         Employee DNI.
     * @param employeeDTO Updated data for the employee.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    @Transactional
    public Optional<EmployeeDTOGetPostPut> updateByDni(String dni, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findByDni(dni);
        Optional<Position> positionOptional = positionRepository.findById(employeeDTO.getPositionId());
        if (employeeOptional.isPresent() && positionOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setDni(employeeDTO.getDni());
            employee.setName(employeeDTO.getName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setAddress(employeeDTO.getAddress());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
            employee.setPosition(positionOptional.get());

            Employee updatedEmployee = employeeRepository.save(employee);
            EmployeeDTOGetPostPut resultDTO = new EmployeeDTOGetPostPut();
            resultDTO.convertToEmployee(updatedEmployee);
            return Optional.of(resultDTO);
        }
        return Optional.empty();
    }

    /**
     * Updates an existing employee by email using EmployeeDTO and returns the updated EmployeeDTOGetPostPut.
     *
     * @param email       Employee email.
     * @param employeeDTO Updated data for the employee.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    @Transactional
    public Optional<EmployeeDTOGetPostPut> updateByEmail(String email, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
        Optional<Position> positionOptional = positionRepository.findById(employeeDTO.getPositionId());
        if (employeeOptional.isPresent() && positionOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setDni(employeeDTO.getDni());
            employee.setName(employeeDTO.getName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setAddress(employeeDTO.getAddress());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
            employee.setPosition(positionOptional.get());

            Employee updatedEmployee = employeeRepository.save(employee);
            EmployeeDTOGetPostPut resultDTO = new EmployeeDTOGetPostPut();
            resultDTO.convertToEmployee(updatedEmployee);
            return Optional.of(resultDTO);
        }
        return Optional.empty();
    }

    /**
     * Updates an existing employee by phone using EmployeeDTO and returns the updated EmployeeDTOGetPostPut.
     *
     * @param phone       Employee phone.
     * @param employeeDTO Updated data for the employee.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    @Transactional
    public Optional<EmployeeDTOGetPostPut> updateByPhone(String phone, EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findByPhone(phone);
        Optional<Position> positionOptional = positionRepository.findById(employeeDTO.getPositionId());
        if (employeeOptional.isPresent() && positionOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setDni(employeeDTO.getDni());
            employee.setName(employeeDTO.getName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setAddress(employeeDTO.getAddress());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
            employee.setPosition(positionOptional.get());

            Employee updatedEmployee = employeeRepository.save(employee);
            EmployeeDTOGetPostPut resultDTO = new EmployeeDTOGetPostPut();
            resultDTO.convertToEmployee(updatedEmployee);
            return Optional.of(resultDTO);
        }
        return Optional.empty();
    }

    /**
     * Finds an employee by email and returns an Optional of EmployeeDTOGetPostPut.
     *
     * @param email Employee email.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    public Optional<EmployeeDTOGetPostPut> findByEmail(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        if (employee.isPresent()) {
            EmployeeDTOGetPostPut employeeDTO = new EmployeeDTOGetPostPut();
            employeeDTO.convertToEmployee(employee.get());
            return Optional.of(employeeDTO);
        }
        return Optional.empty();
    }

    /**
     * Finds an employee by phone and returns an Optional of EmployeeDTOGetPostPut.
     *
     * @param phone Employee phone.
     * @return Optional of EmployeeDTOGetPostPut.
     */
    public Optional<EmployeeDTOGetPostPut> findByPhone(String phone) {
        Optional<Employee> employee = employeeRepository.findByPhone(phone);
        if (employee.isPresent()) {
            EmployeeDTOGetPostPut employeeDTO = new EmployeeDTOGetPostPut();
            employeeDTO.convertToEmployee(employee.get());
            return Optional.of(employeeDTO);
        }
        return Optional.empty();
    }
}