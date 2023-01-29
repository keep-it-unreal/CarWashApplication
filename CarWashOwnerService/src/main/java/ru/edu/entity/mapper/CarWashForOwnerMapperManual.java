package ru.edu.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.CarWash;
import ru.edu.entity.City;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.CarWashForOwnerDto;
import ru.edu.service.CarWashService;
import ru.edu.service.CityService;
import ru.edu.service.UserInfoService;

@Service
@RequiredArgsConstructor
public class CarWashForOwnerMapperManual {

    private final CityService cityService;
    private final UserInfoService userInfoService;

    public CarWashForOwnerDto toDTO(CarWash model) {
        if ( model == null ) {
            return null;
        }

        CarWashForOwnerDto dto = new CarWashForOwnerDto();

        dto.setId(model.getId());
        dto.setAddress(model.getAddress());
        dto.setLatitude(model.getLatitude());
        dto.setLongitude(model.getLongitude());
        dto.setDailyStartTime(model.getDailyStartTime());
        dto.setDailyEndTime(model.getDailyEndTime());
        dto.setPrice(model.getPrice());

        dto.setIdCity(model.getCity().getId());
        dto.setIdUser(model.getUserInfo().getId());
        return dto;
    }

    public CarWash toModel(CarWashForOwnerDto dto) {
        if ( dto == null ) {
            return null;
        }

        CarWash model = new CarWash();

        if (dto.getId() != 0) {
            model.setId(dto.getId());
        }
        model.setAddress(dto.getAddress());
        model.setLatitude(dto.getLatitude());
        model.setLongitude(dto.getLongitude());
        model.setDailyStartTime(dto.getDailyStartTime());
        model.setDailyEndTime(dto.getDailyEndTime());
        model.setLongitude(dto.getLongitude());
        model.setPrice(dto.getPrice());

        if (dto.getIdCity() != 0) {
            model.setCity(cityService.findById(dto.getIdCity()));
        }
        if (dto.getIdUser() != 0) {
            model.setUserInfo(userInfoService.findById(dto.getIdUser()));
        }

        return model;
    }
}
