package com.rh_systems.employee_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh_systems.employee_service.controller.PositionController;
import com.rh_systems.employee_service.dto.PositionDTO;
import com.rh_systems.employee_service.dto.PositionDTOGetPostPut;
import com.rh_systems.employee_service.service.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PositionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PositionService positionService;

    @InjectMocks
    private PositionController positionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(positionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testFindAll() throws Exception {
        // Arrange
        List<PositionDTOGetPostPut> positions = new ArrayList<>();
        PositionDTOGetPostPut position = new PositionDTOGetPostPut();
        position.setId(1L);
        position.setName("Developer");
        position.setDescription("Software Developer");
        positions.add(position);

        when(positionService.findAll()).thenReturn(positions);

        // Act & Assert
        mockMvc.perform(get("/api/position"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Developer"))
                .andExpect(jsonPath("$[0].description").value("Software Developer"));
    }

    @Test
    void testFindById_Success() throws Exception {
        // Arrange
        Long positionId = 1L;
        PositionDTOGetPostPut position = new PositionDTOGetPostPut();
        position.setId(positionId);
        position.setName("Developer");
        position.setDescription("Software Developer");

        when(positionService.findById(positionId)).thenReturn(Optional.of(position));

        // Act & Assert
        mockMvc.perform(get("/api/position/{id}", positionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Developer"))
                .andExpect(jsonPath("$.description").value("Software Developer"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        // Arrange
        Long positionId = 1L;
        when(positionService.findById(positionId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/position/{id}", positionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindByName_Success() throws Exception {
        // Arrange
        String positionName = "Developer";
        PositionDTOGetPostPut position = new PositionDTOGetPostPut();
        position.setId(1L);
        position.setName(positionName);
        position.setDescription("Software Developer");

        when(positionService.findByName(positionName)).thenReturn(Optional.of(position));

        // Act & Assert
        mockMvc.perform(get("/api/position/name/{name}", positionName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Developer"))
                .andExpect(jsonPath("$.description").value("Software Developer"));
    }

    @Test
    void testFindByName_NotFound() throws Exception {
        // Arrange
        String positionName = "Developer";
        when(positionService.findByName(positionName)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/position/name/{name}", positionName))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSave_Success() throws Exception {
        // Arrange
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Developer");
        positionDTO.setDescription("Software Developer");

        PositionDTOGetPostPut savedPosition = new PositionDTOGetPostPut();
        savedPosition.setId(1L);
        savedPosition.setName("Developer");
        savedPosition.setDescription("Software Developer");

        when(positionService.save(any(PositionDTO.class))).thenReturn(Optional.of(savedPosition));

        // Act & Assert
        mockMvc.perform(post("/api/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Developer"))
                .andExpect(jsonPath("$.description").value("Software Developer"));
    }

    @Test
    void testSave_BadRequest() throws Exception {
        // Arrange
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Developer");
        positionDTO.setDescription("Software Developer");

        when(positionService.save(any(PositionDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positionDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate_Success() throws Exception {
        // Arrange
        Long positionId = 1L;
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Senior Developer");
        positionDTO.setDescription("Senior Software Developer");

        PositionDTOGetPostPut updatedPosition = new PositionDTOGetPostPut();
        updatedPosition.setId(positionId);
        updatedPosition.setName("Senior Developer");
        updatedPosition.setDescription("Senior Software Developer");

        when(positionService.update(anyLong(), any(PositionDTO.class))).thenReturn(Optional.of(updatedPosition));

        // Act & Assert
        mockMvc.perform(put("/api/position/{id}", positionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Senior Developer"))
                .andExpect(jsonPath("$.description").value("Senior Software Developer"));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        // Arrange
        Long positionId = 1L;
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Senior Developer");
        positionDTO.setDescription("Senior Software Developer");

        when(positionService.update(anyLong(), any(PositionDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/position/{id}", positionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positionDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateByName_Success() throws Exception {
        // Arrange
        String positionName = "Developer";
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Senior Developer");
        positionDTO.setDescription("Senior Software Developer");

        PositionDTOGetPostPut updatedPosition = new PositionDTOGetPostPut();
        updatedPosition.setId(1L);
        updatedPosition.setName("Senior Developer");
        updatedPosition.setDescription("Senior Software Developer");

        when(positionService.updateByName(anyString(), any(PositionDTO.class))).thenReturn(Optional.of(updatedPosition));

        // Act & Assert
        mockMvc.perform(put("/api/position/name/{name}", positionName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Senior Developer"))
                .andExpect(jsonPath("$.description").value("Senior Software Developer"));
    }

    @Test
    void testUpdateByName_NotFound() throws Exception {
        // Arrange
        String positionName = "Developer";
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("Senior Developer");
        positionDTO.setDescription("Senior Software Developer");

        when(positionService.updateByName(anyString(), any(PositionDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/position/name/{name}", positionName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(positionDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById_Success() throws Exception {
        // Arrange
        Long positionId = 1L;
        when(positionService.deleteById(positionId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/position/{id}", positionId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById_NotFound() throws Exception {
        // Arrange
        Long positionId = 1L;
        when(positionService.deleteById(positionId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/position/{id}", positionId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteByName_Success() throws Exception {
        // Arrange
        String positionName = "Developer";
        when(positionService.deleteByName(positionName)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/position/name/{name}", positionName))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteByName_NotFound() throws Exception {
        // Arrange
        String positionName = "Developer";
        when(positionService.deleteByName(positionName)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/position/name/{name}", positionName))
                .andExpect(status().isNotFound());
    }
}