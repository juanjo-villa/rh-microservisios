package com.rh_systems.employee_service.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rh_systems.employee_service.Entity.Employee;
import com.rh_systems.employee_service.Entity.Position;
import com.rh_systems.employee_service.dto.EmployeeDTO;
import com.rh_systems.employee_service.dto.EmployeeDTOGetPostPut;
import com.rh_systems.employee_service.repository.EmployeeRepository;
import com.rh_systems.employee_service.repository.PositionRepository;
import com.rh_systems.employee_service.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeDTO employeeDTO;
    private Employee employee;
    private Position position;

    @BeforeEach
    void setUp() {
        // Setup common test data
        position = new Position();
        position.setId(1L);
        position.setName("Developer");

        employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDni("12345678A");
        employee.setPhone("123456789");
        employee.setAddress("123 Main St");
        employee.setPassword("encodedPassword");
        employee.setPosition(position);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<EmployeeDTOGetPostPut> result = employeeService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(employee.getId(), result.get(0).getId());
        assertEquals(employee.getName(), result.get(0).getName());
        assertEquals(employee.getLastName(), result.get(0).getLastName());
        assertEquals(employee.getEmail(), result.get(0).getEmail());
    }

    @Test
    void testFindById_Found() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByDni_Found() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.of(employee));

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findByDni("12345678A");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
    }

    @Test
    void testFindByDni_NotFound() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findByDni("12345678A");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByEmail_Found() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findByEmail("john.doe@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findByEmail("john.doe@example.com");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByPhone_Found() {
        // Arrange
        when(employeeRepository.findByPhone(anyString())).thenReturn(Optional.of(employee));

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findByPhone("123456789");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
    }

    @Test
    void testFindByPhone_NotFound() {
        // Arrange
        when(employeeRepository.findByPhone(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.findByPhone("123456789");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testSave_Success() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.save(employeeDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testSave_PositionNotFound() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.save(employeeDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateById_Success() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateById(1L, employeeDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testUpdateById_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateById(1L, employeeDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateById_PositionNotFound() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateById(1L, employeeDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(employeeRepository.existsById(anyLong())).thenReturn(true);

        // Act
        boolean result = employeeService.deleteById(1L);

        // Assert
        assertTrue(result);
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean result = employeeService.deleteById(1L);

        // Assert
        assertFalse(result);
        verify(employeeRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testDeleteByDni_Success() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.of(employee));

        // Act
        boolean result = employeeService.deleteByDni("12345678A");

        // Assert
        assertTrue(result);
        verify(employeeRepository, times(1)).delete(any(Employee.class));
    }

    @Test
    void testDeleteByDni_NotFound() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.empty());

        // Act
        boolean result = employeeService.deleteByDni("12345678A");

        // Assert
        assertFalse(result);
        verify(employeeRepository, times(0)).delete(any(Employee.class));
    }

    @Test
    void testUpdateByDni_Success() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByDni("12345678A", employeeDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testUpdateByDni_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByDni("12345678A", employeeDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateByDni_PositionNotFound() {
        // Arrange
        when(employeeRepository.findByDni(anyString())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByDni("12345678A", employeeDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateByEmail_Success() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByEmail("john.doe@example.com", employeeDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testUpdateByEmail_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByEmail("john.doe@example.com", employeeDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateByEmail_PositionNotFound() {
        // Arrange
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByEmail("john.doe@example.com", employeeDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateByPhone_Success() {
        // Arrange
        when(employeeRepository.findByPhone(anyString())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByPhone("123456789", employeeDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee.getId(), result.get().getId());
        assertEquals(employee.getName(), result.get().getName());
        assertEquals(employee.getLastName(), result.get().getLastName());
        assertEquals(employee.getEmail(), result.get().getEmail());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void testUpdateByPhone_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findByPhone(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByPhone("123456789", employeeDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void testUpdateByPhone_PositionNotFound() {
        // Arrange
        when(employeeRepository.findByPhone(anyString())).thenReturn(Optional.of(employee));
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeDTOGetPostPut> result = employeeService.updateByPhone("123456789", employeeDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }
}
