package com.rh_systems.schedule_service.controllerTest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.rh_systems.schedule_service.controller.CountEmployeeScheduleController;
import com.rh_systems.schedule_service.service.CountEmployeeScheduleService;

@ExtendWith(MockitoExtension.class)
public class CountEmployeeScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountEmployeeScheduleService countEmployeeScheduleService;

    @InjectMocks
    private CountEmployeeScheduleController countEmployeeScheduleController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(countEmployeeScheduleController).build();
    }

    @Test
    void shouldRegisterHoursSuccessfully() throws Exception {
        // Arrange
        Long employeeId = 1L;
        float hours = 5.0f;
        
        doNothing().when(countEmployeeScheduleService).registerWorkHours(employeeId, hours);
        
        // Act & Assert
        mockMvc.perform(post("/api/count-schedule/{employeeId}", employeeId)
            .param("hours", String.valueOf(hours)))
            .andExpect(status().isOk());
        
        verify(countEmployeeScheduleService).registerWorkHours(employeeId, hours);
    }
    
    @Test
    void shouldReturnBadRequestWhenHoursIsZeroOrNegative() throws Exception {
        // Arrange
        Long employeeId = 1L;
        float hours = 0.0f;
        
        // Act & Assert
        mockMvc.perform(post("/api/count-schedule/{employeeId}", employeeId)
            .param("hours", String.valueOf(hours)))
            .andExpect(status().isBadRequest());
    }
}