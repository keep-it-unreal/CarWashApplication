package ru.edu.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.UserInfoDto;
import ru.edu.entity.dto.UserInfoDto_with_cityDto;

@Service
@RequiredArgsConstructor
public class UserInfo_with_city_MapperManual {

    private final CityMapperManual cityMapperManual;

    public UserInfoDto_with_cityDto toDTO(UserInfo model) {
        if ( model == null ) {
            return null;
        }

        UserInfoDto_with_cityDto userInfoDTO = new UserInfoDto_with_cityDto();

        userInfoDTO.setId( model.getId() );
        userInfoDTO.setName( model.getName() );
        userInfoDTO.setPhone( model.getPhone() );
        userInfoDTO.setAddressMail( model.getAddressMail() );
        userInfoDTO.setPassword( model.getPassword() );
        userInfoDTO.setRole( model.getRole() );

        userInfoDTO.setCity(cityMapperManual.toDTO(model.getCity()));

        return userInfoDTO;
    }

    public UserInfo toModel(UserInfoDto_with_cityDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        userInfo.setId( dto.getId() );
        userInfo.setName( dto.getName() );
        userInfo.setPhone( dto.getPhone() );
        userInfo.setAddressMail( dto.getAddressMail() );
        userInfo.setPassword( dto.getPassword() );
        userInfo.setRole( dto.getRole() );

        userInfo.setCity(cityMapperManual.toModel(dto.getCity()));

        return userInfo;
    }
}

