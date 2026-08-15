package com.quickbite.fooddelivery.auth.api.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private UUID userId;

    private String email;

    private String status;
}
