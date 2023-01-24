package com.carwash.telegram.entity.dto;

import com.carwash.telegram.entity.enums.StatusFree;
import com.carwash.telegram.entity.enums.StatusWork;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
public class TimeTableDto {


    private String dateTable;

    private Long idCarWash;

    private Long idUser;

    private StatusFree statusFree;

    private StatusWork statusWork;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableDto that = (TimeTableDto) o;
        return dateTable.equals(that.dateTable) && idCarWash.equals(that.idCarWash) && Objects.equals(idUser, that.idUser) && statusFree == that.statusFree && statusWork == that.statusWork;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTable, idCarWash, idUser, statusFree, statusWork);
    }


}
