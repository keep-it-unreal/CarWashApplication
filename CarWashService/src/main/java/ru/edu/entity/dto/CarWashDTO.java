package ru.edu.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.CarWash;

@Data
@NoArgsConstructor
public class CarWashDTO {
    private Long id;
    private String address;
    private double price;

    public CarWashDTO(CarWash carWash) {
        this.id = carWash.getId();
        this.address = carWash.getAddress();
        this.price = carWash.getPrice();
    }
}
