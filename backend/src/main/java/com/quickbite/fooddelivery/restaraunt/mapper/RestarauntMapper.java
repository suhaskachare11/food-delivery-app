package com.quickbite.fooddelivery.restaraunt.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.quickbite.fooddelivery.restaraunt.dto.RestarauntDto;
import com.quickbite.fooddelivery.restaraunt.entity.RestarauntEntity;

@Mapper(componentModel = "spring")
public interface RestarauntMapper {

    @Mapping(source = "restarauntStatus", target = "status")

    RestarauntDto toDto(RestarauntEntity entity);

    RestarauntEntity toEntity(RestarauntDto dto);
}
