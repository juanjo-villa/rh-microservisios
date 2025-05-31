package com.rh_systems.employee_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private EmployeePrincipal employeePrincipal;

    private final String jwtSecret = "testJwtSecretKeyForUnitTestingPurposesOnlyDoNotUseInProduction";
    private final int jwtExpirationInMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", jwtExpirationInMs);
    }

    @Test
    void testGenerateToken_Success() {
        // Arrange
        String email = "test@example.com";
        when(authentication.getPrincipal()).thenReturn(employeePrincipal);
        when(employeePrincipal.getUsername()).thenReturn(email);

        // Act
        String token = jwtTokenProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateToken_NullAuthentication() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.generateToken(null));
    }

    @Test
    void testGetUsernameFromJWT_Success() {
        // Arrange
        String email = "test@example.com";
        when(authentication.getPrincipal()).thenReturn(employeePrincipal);
        when(employeePrincipal.getUsername()).thenReturn(email);
        String token = jwtTokenProvider.generateToken(authentication);

        // Act
        String extractedEmail = jwtTokenProvider.getUsernameFromJWT(token);

        // Assert
        assertEquals(email, extractedEmail);
    }

    @Test
    void testGetUsernameFromJWT_NullToken() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getUsernameFromJWT(null));
    }

    @Test
    void testGetUsernameFromJWT_EmptyToken() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getUsernameFromJWT(""));
    }

    @Test
    void testValidateToken_ValidToken() {
        // Arrange
        String email = "test@example.com";
        when(authentication.getPrincipal()).thenReturn(employeePrincipal);
        when(employeePrincipal.getUsername()).thenReturn(email);
        String token = jwtTokenProvider.generateToken(authentication);

        // Act
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_NullToken() {
        // Act
        boolean isValid = jwtTokenProvider.validateToken(null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_EmptyToken() {
        // Act
        boolean isValid = jwtTokenProvider.validateToken("");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        // Act
        boolean isValid = jwtTokenProvider.validateToken("invalid.token.string");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_ExpiredToken() {
        // Arrange - Create a token that's already expired
        String email = "test@example.com";
        
        // Create a token with negative expiration (already expired)
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", -10000);
        
        when(authentication.getPrincipal()).thenReturn(employeePrincipal);
        when(employeePrincipal.getUsername()).thenReturn(email);
        String expiredToken = jwtTokenProvider.generateToken(authentication);
        
        // Reset expiration for other tests
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", jwtExpirationInMs);

        // Act
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // Assert
        assertFalse(isValid);
    }
}