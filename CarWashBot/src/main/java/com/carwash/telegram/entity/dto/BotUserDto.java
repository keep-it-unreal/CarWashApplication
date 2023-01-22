package com.carwash.telegram.entity.dto;

import lombok.Data;

import java.util.Objects;

@Data
public class BotUserDto {
    private Long id;
    private String name;

    private String phone;

    private String addressMail;

    private String password;

    //private CityDto city;
    private Long idCity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotUserDto that = (BotUserDto) o;
        return name.toUpperCase().equals(that.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
