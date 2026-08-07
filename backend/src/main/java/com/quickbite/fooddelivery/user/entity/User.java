package com.quickbite.fooddelivery.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.quickbite.fooddelivery.user.enums.UserStatus;

@Entity
@Table(
    name = "users", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_user_phone_number", columnNames = "phone_number")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false,length = 50)
   private String firstName;

   @Column(nullable = false,length = 50)
   private String lastName;

   @Column(nullable = false, length = 255)
   private String email;

   @Column(name = "phone_number", nullable = false, length = 20)
   private String phoneNumber;

   @Column(nullable = false)
   private String password;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private UserStatus status;

   @Column(nullable = false)
   private boolean emailVerified;

   @Column(nullable = false)
    private boolean phoneVerified;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}