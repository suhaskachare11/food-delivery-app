package com.quickbite.fooddelivery.restaraunt.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.quickbite.fooddelivery.restaraunt.enums.RestaurantStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaruants")
@Setter
@Getter
public class RestarauntEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column
    private String logoUrl;

    @Column(precision = 2, scale = 1)
    private BigDecimal ratings;

    @Column
    private Integer totalRatings;

    @Enumerated(EnumType.STRING)
    @Column
    private RestaurantStatus restarauntStatus;

    @Column
    private String email;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        Instant now = Instant.now();
        updatedAt = now;
    }


}
