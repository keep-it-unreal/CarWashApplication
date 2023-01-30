package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.TimeTable;
import ru.edu.entity.dto.TimeTableDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = TimeTableMapperManual.class)
public interface TimeTableListMapper {
    List<TimeTable> toModelList(List<TimeTableDto> dtos);
    List<TimeTableDto> toDtoList(List<TimeTable> model);
}
