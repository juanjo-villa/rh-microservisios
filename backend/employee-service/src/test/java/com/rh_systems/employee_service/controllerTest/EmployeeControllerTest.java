package com.rh_systems.employee_service.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh_systems.employee_service.controller.EmployeeController;
import com.rh_systems.employee_service.dto.EmployeeDTO;
import com.rh_systems.employee_service.dto.EmployeeDTOGetPostPut;
import com.rh_systems.employee_service.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindAll() throws Exception {
        // Arrange
        List<EmployeeDTOGetPostPut> employees = new ArrayList<>();
        EmployeeDTOGetPostPut employee = new EmployeeDTOGetPostPut();
        employee.setId(1L);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employees.add(employee);

        when(employeeService.findAll()).thenReturn(employees);

        // Act & Assert
        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById_Success() throws Exception {
        // Arrange
        Long employeeId = 1L;
        EmployeeDTOGetPostPut employee = new EmployeeDTOGetPostPut();
        employee.setId(employeeId);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act & Assert
        mockMvc.perform(get("/api/employee/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById_NotFound() throws Exception {
        // Arrange
        Long employeeId = 1L;
        when(employeeService.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/employee/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByDni_Success() throws Exception {
        // Arrange
        String dni = "12345678A";
        EmployeeDTOGetPostPut employee = new EmployeeDTOGetPostPut();
        employee.setId(1L);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDni(dni);

        when(employeeService.findByDni(dni)).thenReturn(Optional.of(employee));

        // Act & Assert
        mockMvc.perform(get("/api/employee/dni/{dni}", dni))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByDni_NotFound() throws Exception {
        // Arrange
        String dni = "12345678A";
        when(employeeService.findByDni(dni)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/employee/dni/{dni}", dni))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByEmail_Success() throws Exception {
        // Arrange
        String email = "john.doe@example.com";
        EmployeeDTOGetPostPut employee = new EmployeeDTOGetPostPut();
        employee.setId(1L);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail(email);

        when(employeeService.findByEmail(email)).thenReturn(Optional.of(employee));

        // Act & Assert
        mockMvc.perform(get("/api/employee/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByEmail_NotFound() throws Exception {
        // Arrange
        String email = "john.doe@example.com";
        when(employeeService.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/employee/email/{email}", email))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByPhone_Success() throws Exception {
        // Arrange
        String phone = "123456789";
        EmployeeDTOGetPostPut employee = new EmployeeDTOGetPostPut();
        employee.setId(1L);
        employee.setName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhone(phone);

        when(employeeService.findByPhone(phone)).thenReturn(Optional.of(employee));

        // Act & Assert
        mockMvc.perform(get("/api/employee/phone/{phone}", phone))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByPhone_NotFound() throws Exception {
        // Arrange
        String phone = "123456789";
        when(employeeService.findByPhone(phone)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/employee/phone/{phone}", phone))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSave_Success() throws Exception {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        EmployeeDTOGetPostPut savedEmployee = new EmployeeDTOGetPostPut();
        savedEmployee.setId(1L);
        savedEmployee.setName("John");
        savedEmployee.setLastName("Doe");
        savedEmployee.setEmail("john.doe@example.com");
        savedEmployee.setDni("12345678A");
        savedEmployee.setPhone("123456789");
        savedEmployee.setAddress("123 Main St");

        when(employeeService.save(any(EmployeeDTO.class))).thenReturn(Optional.of(savedEmployee));

        // Act & Assert
        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSave_BadRequest() throws Exception {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("123456789");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        when(employeeService.save(any(EmployeeDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateById_Success() throws Exception {
        // Arrange
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        EmployeeDTOGetPostPut updatedEmployee = new EmployeeDTOGetPostPut();
        updatedEmployee.setId(employeeId);
        updatedEmployee.setName("John");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");
        updatedEmployee.setDni("12345678A");
        updatedEmployee.setPhone("123456789");
        updatedEmployee.setAddress("123 Main St");

        when(employeeService.updateById(anyLong(), any(EmployeeDTO.class))).thenReturn(Optional.of(updatedEmployee));

        // Act & Assert
        mockMvc.perform(put("/api/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateById_NotFound() throws Exception {
        // Arrange
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        when(employeeService.updateById(anyLong(), any(EmployeeDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateByDni_Success() throws Exception {
        // Arrange
        String dni = "12345678A";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        EmployeeDTOGetPostPut updatedEmployee = new EmployeeDTOGetPostPut();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("John");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");
        updatedEmployee.setDni("12345678A");
        updatedEmployee.setPhone("123456789");
        updatedEmployee.setAddress("123 Main St");

        when(employeeService.updateByDni(anyString(), any(EmployeeDTO.class))).thenReturn(Optional.of(updatedEmployee));

        // Act & Assert
        mockMvc.perform(put("/api/employee/dni/{dni}", dni)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateByDni_NotFound() throws Exception {
        // Arrange
        String dni = "12345678A";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        when(employeeService.updateByDni(anyString(), any(EmployeeDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/employee/dni/{dni}", dni)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateByEmail_Success() throws Exception {
        // Arrange
        String email = "john.doe@example.com";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        EmployeeDTOGetPostPut updatedEmployee = new EmployeeDTOGetPostPut();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("John");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");
        updatedEmployee.setDni("12345678A");
        updatedEmployee.setPhone("123456789");
        updatedEmployee.setAddress("123 Main St");

        when(employeeService.updateByEmail(anyString(), any(EmployeeDTO.class))).thenReturn(Optional.of(updatedEmployee));

        // Act & Assert
        mockMvc.perform(put("/api/employee/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateByEmail_NotFound() throws Exception {
        // Arrange
        String email = "john.doe@example.com";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        when(employeeService.updateByEmail(anyString(), any(EmployeeDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/employee/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateByPhone_Success() throws Exception {
        // Arrange
        String phone = "123456789";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        EmployeeDTOGetPostPut updatedEmployee = new EmployeeDTOGetPostPut();
        updatedEmployee.setId(1L);
        updatedEmployee.setName("John");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("john.doe@example.com");
        updatedEmployee.setDni("12345678A");
        updatedEmployee.setPhone("123456789");
        updatedEmployee.setAddress("123 Main St");

        when(employeeService.updateByPhone(anyString(), any(EmployeeDTO.class))).thenReturn(Optional.of(updatedEmployee));

        // Act & Assert
        mockMvc.perform(put("/api/employee/phone/{phone}", phone)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateByPhone_NotFound() throws Exception {
        // Arrange
        String phone = "123456789";
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDni("12345678A");
        employeeDTO.setPhone("123456789");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPassword("password");
        employeeDTO.setPositionId(1L);

        when(employeeService.updateByPhone(anyString(), any(EmployeeDTO.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/employee/phone/{phone}", phone)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteById_Success() throws Exception {
        // Arrange
        Long employeeId = 1L;
        when(employeeService.deleteById(employeeId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/employee/{id}", employeeId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteById_NotFound() throws Exception {
        // Arrange
        Long employeeId = 1L;
        when(employeeService.deleteById(employeeId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/employee/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteByDni_Success() throws Exception {
        // Arrange
        String dni = "12345678A";
        when(employeeService.deleteByDni(dni)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/employee/dni/{dni}", dni))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteByDni_NotFound() throws Exception {
        // Arrange
        String dni = "12345678A";
        when(employeeService.deleteByDni(dni)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/employee/dni/{dni}", dni))
                .andExpect(status().isNotFound());
    }
}
