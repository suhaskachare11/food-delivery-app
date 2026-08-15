package com.quickbite.fooddelivery.auth.service;

import com.quickbite.fooddelivery.auth.api.request.RegisterRequest;
import com.quickbite.fooddelivery.auth.api.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}