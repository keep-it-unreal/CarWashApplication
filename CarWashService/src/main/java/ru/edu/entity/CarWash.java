package ru.edu.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String address;

    private double latitude;
    private double longitude;
    private String dailyStartTime;
    private String dailyEndTime;

    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    private WorkShedule workShedule;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TimeTable> timeTableList;


}
