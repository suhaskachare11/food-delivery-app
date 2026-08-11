package com.quickbite.fooddelivery.restaraunt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.quickbite.fooddelivery.restaraunt.entity.RestarauntEntity;

@Repository
public interface RestarauntRepository extends JpaRepository<RestarauntEntity, UUID> {

}
