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
public class TimeInterruptID implements Serializable {

    private Instant dateBegin;

    private Long idCarWash;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeInterruptID that = (TimeInterruptID) o;
        return dateBegin.equals(that.dateBegin) && idCarWash.equals(that.idCarWash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateBegin, idCarWash);
    }
}
