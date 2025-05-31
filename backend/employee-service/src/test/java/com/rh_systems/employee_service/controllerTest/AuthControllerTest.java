package com.rh_systems.employee_service.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh_systems.employee_service.controller.AuthController;
import com.rh_systems.employee_service.dto.EmployeeLoginDTO;
import com.rh_systems.employee_service.dto.JwtResponse;
import com.rh_systems.employee_service.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAuthenticateEmployee_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        String token = "test.jwt.token";

        EmployeeLoginDTO loginDTO = new EmployeeLoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void testAuthenticateEmployee_InvalidCredentials() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "wrong_password";

        EmployeeLoginDTO loginDTO = new EmployeeLoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testAuthenticateEmployee_MissingEmail() throws Exception {
        // Arrange
        EmployeeLoginDTO loginDTO = new EmployeeLoginDTO();
        loginDTO.setPassword("password");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAuthenticateEmployee_MissingPassword() throws Exception {
        // Arrange
        EmployeeLoginDTO loginDTO = new EmployeeLoginDTO();
        loginDTO.setEmail("test@example.com");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isBadRequest());
    }
}
