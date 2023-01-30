package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.TimeTable;
import ru.edu.entity.dto.TimeTableDto;

@Mapper(componentModel = "spring")
public interface TimeTableMapper {
    TimeTableDto toDTO(TimeTable model);
    TimeTable toModel(TimeTableDto dto);
}
