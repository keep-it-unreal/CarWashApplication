package ru.edu.entity.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.edu.entity.CarWash;
import ru.edu.entity.dto.CarWashForOwnerDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarWashListMapperManual {

    @Autowired
    private CarWashForOwnerMapper carWashForOwnerMapper;

    public List<CarWash> toModelList(List<CarWashForOwnerDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<CarWash> list = new ArrayList<CarWash>( dtos.size() );
        for ( CarWashForOwnerDto carWashForOwnerDto : dtos ) {
            list.add( carWashForUserDtoToCarWash(carWashForOwnerDto) );
        }

        return list;
    }

    public List<CarWashForOwnerDto> toDtoList(List<CarWash> model) {
        if ( model == null ) {
            return null;
        }

        List<CarWashForOwnerDto> list = new ArrayList<CarWashForOwnerDto>( model.size() );
        CarWashForOwnerDto dto;
        for ( CarWash carWash : model ) {
            dto = carWashForOwnerMapper.toDTO( carWash );
            list.add( dto );
        }

        return list;
    }

    protected CarWash carWashForUserDtoToCarWash(CarWashForOwnerDto carWashForOwnerDto) {
        if ( carWashForOwnerDto == null ) {
            return null;
        }

        CarWash carWash = new CarWash();

        carWash.setId( carWashForOwnerDto.getId() );
        carWash.setAddress( carWashForOwnerDto.getAddress() );
        carWash.setPrice( carWashForOwnerDto.getPrice() );

        return carWash;
    }
}

