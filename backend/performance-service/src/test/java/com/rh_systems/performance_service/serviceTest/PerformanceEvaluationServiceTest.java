package com.rh_systems.performance_service.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rh_systems.performance_service.Entity.PerformanceEvaluation;
import com.rh_systems.performance_service.client.EmployeeClient;
import com.rh_systems.performance_service.dto.EmployeeDTO;
import com.rh_systems.performance_service.dto.EvaluationRequest;
import com.rh_systems.performance_service.repository.PerformanceEvaluationRepository;
import com.rh_systems.performance_service.service.PerformanceEvaluationService;

@ExtendWith(MockitoExtension.class)
class PerformanceEvaluationServiceTest {

    @Mock
    private EmployeeClient employeeClient;

    @Mock
    private PerformanceEvaluationRepository repository;

    @InjectMocks
    private PerformanceEvaluationService service;

    @Test
    void shouldCreateEvaluationWithEmployeeData() {
        // Arrange
        EmployeeDTO mockEmployee = new EmployeeDTO(1L, "12345678", "Alice", "Smith", "alice@example.com", 
                                                  "123456789", "123 Main St", "Developer", "IT");
        when(employeeClient.getEmployeeById(1L)).thenReturn(mockEmployee);

        EvaluationRequest request = new EvaluationRequest(95);
        
        // Act
        service.createEvaluation(1L, request);

        // Assert
        verify(repository).save(any(PerformanceEvaluation.class));
    }
}