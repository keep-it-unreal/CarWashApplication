package ru.edu.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.CarWash;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.UserInfo;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusWork;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDto implements Serializable {

    private String dateTable;

    private Long idCarWash;

    private String address;

    private Long idUser;

    private StatusFree statusFree;

    private StatusWork statusWork;
}