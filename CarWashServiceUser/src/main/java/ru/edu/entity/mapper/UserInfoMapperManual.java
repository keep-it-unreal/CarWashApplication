package ru.edu.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.UserInfoDto;

@Service
@RequiredArgsConstructor
public class UserInfoMapperManual {

    private final CityMapperManual cityMapperManual;

    public UserInfoDto toDTO(UserInfo model) {
        if ( model == null ) {
            return null;
        }

        UserInfoDto userInfoDTO = new UserInfoDto();

        userInfoDTO.setId( model.getId() );
        userInfoDTO.setName( model.getName() );
        userInfoDTO.setPhone( model.getPhone() );
        userInfoDTO.setAddressMail( model.getAddressMail() );
        userInfoDTO.setPassword( model.getPassword() );
        userInfoDTO.setRole( model.getRole() );

        userInfoDTO.setIdCity(model.getCity().getId());

        return userInfoDTO;
    }

    public UserInfo toModel(UserInfoDto dto) {
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

        userInfo.setCity(City.builder().id(dto.getIdCity()).build());

        return userInfo;
    }
}

