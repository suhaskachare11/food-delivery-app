package com.quickbite.fooddelivery.restaraunt.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.quickbite.fooddelivery.restaraunt.enums.RestaurantStatus;

import lombok.Data;

@Data
public class RestarauntDto {
    private UUID id;

    private UUID ownerId;

    private String name;

    private String description;

    private BigDecimal ratings;

    private int totalRatings;

    private RestaurantStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private String logoUrl;

    private String phoneNumber;
}
