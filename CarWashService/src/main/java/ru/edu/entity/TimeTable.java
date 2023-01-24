package ru.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusWork;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity(name = "time_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeTable implements Serializable {

    @EmbeddedId
    private TimeTableID id;

    private StatusFree statusFree;

    private StatusWork statusWork;

    @ManyToOne(cascade = CascadeType.ALL,optional=false)
    private CarWash carWash;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;

    public TimeTableID getID() {
        return id;
    }
}