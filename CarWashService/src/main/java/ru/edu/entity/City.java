package ru.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.dto.CityDTO;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City implements Serializable {

    @Id
    @Column(name = "id_city")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "city")
    //private List<UserInfo> userInfoList;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "city")
    //private List<CarWash> carWashesList;

    public City(CityDTO cityDTO){
        this.id = cityDTO.getId();
        this.name = cityDTO.getName();
    }
}
