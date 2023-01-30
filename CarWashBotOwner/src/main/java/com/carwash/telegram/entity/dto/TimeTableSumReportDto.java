package com.carwash.telegram.entity.dto;

import lombok.Data;

@Data
public class TimeTableSumReportDto {

    private Long idCarWash;
    private String address;
    private Double price;
    private Integer ch;

    /*
    @Override
    public String toString() {
        String europeanDatePattern = "dd.MM.yyyy";
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

        return "Адрес: " + address + ", " +
                "клиентов: " + countClient + ", " +
                "сумма: " + price * countClient;
    }

     */
}
