package com.carwash.telegram.model;

import lombok.Data;

import java.util.Objects;

@Data
public class AllCarWashModel implements CarWashModel {
    private long id;
    private String address;
    private String latitude;
    private String longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllCarWashModel that = (AllCarWashModel) o;
        return id == that.id && Objects.equals(address, that.address) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, latitude, longitude);
    }

    @Override
    public String toString() {
        return  id +
                ", адрес='" + address + '\'' +
                ", координата 1='" + latitude + '\'' +
                ", координата 2='" + longitude + '\'';
    }
}
