package com.quickbite.fooddelivery.restaraunt.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.quickbite.fooddelivery.restaraunt.dto.RestarauntDto;
import com.quickbite.fooddelivery.restaraunt.entity.RestarauntEntity;
import com.quickbite.fooddelivery.restaraunt.enums.RestaurantStatus;
import com.quickbite.fooddelivery.restaraunt.mapper.RestarauntMapper;
import com.quickbite.fooddelivery.restaraunt.repository.RestarauntRepository;

@Service
public class RestarauntServiceImpl implements RestarauntService {
    private final RestarauntRepository restarauntRepository;
    private final RestarauntMapper restarauntMapper;

    RestarauntServiceImpl(RestarauntRepository restarauntRepository,
            RestarauntMapper restarauntMapper) {
        this.restarauntRepository = restarauntRepository;
        this.restarauntMapper = restarauntMapper;
    }

    @Override
    public void createRestaraunt(RestarauntDto restaraunt) {
        RestarauntEntity restarauntEntity = restarauntMapper.toEntity(restaraunt);
        restarauntEntity.setRestarauntStatus(RestaurantStatus.PENDING_APPROVAL);
        restarauntRepository.save(restarauntEntity);

    }

    @Override
    public RestarauntDto getRestarauntById(UUID id) {
        RestarauntEntity restarauntEntity = restarauntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        return restarauntMapper.toDto(restarauntEntity);
    }
}
