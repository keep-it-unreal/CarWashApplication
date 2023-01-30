package ru.edu.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.UserInfoDto;
import ru.edu.service.CityService;

@Service
@RequiredArgsConstructor
public class UserInfoMapperManual {

    private final CityService cityService;

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

        UserInfo model = new UserInfo();

        model.setId( dto.getId() );
        model.setName( dto.getName() );
        model.setPhone( dto.getPhone() );
        model.setAddressMail( dto.getAddressMail() );
        model.setPassword( dto.getPassword() );
        model.setRole( dto.getRole() );

        if (dto.getIdCity() != null) {
            model.setCity(cityService.findById(dto.getIdCity()));
        }

        return model;
    }
}

