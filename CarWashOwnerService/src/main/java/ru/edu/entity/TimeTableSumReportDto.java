package ru.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
public class TimeTableSumReportDto implements Serializable {


    @Id
    private Long idCarWash;
    private String address;
    private Double price;
    private Integer ch;


}
