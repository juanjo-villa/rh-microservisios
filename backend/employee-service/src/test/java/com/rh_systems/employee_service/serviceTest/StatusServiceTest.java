package com.rh_systems.employee_service.serviceTest;

import com.rh_systems.employee_service.Entity.Employee;
import com.rh_systems.employee_service.Entity.Status;
import com.rh_systems.employee_service.Entity.StatusPermission;
import com.rh_systems.employee_service.dto.StatusDTO;
import com.rh_systems.employee_service.dto.StatusDTOGetPostPut;
import com.rh_systems.employee_service.repository.EmployeeRepository;
import com.rh_systems.employee_service.repository.StatusPermissionRepository;
import com.rh_systems.employee_service.repository.StatusRepository;
import com.rh_systems.employee_service.service.StatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private StatusPermissionRepository statusPermissionRepository;

    @InjectMocks
    private StatusService statusService;

    private Status status;
    private StatusDTO statusDTO;
    private StatusDTOGetPostPut statusDTOGetPostPut;
    private Employee employee;
    private StatusPermission statusPermission;

    @BeforeEach
    void setUp() {
        // Setup test data
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        statusPermission = new StatusPermission();
        statusPermission.setId(1L);
        statusPermission.setName("VACATION");
        statusPermission.setDescription("Vacation permission");

        status = new Status();
        status.setId(1L);
        status.setType("VACATION");
        status.setStartDate(new Date());
        status.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        status.setPaid(1.0f);
        status.setDescription("Annual vacation");
        status.setEmployee(employee);
        status.setStatusPermission(statusPermission);

        statusDTO = new StatusDTO();
        statusDTO.setType("VACATION");
        statusDTO.setStartDate(new Date());
        statusDTO.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        statusDTO.setPaid(1.0f);
        statusDTO.setDescription("Annual vacation");
        statusDTO.setEmployeeId(1L);
        statusDTO.setStatusPermissionId(1L);

        statusDTOGetPostPut = new StatusDTOGetPostPut();
        statusDTOGetPostPut.setId(1L);
        statusDTOGetPostPut.setType("VACATION");
        statusDTOGetPostPut.setStartDate(new Date());
        statusDTOGetPostPut.setEndDate(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7 days later
        statusDTOGetPostPut.setPaid(1.0f);
        statusDTOGetPostPut.setDescription("Annual vacation");
        statusDTOGetPostPut.setEmployeeId(1L);
        statusDTOGetPostPut.setStatusPermissionId(1L);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Status> statuses = new ArrayList<>();
        statuses.add(status);
        when(statusRepository.findAll()).thenReturn(statuses);

        // Act
        List<StatusDTOGetPostPut> result = statusService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(status.getId(), result.get(0).getId());
        assertEquals(status.getType(), result.get(0).getType());
        assertEquals(status.getDescription(), result.get(0).getDescription());
        verify(statusRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        // Arrange
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(status));

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(status.getId(), result.get().getId());
        assertEquals(status.getType(), result.get().getType());
        assertEquals(status.getDescription(), result.get().getDescription());
        verify(statusRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(statusRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(statusRepository, times(1)).findById(1L);
    }

    @Test
    void testSave_Success() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.of(statusPermission));
        when(statusRepository.save(any(Status.class))).thenReturn(status);

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.save(statusDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(status.getId(), result.get().getId());
        assertEquals(status.getType(), result.get().getType());
        assertEquals(status.getDescription(), result.get().getDescription());
        verify(employeeRepository, times(1)).findById(statusDTO.getEmployeeId());
        verify(statusPermissionRepository, times(1)).findById(statusDTO.getStatusPermissionId());
        verify(statusRepository, times(1)).save(any(Status.class));
    }

    @Test
    void testSave_EmployeeNotFound() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.save(statusDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(statusDTO.getEmployeeId());
        verify(statusPermissionRepository, never()).findById(anyLong());
        verify(statusRepository, never()).save(any(Status.class));
    }

    @Test
    void testSave_StatusPermissionNotFound() {
        // Arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.save(statusDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(statusDTO.getEmployeeId());
        verify(statusPermissionRepository, times(1)).findById(statusDTO.getStatusPermissionId());
        verify(statusRepository, never()).save(any(Status.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(status));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.of(statusPermission));
        when(statusRepository.save(any(Status.class))).thenReturn(status);

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.update(1L, statusDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(status.getId(), result.get().getId());
        assertEquals(status.getType(), result.get().getType());
        assertEquals(status.getDescription(), result.get().getDescription());
        verify(statusRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findById(statusDTO.getEmployeeId());
        verify(statusPermissionRepository, times(1)).findById(statusDTO.getStatusPermissionId());
        verify(statusRepository, times(1)).save(any(Status.class));
    }

    @Test
    void testUpdate_StatusNotFound() {
        // Arrange
        when(statusRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.update(1L, statusDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(statusRepository, times(1)).findById(1L);
        verify(employeeRepository, never()).findById(anyLong());
        verify(statusPermissionRepository, never()).findById(anyLong());
        verify(statusRepository, never()).save(any(Status.class));
    }

    @Test
    void testUpdate_EmployeeNotFound() {
        // Arrange
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(status));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.update(1L, statusDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(statusRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findById(statusDTO.getEmployeeId());
        verify(statusPermissionRepository, never()).findById(anyLong());
        verify(statusRepository, never()).save(any(Status.class));
    }

    @Test
    void testUpdate_StatusPermissionNotFound() {
        // Arrange
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(status));
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusDTOGetPostPut> result = statusService.update(1L, statusDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(statusRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findById(statusDTO.getEmployeeId());
        verify(statusPermissionRepository, times(1)).findById(statusDTO.getStatusPermissionId());
        verify(statusRepository, never()).save(any(Status.class));
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(statusRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(statusRepository).deleteById(anyLong());

        // Act
        boolean result = statusService.deleteById(1L);

        // Assert
        assertTrue(result);
        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        when(statusRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean result = statusService.deleteById(1L);

        // Assert
        assertFalse(result);
        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, never()).deleteById(anyLong());
    }
}