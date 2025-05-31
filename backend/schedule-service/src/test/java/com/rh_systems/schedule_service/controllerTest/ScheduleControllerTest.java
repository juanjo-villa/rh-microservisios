package com.rh_systems.schedule_service.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rh_systems.schedule_service.controller.ScheduleController;
import com.rh_systems.schedule_service.dto.ScheduleDTO;
import com.rh_systems.schedule_service.dto.ScheduleDTOGetPostPut;
import com.rh_systems.schedule_service.service.ScheduleService;

@ExtendWith(MockitoExtension.class)
public class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();
    }

    @Test
    void shouldReturn201WhenCreatingSchedule() throws Exception {
        // Arrange
        ScheduleDTO dto = new ScheduleDTO();
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setExitTime(LocalTime.of(17, 0));
        dto.setTotalHours(8.0f);
        dto.setDeductedHours(1.0f);
        dto.setEmployeeIds(List.of(1L, 2L));

        ScheduleDTOGetPostPut responseDto = new ScheduleDTOGetPostPut();
        responseDto.setId(1L);
        responseDto.setDate(dto.getDate());
        responseDto.setStartTime(dto.getStartTime());
        responseDto.setExitTime(dto.getExitTime());
        responseDto.setTotalHours(dto.getTotalHours());
        responseDto.setDeductedHours(dto.getDeductedHours());

        when(scheduleService.createScheduleWithEmployees(any(ScheduleDTO.class)))
            .thenReturn(Optional.of(responseDto));

        // Configure ObjectMapper to handle Java 8 date/time types
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(post("/api/schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400WhenScheduleWithSameDateExists() throws Exception {
        // Arrange
        ScheduleDTO dto = new ScheduleDTO();
        dto.setDate(LocalDate.now());
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setExitTime(LocalTime.of(17, 0));
        dto.setTotalHours(8.0f);
        dto.setDeductedHours(1.0f);
        dto.setEmployeeIds(List.of(1L, 2L));

        when(scheduleService.createScheduleWithEmployees(any(ScheduleDTO.class)))
            .thenReturn(Optional.empty());

        // Configure ObjectMapper to handle Java 8 date/time types
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(post("/api/schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }
}
