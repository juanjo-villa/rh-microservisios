package com.rh_systems.schedule_service.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rh_systems.schedule_service.Entity.CountEmployeeSchedule;
import com.rh_systems.schedule_service.Entity.EmployeeSchedule;
import com.rh_systems.schedule_service.Entity.Schedule;
import com.rh_systems.schedule_service.repository.CountEmployeeScheduleRepository;
import com.rh_systems.schedule_service.repository.EmployeeScheduleRepository;
import com.rh_systems.schedule_service.service.CountEmployeeScheduleService;

@ExtendWith(MockitoExtension.class)
public class CountEmployeeScheduleServiceTest {

    @Mock
    private CountEmployeeScheduleRepository countEmployeeScheduleRepository;
    
    @Mock
    private EmployeeScheduleRepository employeeScheduleRepository;

    @InjectMocks
    private CountEmployeeScheduleService countEmployeeScheduleService;

    @Test
    void shouldRegisterWorkHoursForNewEmployee() {
        // Arrange
        Long employeeId = 1L;
        float hoursWorked = 5.0f;
        
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        employeeSchedule.setId(1L);
        employeeSchedule.setEmployeeId(employeeId);
        
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        employeeSchedule.setSchedule(schedule);
        
        when(countEmployeeScheduleRepository.findByEmployeeId(employeeId)).thenReturn(Optional.empty());
        when(employeeScheduleRepository.findByEmployeeId(employeeId)).thenReturn(List.of(employeeSchedule));
        
        // Act
        countEmployeeScheduleService.registerWorkHours(employeeId, hoursWorked);
        
        // Assert
        verify(countEmployeeScheduleRepository).save(any(CountEmployeeSchedule.class));
    }
    
    @Test
    void shouldUpdateExistingWorkHours() {
        // Arrange
        Long employeeId = 1L;
        float hoursWorked = 2.0f;
        
        CountEmployeeSchedule existingCount = new CountEmployeeSchedule();
        existingCount.setId(1L);
        existingCount.setWorkHours(3.0f);
        existingCount.setWorkDate(LocalDate.now());
        
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        employeeSchedule.setId(1L);
        employeeSchedule.setEmployeeId(employeeId);
        existingCount.setEmployeeSchedule(employeeSchedule);
        
        when(countEmployeeScheduleRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(existingCount));
        
        // Act
        countEmployeeScheduleService.registerWorkHours(employeeId, hoursWorked);
        
        // Assert
        verify(countEmployeeScheduleRepository).save(any(CountEmployeeSchedule.class));
    }
    
    @Test
    void shouldNotExceedEightHours() {
        // Arrange
        Long employeeId = 1L;
        float hoursWorked = 3.0f;
        
        CountEmployeeSchedule existingCount = new CountEmployeeSchedule();
        existingCount.setId(1L);
        existingCount.setWorkHours(7.0f);
        existingCount.setWorkDate(LocalDate.now());
        
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        employeeSchedule.setId(1L);
        employeeSchedule.setEmployeeId(employeeId);
        existingCount.setEmployeeSchedule(employeeSchedule);
        
        when(countEmployeeScheduleRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(existingCount));
        
        // Act
        countEmployeeScheduleService.registerWorkHours(employeeId, hoursWorked);
        
        // Assert
        verify(countEmployeeScheduleRepository).save(any(CountEmployeeSchedule.class));
    }
    
    @Test
    void shouldNotRegisterMoreHoursWhenAlreadyEightHours() {
        // Arrange
        Long employeeId = 1L;
        float hoursWorked = 2.0f;
        
        CountEmployeeSchedule existingCount = new CountEmployeeSchedule();
        existingCount.setId(1L);
        existingCount.setWorkHours(8.0f);
        existingCount.setWorkDate(LocalDate.now());
        
        EmployeeSchedule employeeSchedule = new EmployeeSchedule();
        employeeSchedule.setId(1L);
        employeeSchedule.setEmployeeId(employeeId);
        existingCount.setEmployeeSchedule(employeeSchedule);
        
        when(countEmployeeScheduleRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(existingCount));
        
        // Act
        countEmployeeScheduleService.registerWorkHours(employeeId, hoursWorked);
        
        // Assert
        verify(countEmployeeScheduleRepository, never()).save(any(CountEmployeeSchedule.class));
    }
}