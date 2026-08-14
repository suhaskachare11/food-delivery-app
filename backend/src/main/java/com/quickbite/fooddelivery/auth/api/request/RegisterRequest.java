package com.quickbite.fooddelivery.auth.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(
        message = "First name is required"
    )
    @Size(
        min = 2,
        max = 50,
        message = "First name must be between 2 and 50 characters"
    )
    private String firstName;

    @NotBlank(
        message = "Last name is required"
    )
    @Size(
        min = 2,
        max = 50,
        message = "Last name must be between 2 and 50 characters"
    )
    private String lastName;

    @NotBlank(
        message = "Email is required"
    )
    @Email(
        message = "Email should be valid"
    )
    private String email;

    @NotBlank(
        message = "Phone number is required"
    )
    private String phoneNumber;

    @NotBlank(
        message = "Password is required"
    )
    @Size(
        min = 8,
        max = 128,
        message = "Password must be at least 8 characters long"
    )
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
        message = "Password is too weak"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

}
