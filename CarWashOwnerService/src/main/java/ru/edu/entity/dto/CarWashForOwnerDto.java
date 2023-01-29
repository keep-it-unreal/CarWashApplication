package ru.edu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarWashForOwnerDto implements Serializable {

    private Long id;

    private String address;

    private double latitude;

    private double longitude;

    private String dailyStartTime;

    private String dailyEndTime;

    private double price;

    private Long idUser;

    private Long idCity;


}
