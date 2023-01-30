package ru.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeTableID implements Serializable {

    private Instant dateTable;

    private Long idCarWash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableID that = (TimeTableID) o;
        return dateTable.equals(that.dateTable) && idCarWash.equals(that.idCarWash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTable, idCarWash);
    }
}
