package com.rh_systems.employee_service.serviceTest;

import com.rh_systems.employee_service.Entity.StatusPermission;
import com.rh_systems.employee_service.dto.StatusPermissionDTOGetPostPut;
import com.rh_systems.employee_service.repository.StatusPermissionRepository;
import com.rh_systems.employee_service.service.StatusPermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusPermissionServiceTest {

    @Mock
    private StatusPermissionRepository statusPermissionRepository;

    @InjectMocks
    private StatusPermissionService statusPermissionService;

    private StatusPermission statusPermission;
    private StatusPermissionDTOGetPostPut statusPermissionDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        statusPermission = new StatusPermission();
        statusPermission.setId(1L);
        statusPermission.setName("VACATION");
        statusPermission.setDescription("Vacation permission");

        statusPermissionDTO = new StatusPermissionDTOGetPostPut();
        statusPermissionDTO.setId(1L);
        statusPermissionDTO.setName("VACATION");
        statusPermissionDTO.setDescription("Vacation permission");
    }

    @Test
    void testFindAll() {
        // Arrange
        List<StatusPermission> permissions = new ArrayList<>();
        permissions.add(statusPermission);
        when(statusPermissionRepository.findAll()).thenReturn(permissions);

        // Act
        List<StatusPermissionDTOGetPostPut> result = statusPermissionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statusPermission.getId(), result.get(0).getId());
        assertEquals(statusPermission.getName(), result.get(0).getName());
        assertEquals(statusPermission.getDescription(), result.get(0).getDescription());
        verify(statusPermissionRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        // Arrange
        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.of(statusPermission));

        // Act
        Optional<StatusPermissionDTOGetPostPut> result = statusPermissionService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(statusPermission.getId(), result.get().getId());
        assertEquals(statusPermission.getName(), result.get().getName());
        assertEquals(statusPermission.getDescription(), result.get().getDescription());
        verify(statusPermissionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusPermissionDTOGetPostPut> result = statusPermissionService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(statusPermissionRepository, times(1)).findById(1L);
    }

    @Test
    void testSave_Success() {
        // Arrange
        StatusPermissionDTOGetPostPut inputDTO = new StatusPermissionDTOGetPostPut();
        inputDTO.setName("VACATION");
        inputDTO.setDescription("Vacation permission");

        when(statusPermissionRepository.save(any(StatusPermission.class))).thenReturn(statusPermission);

        // Act
        Optional<StatusPermissionDTOGetPostPut> result = statusPermissionService.save(inputDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(statusPermission.getId(), result.get().getId());
        assertEquals(statusPermission.getName(), result.get().getName());
        assertEquals(statusPermission.getDescription(), result.get().getDescription());
        verify(statusPermissionRepository, times(1)).save(any(StatusPermission.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        StatusPermissionDTOGetPostPut updateDTO = new StatusPermissionDTOGetPostPut();
        updateDTO.setName("SICK_LEAVE");
        updateDTO.setDescription("Sick leave permission");

        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.of(statusPermission));
        when(statusPermissionRepository.save(any(StatusPermission.class))).thenReturn(statusPermission);

        // Act
        Optional<StatusPermissionDTOGetPostPut> result = statusPermissionService.update(1L, updateDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(statusPermission.getId(), result.get().getId());
        verify(statusPermissionRepository, times(1)).findById(1L);
        verify(statusPermissionRepository, times(1)).save(any(StatusPermission.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        StatusPermissionDTOGetPostPut updateDTO = new StatusPermissionDTOGetPostPut();
        updateDTO.setName("SICK_LEAVE");
        updateDTO.setDescription("Sick leave permission");

        when(statusPermissionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<StatusPermissionDTOGetPostPut> result = statusPermissionService.update(1L, updateDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(statusPermissionRepository, times(1)).findById(1L);
        verify(statusPermissionRepository, never()).save(any(StatusPermission.class));
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(statusPermissionRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(statusPermissionRepository).deleteById(anyLong());

        // Act
        boolean result = statusPermissionService.deleteById(1L);

        // Assert
        assertTrue(result);
        verify(statusPermissionRepository, times(1)).existsById(1L);
        verify(statusPermissionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        when(statusPermissionRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean result = statusPermissionService.deleteById(1L);

        // Assert
        assertFalse(result);
        verify(statusPermissionRepository, times(1)).existsById(1L);
        verify(statusPermissionRepository, never()).deleteById(anyLong());
    }
}