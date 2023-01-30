package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.CarWash;
import ru.edu.entity.dto.CarWashForOwnerDto;

@Mapper(componentModel = "spring")
public interface CarWashForOwnerMapper {
    CarWashForOwnerDto toDTO(CarWash model);
    CarWash toModel(CarWashForOwnerDto dto);
}
