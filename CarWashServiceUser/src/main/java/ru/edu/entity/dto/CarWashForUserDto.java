package ru.edu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarWashForUserDto implements Serializable {

    private Long id;

    private String address;

    private double price;

}
