package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.CarWash;
import ru.edu.entity.dto.CarWashForUserDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = CarWashForUserMapper.class)
public interface CarWashListMapper {
    List<CarWash> toModelList(List<CarWashForUserDto> dtos);
    List<CarWashForUserDto> toDtoList(List<CarWash> model);
}
