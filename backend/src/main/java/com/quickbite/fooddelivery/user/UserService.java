package com.quickbite.fooddelivery.user;

import com.quickbite.fooddelivery.user.dto.UserRequest;
import com.quickbite.fooddelivery.user.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered: " + request.email());
        }

        User user = User.builder()
            .name(request.name())
            .email(request.email())
            .build();

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail(), saved.getCreatedAt());
    }

    public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
        .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt()))
        .toList();
    }
}