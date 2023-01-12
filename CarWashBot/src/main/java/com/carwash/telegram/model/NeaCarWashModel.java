package com.carwash.telegram.model;

import lombok.Data;

import java.util.Objects;

@Data
public class NeaCarWashModel implements CarWashModel {
    private long id;
    private String address;
    private String price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NeaCarWashModel that = (NeaCarWashModel) o;
        return id == that.id && Objects.equals(address, that.address) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, price);
    }

    @Override
    public String toString() {
        return  "/" + id +
                ", адрес='" + address + '\'' +
                ", цена='" + price + '\'';
    }
}
