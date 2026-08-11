package com.quickbite.fooddelivery.restaraunt.service;

import java.util.UUID;
import com.quickbite.fooddelivery.restaraunt.dto.RestarauntDto;

public interface RestarauntService {

    public void createRestaraunt(RestarauntDto restaraunt);

    public RestarauntDto getRestarauntById(UUID id);

}
