package com.rh_systems.employee_service.serviceTest;

import com.rh_systems.employee_service.Entity.Position;
import com.rh_systems.employee_service.dto.PositionDTO;
import com.rh_systems.employee_service.dto.PositionDTOGetPostPut;
import com.rh_systems.employee_service.repository.PositionRepository;
import com.rh_systems.employee_service.service.PositionService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionService positionService;

    private Position position;
    private PositionDTO positionDTO;
    private PositionDTOGetPostPut positionDTOGetPostPut;

    @BeforeEach
    void setUp() {
        // Setup test data
        position = new Position();
        position.setId(1L);
        position.setName("Manager");
        position.setDescription("Department Manager");

        positionDTO = new PositionDTO();
        positionDTO.setName("Manager");
        positionDTO.setDescription("Department Manager");

        positionDTOGetPostPut = new PositionDTOGetPostPut();
        positionDTOGetPostPut.setId(1L);
        positionDTOGetPostPut.setName("Manager");
        positionDTOGetPostPut.setDescription("Department Manager");
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Position> positions = new ArrayList<>();
        positions.add(position);
        when(positionRepository.findAll()).thenReturn(positions);

        // Act
        List<PositionDTOGetPostPut> result = positionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(position.getId(), result.get(0).getId());
        assertEquals(position.getName(), result.get(0).getName());
        assertEquals(position.getDescription(), result.get(0).getDescription());
        verify(positionRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(position.getId(), result.get().getId());
        assertEquals(position.getName(), result.get().getName());
        assertEquals(position.getDescription(), result.get().getDescription());
        verify(positionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(positionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByName_Found() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.of(position));

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.findByName("Manager");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(position.getId(), result.get().getId());
        assertEquals(position.getName(), result.get().getName());
        assertEquals(position.getDescription(), result.get().getDescription());
        verify(positionRepository, times(1)).findByName("Manager");
    }

    @Test
    void testFindByName_NotFound() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.findByName("Manager");

        // Assert
        assertFalse(result.isPresent());
        verify(positionRepository, times(1)).findByName("Manager");
    }

    @Test
    void testSave_Success() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.save(positionDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(position.getId(), result.get().getId());
        assertEquals(position.getName(), result.get().getName());
        assertEquals(position.getDescription(), result.get().getDescription());
        verify(positionRepository, times(1)).findByName(positionDTO.getName());
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void testSave_DuplicateName() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.of(position));

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.save(positionDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(positionRepository, times(1)).findByName(positionDTO.getName());
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        // Create a DTO with a different name to test the duplicate check
        PositionDTO updateDTO = new PositionDTO();
        updateDTO.setName("Updated Manager");
        updateDTO.setDescription("Updated Description");

        // Mock the check for duplicate name
        when(positionRepository.findByName("Updated Manager")).thenReturn(Optional.empty());

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.update(1L, updateDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(position.getId(), result.get().getId());
        assertEquals(position.getName(), result.get().getName());
        assertEquals(position.getDescription(), result.get().getDescription());
        verify(positionRepository, times(1)).findById(1L);
        verify(positionRepository, times(1)).findByName("Updated Manager");
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.update(1L, positionDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(positionRepository, times(1)).findById(1L);
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void testUpdate_DuplicateName() {
        // Arrange
        Position existingPosition = new Position();
        existingPosition.setId(1L);
        existingPosition.setName("Manager");
        existingPosition.setDescription("Department Manager");

        Position duplicatePosition = new Position();
        duplicatePosition.setId(2L);
        duplicatePosition.setName("Director");
        duplicatePosition.setDescription("Department Director");

        PositionDTO updateDTO = new PositionDTO();
        updateDTO.setName("Director");
        updateDTO.setDescription("Updated Description");

        when(positionRepository.findById(1L)).thenReturn(Optional.of(existingPosition));
        when(positionRepository.findByName("Director")).thenReturn(Optional.of(duplicatePosition));

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.update(1L, updateDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(positionRepository, times(1)).findById(1L);
        verify(positionRepository, times(1)).findByName("Director");
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void testUpdateByName_Success() {
        // Arrange
        when(positionRepository.findByName("Manager")).thenReturn(Optional.of(position));
        when(positionRepository.findByName("Updated Manager")).thenReturn(Optional.empty());
        when(positionRepository.save(any(Position.class))).thenReturn(position);

        PositionDTO updateDTO = new PositionDTO();
        updateDTO.setName("Updated Manager");
        updateDTO.setDescription("Updated Description");

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.updateByName("Manager", updateDTO);

        // Assert
        assertTrue(result.isPresent());
        verify(positionRepository, times(1)).findByName("Manager");
        verify(positionRepository, times(1)).findByName("Updated Manager");
        verify(positionRepository, times(1)).save(any(Position.class));
    }

    @Test
    void testUpdateByName_NotFound() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<PositionDTOGetPostPut> result = positionService.updateByName("Manager", positionDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(positionRepository, times(1)).findByName("Manager");
        verify(positionRepository, never()).save(any(Position.class));
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.of(position));
        doNothing().when(positionRepository).deleteById(anyLong());

        // Act
        boolean result = positionService.deleteById(1L);

        // Assert
        assertTrue(result);
        verify(positionRepository, times(1)).findById(1L);
        verify(positionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        when(positionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = positionService.deleteById(1L);

        // Assert
        assertFalse(result);
        verify(positionRepository, times(1)).findById(1L);
        verify(positionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteByName_Success() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.of(position));
        doNothing().when(positionRepository).deleteByName(anyString());

        // Act
        boolean result = positionService.deleteByName("Manager");

        // Assert
        assertTrue(result);
        verify(positionRepository, times(1)).findByName("Manager");
        verify(positionRepository, times(1)).deleteByName("Manager");
    }

    @Test
    void testDeleteByName_NotFound() {
        // Arrange
        when(positionRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        boolean result = positionService.deleteByName("Manager");

        // Assert
        assertFalse(result);
        verify(positionRepository, times(1)).findByName("Manager");
        verify(positionRepository, never()).deleteByName(anyString());
    }
}
