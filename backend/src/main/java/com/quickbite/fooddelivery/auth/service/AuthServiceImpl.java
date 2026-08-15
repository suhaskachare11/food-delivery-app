package com.quickbite.fooddelivery.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quickbite.fooddelivery.auth.api.request.RegisterRequest;
import com.quickbite.fooddelivery.auth.api.response.RegisterResponse;
import com.quickbite.fooddelivery.common.exception.EmailAlreadyExistsException;
import com.quickbite.fooddelivery.common.exception.PasswordMismatchException;
import com.quickbite.fooddelivery.common.exception.PhoneAlreadyExistsException;
import com.quickbite.fooddelivery.user.entity.User;
import com.quickbite.fooddelivery.user.enums.UserStatus;
import com.quickbite.fooddelivery.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }

        String email = request.getEmail().trim().toLowerCase();

        String phoneNumber = request.getPhoneNumber().trim();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(email)
                .phoneNumber(phoneNumber)
                .passwordHash(encodedPassword)
                .status(UserStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .phoneVerified(false)
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .status(savedUser.getStatus().name())
                .build();

    }

}
