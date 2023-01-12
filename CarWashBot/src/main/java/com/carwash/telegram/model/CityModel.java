package com.carwash.telegram.model;

import lombok.Data;

import java.util.Objects;

@Data
public class CityModel {

    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityModel cityModel = (CityModel) o;
        return name.equalsIgnoreCase(cityModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "CityModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
