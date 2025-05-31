package com.rh_systems.schedule_service.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rh_systems.schedule_service.Entity.Schedule;
import com.rh_systems.schedule_service.client.EmployeeClient;
import com.rh_systems.schedule_service.dto.EmployeeDTO;
import com.rh_systems.schedule_service.dto.ScheduleDTO;
import com.rh_systems.schedule_service.repository.EmployeeScheduleRepository;
import com.rh_systems.schedule_service.repository.ScheduleRepository;
import com.rh_systems.schedule_service.service.ScheduleService;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private EmployeeScheduleRepository employeeScheduleRepository;

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void shouldCreateScheduleWithMultipleEmployees() {
        // Arrange
        ScheduleDTO dto = new ScheduleDTO();
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setExitTime(LocalTime.of(17, 0));
        dto.setTotalHours(8.0f);
        dto.setDeductedHours(1.0f);
        dto.setEmployeeIds(List.of(1L, 2L));

        Schedule savedSchedule = new Schedule();
        savedSchedule.setId(1L);
        savedSchedule.setDate(dto.getDate());
        savedSchedule.setStartTime(dto.getStartTime());
        savedSchedule.setExitTime(dto.getExitTime());
        savedSchedule.setTotalHours(dto.getTotalHours());
        savedSchedule.setDeductedHours(dto.getDeductedHours());

        when(scheduleRepository.findByDate(any(LocalDate.class))).thenReturn(Optional.empty());
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);
        when(employeeClient.getEmployeeById(1L))
            .thenReturn(new EmployeeDTO(1L, "John", "Doe", "john.doe@example.com", "Developer"));
        when(employeeClient.getEmployeeById(2L))
            .thenReturn(new EmployeeDTO(2L, "Jane", "Smith", "jane.smith@example.com", "Designer"));

        // Act
        scheduleService.createScheduleWithEmployees(dto);

        // Assert
        verify(scheduleRepository).save(any(Schedule.class));
        verify(employeeClient, times(2)).getEmployeeById(anyLong());
        verify(employeeScheduleRepository).saveAll(any());
    }
}
