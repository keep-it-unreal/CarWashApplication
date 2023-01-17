package ru.edu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CarWash implements Serializable {

    @Id
    @Column(name = "id_car_wash")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String address;

    private double latitude;
    private double longitude;
    private String dailyStartTime;
    private String dailyEndTime;

    private double price;

    //private Long idOwner;
    //private Long idCity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_owner", foreignKey = @ForeignKey(name = "FK_CAR_WASH_USER"))
    @JsonIgnore
    private UserInfo ownerInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_city", foreignKey = @ForeignKey(name = "FK_CAR_WASH_CITY"))
    //@JsonIgnore
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_car_wash", referencedColumnName = "id_car_wash")
    @JsonIgnore
    private WorkShedule workShedule;

    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carWash")
    @JsonIgnore
    private List<TimeTable> timeTableList;
     */

}
