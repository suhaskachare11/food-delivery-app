package com.quickbite.fooddelivery.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quickbite.fooddelivery.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);
    
}