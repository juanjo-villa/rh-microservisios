package com.rh_systems.schedule_service.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.rh_systems.schedule_service.Entity.EmployeeSchedule;
import com.rh_systems.schedule_service.Entity.Schedule;
import com.rh_systems.schedule_service.dto.EmployeeScheduleDTO;
import com.rh_systems.schedule_service.dto.EmployeeScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.repository.EmployeeScheduleRepository;
import com.rh_systems.schedule_service.repository.ScheduleRepository;
import com.rh_systems.schedule_service.service.EmployeeScheduleService;

@ExtendWith(MockitoExtension.class)
public class EmployeeScheduleServiceTest {

    @Mock
    private EmployeeScheduleRepository employeeScheduleRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private EmployeeScheduleService employeeScheduleService;

    private EmployeeSchedule employeeSchedule;
    private Schedule schedule;
    private EmployeeScheduleDTO employeeScheduleDTO;

    @BeforeEach
    void setUp() {
        // Setup common test data
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setDate(java.time.LocalDate.now());
        schedule.setStartTime(java.time.LocalTime.of(8, 0));
        schedule.setExitTime(java.time.LocalTime.of(16, 0));
        schedule.setTotalHours(8.0f);
        schedule.setDeductedHours(0.0f);

        employeeSchedule = new EmployeeSchedule();
        employeeSchedule.setId(1L);
        employeeSchedule.setEmployeeId(1L);
        employeeSchedule.setSchedule(schedule);

        employeeScheduleDTO = new EmployeeScheduleDTO();
        employeeScheduleDTO.setEmployeeId(1L);
        employeeScheduleDTO.setScheduleId(1L);
    }

    @Test
    void testGetAllEmployeeSchedule() {
        // Arrange
        List<EmployeeSchedule> employeeSchedules = new ArrayList<>();
        employeeSchedules.add(employeeSchedule);
        when(employeeScheduleRepository.findAll()).thenReturn(employeeSchedules);

        // Act
        List<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.getAllEmployeeSchedule();

        // Assert
        assertEquals(1, result.size());
        assertEquals(employeeSchedule.getId(), result.get(0).getId());
        assertEquals(employeeSchedule.getEmployeeId(), result.get(0).getEmployeeId());
        assertEquals(employeeSchedule.getSchedule().getId(), result.get(0).getScheduleId());
    }

    @Test
    void testGetEmployeeScheduleById_Found() {
        // Arrange
        when(employeeScheduleRepository.findById(anyLong())).thenReturn(Optional.of(employeeSchedule));

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.getEmployeeScheduleById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employeeSchedule.getId(), result.get().getId());
        assertEquals(employeeSchedule.getEmployeeId(), result.get().getEmployeeId());
        assertEquals(employeeSchedule.getSchedule().getId(), result.get().getScheduleId());
    }

    @Test
    void testGetEmployeeScheduleById_NotFound() {
        // Arrange
        when(employeeScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.getEmployeeScheduleById(1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetEmployeeScheduleByEmployeeIdAndScheduleId_Found() {
        // Arrange
        when(employeeScheduleRepository.findByEmployeeIdAndScheduleId(anyLong(), anyLong())).thenReturn(Optional.of(employeeSchedule));

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.getEmployeeScheduleByEmployeeIdAndScheduleId(1L, 1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employeeSchedule.getId(), result.get().getId());
        assertEquals(employeeSchedule.getEmployeeId(), result.get().getEmployeeId());
        assertEquals(employeeSchedule.getSchedule().getId(), result.get().getScheduleId());
    }

    @Test
    void testGetEmployeeScheduleByEmployeeIdAndScheduleId_NotFound() {
        // Arrange
        when(employeeScheduleRepository.findByEmployeeIdAndScheduleId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.getEmployeeScheduleByEmployeeIdAndScheduleId(1L, 1L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateEmployeeSchedule_Success() {
        // Arrange
        when(employeeScheduleRepository.findByEmployeeIdAndScheduleId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));
        when(employeeScheduleRepository.save(any(EmployeeSchedule.class))).thenReturn(employeeSchedule);

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.createEmployeeSchedule(employeeScheduleDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employeeSchedule.getId(), result.get().getId());
        assertEquals(employeeSchedule.getEmployeeId(), result.get().getEmployeeId());
        assertEquals(employeeSchedule.getSchedule().getId(), result.get().getScheduleId());
    }

    @Test
    void testCreateEmployeeSchedule_AlreadyExists() {
        // Arrange
        when(employeeScheduleRepository.findByEmployeeIdAndScheduleId(anyLong(), anyLong())).thenReturn(Optional.of(employeeSchedule));

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.createEmployeeSchedule(employeeScheduleDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCreateEmployeeSchedule_ScheduleNotFound() {
        // Arrange
        when(employeeScheduleRepository.findByEmployeeIdAndScheduleId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(scheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmployeeScheduleDTOGetPostPut> result = employeeScheduleService.createEmployeeSchedule(employeeScheduleDTO);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteEmployeeSchedule_Success() {
        // Arrange
        when(employeeScheduleRepository.findById(anyLong())).thenReturn(Optional.of(employeeSchedule));

        // Act
        boolean result = employeeScheduleService.deleteEmployeeSchedule(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeleteEmployeeSchedule_NotFound() {
        // Arrange
        when(employeeScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = employeeScheduleService.deleteEmployeeSchedule(1L);

        // Assert
        assertFalse(result);
    }
}
