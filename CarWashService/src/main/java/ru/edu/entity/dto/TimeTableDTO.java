package ru.edu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.TimeTable;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusWork;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableDTO {
    private Instant dateTable;
    private Long idCarWash;
    private String address;
    private Long idUser;
    private StatusFree statusFree;
    private StatusWork statusWork;

    public TimeTableDTO(TimeTable timeTable) {
        this.dateTable = timeTable.getID().getDateTable();
        this.idCarWash = timeTable.getCarWash().getId();
        this.address = timeTable.getCarWash().getAddress();
        this.idUser = timeTable.getUserInfo().getId();
        this.statusFree = timeTable.getStatusFree();
        this.statusWork = timeTable.getStatusWork();
    }
}
