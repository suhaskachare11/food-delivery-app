package com.quickbite.fooddelivery.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickbite.fooddelivery.auth.api.request.RegisterRequest;
import com.quickbite.fooddelivery.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("food_delivery_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureDatabase(
            DynamicPropertyRegistry registry) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl);

        registry.add(
                "spring.datasource.username",
                postgres::getUsername);

        registry.add(
                "spring.datasource.password",
                postgres::getPassword);

        registry.add(
                "spring.datasource.driver-class-name",
                postgres::getDriverClassName);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_success() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Suhas");
        request.setLastName("Kachare");
        request.setEmail("suhas@example.com");
        request.setPhoneNumber("+919876543210");
        request.setPassword("Password@123");
        request.setConfirmPassword("Password@123");

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.email")
                        .value("suhas@example.com"))
                .andExpect(jsonPath("$.status")
                        .value("PENDING_VERIFICATION"));
    }

    @Test
    void registerUser_shouldPersistUser() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPhoneNumber("+919876543211");
        request.setPassword("Password@123");
        request.setConfirmPassword("Password@123");

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        var savedUser = userRepository.findByEmail("john@example.com");

        assert savedUser.isPresent();

        var user = savedUser.get();

        org.junit.jupiter.api.Assertions.assertEquals(
                "John",
                user.getFirstName());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Doe",
                user.getLastName());

        org.junit.jupiter.api.Assertions.assertEquals(
                "PENDING_VERIFICATION",
                user.getStatus().name());

        org.junit.jupiter.api.Assertions.assertFalse(
                user.isEmailVerified());

        org.junit.jupiter.api.Assertions.assertFalse(
                user.isPhoneVerified());

        org.junit.jupiter.api.Assertions.assertNotEquals(
                "Password@123",
                user.getPasswordHash());
    }

    @Test
    void registerUser_invalidRequest_shouldReturn400()
            throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setFirstName("");
        request.setLastName("");
        request.setEmail("invalid-email");
        request.setPhoneNumber("");
        request.setPassword("123");
        request.setConfirmPassword("123");

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void registerUser_duplicateEmail_shouldReturn409()
            throws Exception {

        RegisterRequest firstRequest = new RegisterRequest();

        firstRequest.setFirstName("Suhas");
        firstRequest.setLastName("Kachare");
        firstRequest.setEmail("duplicate@example.com");
        firstRequest.setPhoneNumber("+919876543212");
        firstRequest.setPassword("Password@123");
        firstRequest.setConfirmPassword("Password@123");

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isCreated());

        RegisterRequest secondRequest = new RegisterRequest();

        secondRequest.setFirstName("John");
        secondRequest.setLastName("Doe");
        secondRequest.setEmail("duplicate@example.com");
        secondRequest.setPhoneNumber("+919876543213");
        secondRequest.setPassword("Password@123");
        secondRequest.setConfirmPassword("Password@123");

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code")
                        .value("EMAIL_ALREADY_EXISTS"));
    }
}
