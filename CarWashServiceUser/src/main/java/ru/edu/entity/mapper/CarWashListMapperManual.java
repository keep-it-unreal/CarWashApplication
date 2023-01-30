package ru.edu.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.edu.entity.CarWash;
import ru.edu.entity.dto.CarWashForUserDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarWashListMapperManual {

    @Autowired
    private CarWashForUserMapper carWashForUserMapper;

    public List<CarWash> toModelList(List<CarWashForUserDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<CarWash> list = new ArrayList<CarWash>( dtos.size() );
        for ( CarWashForUserDto carWashForUserDto : dtos ) {
            list.add( carWashForUserDtoToCarWash( carWashForUserDto ) );
        }

        return list;
    }

    public List<CarWashForUserDto> toDtoList(List<CarWash> model) {
        if ( model == null ) {
            return null;
        }

        List<CarWashForUserDto> list = new ArrayList<CarWashForUserDto>( model.size() );
        CarWashForUserDto dto;
        for ( CarWash carWash : model ) {
            dto = carWashForUserMapper.toDTO( carWash );
            list.add( dto );
        }

        return list;
    }

    protected CarWash carWashForUserDtoToCarWash(CarWashForUserDto carWashForUserDto) {
        if ( carWashForUserDto == null ) {
            return null;
        }

        CarWash carWash = new CarWash();

        carWash.setId( carWashForUserDto.getId() );
        carWash.setAddress( carWashForUserDto.getAddress() );
        carWash.setPrice( carWashForUserDto.getPrice() );

        return carWash;
    }
}

