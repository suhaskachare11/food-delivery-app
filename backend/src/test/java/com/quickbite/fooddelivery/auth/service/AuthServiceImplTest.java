package com.quickbite.fooddelivery.auth.service;

import com.quickbite.fooddelivery.auth.api.request.RegisterRequest;
import com.quickbite.fooddelivery.auth.api.response.RegisterResponse;
import com.quickbite.fooddelivery.common.exception.EmailAlreadyExistsException;
import com.quickbite.fooddelivery.common.exception.PasswordMismatchException;
import com.quickbite.fooddelivery.common.exception.PhoneAlreadyExistsException;
import com.quickbite.fooddelivery.user.entity.User;
import com.quickbite.fooddelivery.user.enums.UserStatus;
import com.quickbite.fooddelivery.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {

        request = new RegisterRequest();

        request.setFirstName("Suhas");
        request.setLastName("Kachare");
        request.setEmail("suhas@example.com");
        request.setPhoneNumber("+919876543210");
        request.setPassword("Password@123");
        request.setConfirmPassword("Password@123");
    }

    @Test
    void registerUser_success() {

        // Arrange
        when(userRepository.existsByEmail("suhas@example.com"))
                .thenReturn(false);

        when(userRepository.existsByPhoneNumber("+919876543210"))
                .thenReturn(false);

        when(passwordEncoder.encode("Password@123"))
                .thenReturn("hashed-password");

        UUID userId = UUID.randomUUID();

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {

                    User user = invocation.getArgument(0);
                    user.setId(userId);

                    return user;
                });

        // Act
        RegisterResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals("suhas@example.com", response.getEmail());
        assertEquals(
                UserStatus.PENDING_VERIFICATION.name(),
                response.getStatus());

        verify(userRepository)
                .existsByEmail("suhas@example.com");

        verify(userRepository)
                .existsByPhoneNumber("+919876543210");

        verify(passwordEncoder)
                .encode("Password@123");

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void registerUser_passwordIsHashed() {

        // Arrange
        when(userRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(userRepository.existsByPhoneNumber(anyString()))
                .thenReturn(false);

        when(passwordEncoder.encode("Password@123"))
                .thenReturn("hashed-password");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(
                "hashed-password",
                savedUser.getPasswordHash());

        assertNotEquals(
                "Password@123",
                savedUser.getPasswordHash());
    }

    @Test
    void registerUser_duplicateEmail() {

        // Arrange
        when(userRepository.existsByEmail("suhas@example.com"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(request));

        verify(userRepository)
                .existsByEmail("suhas@example.com");

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void registerUser_duplicatePhone() {

        // Arrange
        when(userRepository.existsByEmail("suhas@example.com"))
                .thenReturn(false);

        when(userRepository.existsByPhoneNumber("+919876543210"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                PhoneAlreadyExistsException.class,
                () -> authService.register(request));

        verify(userRepository)
                .existsByEmail("suhas@example.com");

        verify(userRepository)
                .existsByPhoneNumber("+919876543210");

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void registerUser_passwordMismatch() {

        // Arrange
        request.setConfirmPassword("Different@123");

        // Act & Assert
        assertThrows(
                PasswordMismatchException.class,
                () -> authService.register(request));

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }
}
