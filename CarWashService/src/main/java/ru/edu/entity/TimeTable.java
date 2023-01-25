package ru.edu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusWork;

import javax.persistence.*;
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
    @JoinColumn(name = "idCarWash", foreignKey = @ForeignKey(name = "FK_TIME_TABLE_CAR"), insertable=false, updatable=false)
    @JsonIgnore
    private CarWash carWash;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser", foreignKey = @ForeignKey(name = "FK_TIME_TABLE_USER"))
    private UserInfo userInfo;

    public TimeTableID getID() {
        return id;
    }
}