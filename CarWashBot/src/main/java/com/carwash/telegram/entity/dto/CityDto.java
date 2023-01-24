package com.carwash.telegram.entity.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class CityDto {

    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityDto cityDto = (CityDto) o;
        return name.equalsIgnoreCase(cityDto.name);
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
