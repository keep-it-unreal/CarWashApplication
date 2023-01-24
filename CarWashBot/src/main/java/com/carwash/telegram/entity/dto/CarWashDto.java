package com.carwash.telegram.entity.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class CarWashDto {
    private long id;
    private String address;

    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarWashDto that = (CarWashDto) o;
        return id == that.id && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }

    @Override
    public String toString() {
        return  id +
                ", адрес='" + address + '\'' +
                ", цена='" + price + '\'';
    }
}
