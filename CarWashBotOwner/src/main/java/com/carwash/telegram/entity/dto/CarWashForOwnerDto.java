package com.carwash.telegram.entity.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class CarWashForOwnerDto {
    private long id;
    private String address;
    private double latitude;
    private double longitude;
    private String dailyStartTime;
    private String dailyEndTime;
    private double price;

    private Long idUser;

    private Long idCity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarWashForOwnerDto that = (CarWashForOwnerDto) o;
        return id == that.id && Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && Double.compare(that.price, price) == 0 && address.equals(that.address) && Objects.equals(dailyStartTime, that.dailyStartTime) && Objects.equals(dailyEndTime, that.dailyEndTime) && idUser.equals(that.idUser) && idCity.equals(that.idCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, latitude, longitude, dailyStartTime, dailyEndTime, price, idUser, idCity);
    }

    @Override
    public String toString() {
        return " Адрес: " + address + "\n" +
                "Координаты: " + latitude +
                ", "  + longitude + "\n"+
                "Время работы с " + dailyStartTime + " " +
                "по " + dailyEndTime + " час.\n" +
                "Стоимость услуги: " + price + " руб.\n";
    }
}
