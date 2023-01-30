package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.CarWash;
import ru.edu.entity.dto.CarWashForOwnerDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = CarWashForOwnerMapperManual.class)
public interface CarWashListMapper {
    List<CarWash> toModelList(List<CarWashForOwnerDto> dtos);
    List<CarWashForOwnerDto> toDtoList(List<CarWash> model);
}
