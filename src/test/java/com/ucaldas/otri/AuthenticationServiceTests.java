package com.ucaldas.otri;

import com.ucaldas.otri.application.auth.models.AuthenticationRequest;
import com.ucaldas.otri.application.auth.models.AuthenticationResponse;
import com.ucaldas.otri.application.auth.models.RegisterRequest;
import com.ucaldas.otri.application.auth.services.AuthenticationService;
import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.shared.exceptions.ErrorCodes;
import com.ucaldas.otri.domain.services.IJwtService;
import com.ucaldas.otri.domain.users.entities.RoleType;
import com.ucaldas.otri.domain.users.entities.User;
import com.ucaldas.otri.domain.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IJwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                userRepository,
                passwordEncoder,
                jwtService,
                authenticationManager
        );
    }

    @Test
    void register_ShouldCreateNewUser_WhenEmailDoesNotExist() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "password123"
        );

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");

        // Act
        String result = authenticationService.register(request);

        // Assert
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("John", savedUser.getFirstname());
        assertEquals("Doe", savedUser.getLastname());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("encoded_password", savedUser.getPassword());
        assertEquals(RoleType.Admin, savedUser.getRole());
        assertEquals("Usuario registrado exitosamente", result);
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "existing@example.com",
                "password123"
        );

        when(userRepository.findByEmail(request.getEmail())).thenReturn(
                Optional.of(User.builder().email("existing@example.com").build())
        );

        // Act & Assert
        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> authenticationService.register(request)
        );

        assertEquals("User with email existing@example.com already exists", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "password123");
        User user = User.builder()
                .email("john.doe@example.com")
                .password("encoded_password")
                .firstname("John")
                .lastname("Doe")
                .role(RoleType.Admin)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("valid.jwt.token");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("valid.jwt.token", response.getToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    }

    @Test
    void authenticate_ShouldThrowException_WhenCredentialsAreInvalid() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "wrong_password");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> authenticationService.authenticate(request)
        );

        assertEquals("Usuario o contraseña inválidos", exception.getMessage());
        assertEquals(ErrorCodes.INVALID_CREDENTIALS, exception.getErrorCode());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("nonexistent@example.com", "password123");

        // No exception from authenticationManager in this case
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> authenticationService.authenticate(request)
        );

        assertEquals("No se ha encontrado el usuario", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void register_ShouldProperlyEncodePassword() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "securePassword"
        );

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("securePassword")).thenReturn("very_encoded_password");

        // Act
        authenticationService.register(request);

        // Assert
        verify(passwordEncoder).encode("securePassword");
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("very_encoded_password", savedUser.getPassword());
        assertNotEquals("securePassword", savedUser.getPassword());
    }
}
