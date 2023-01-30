package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.CarWash;
import ru.edu.entity.dto.CarWashForUserDto;

@Mapper(componentModel = "spring")
public interface CarWashForUserMapper {
    CarWashForUserDto toDTO(CarWash model);
    //CarWash toModel(CarWashForUserDto dto);
}
