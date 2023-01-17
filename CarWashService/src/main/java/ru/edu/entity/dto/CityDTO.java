package ru.edu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.City;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO implements Serializable {

    private Long id;
    private String name;

    public CityDTO(City city){
        this.id = city.getId();
        this.name = city.getName();
    }

}
