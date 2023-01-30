package ru.edu.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.entity.dto.CityDto;
import ru.edu.service.CityService;

@Service
@RequiredArgsConstructor
public class CityMapperManual {

    @Autowired
    CityService service;

    public CityDto toDTO(City model) {
        if ( model == null ) {
            return null;
        }

        CityDto cityDTO = new CityDto();

        cityDTO.setId( model.getId() );

        return cityDTO;
    }

    public City toModel(CityDto dto) {
        if ( dto == null ) {
            return null;
        }

        City city = new City();

        city.setId( dto.getId() );
        city.setName(service.findById(dto.getId()).getName());

        return city;
    }
}
