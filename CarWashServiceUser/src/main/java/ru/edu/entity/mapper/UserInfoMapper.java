package ru.edu.entity.mapper;

import org.mapstruct.Mapper;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.UserInfoDto;

@Mapper(componentModel = "spring", uses = {CityMapperManual.class})
public interface UserInfoMapper {
    UserInfoDto toDTO(UserInfo model);
    UserInfo toModel(UserInfoDto dto);
}
