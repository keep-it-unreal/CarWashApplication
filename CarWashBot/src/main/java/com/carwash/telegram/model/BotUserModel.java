package com.carwash.telegram.model;

import lombok.Data;

import java.util.Objects;

@Data
public class BotUserModel {
    private Long id;
    private String name;

    private String phone;

    private String addressMail;

    private String password;

    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotUserModel that = (BotUserModel) o;
        return name.toUpperCase().equals(that.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "BotUserModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", addressMail='" + addressMail + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
