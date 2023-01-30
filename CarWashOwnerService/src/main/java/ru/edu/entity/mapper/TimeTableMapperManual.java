package ru.edu.entity.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.TimeTableDto;
import ru.edu.service.TimeTableService;
import ru.edu.service.UserInfoService;
import ru.edu.util.DateConverter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Service
@RequiredArgsConstructor
public class TimeTableMapperManual {

    private final TimeTableService service;
    private final UserInfoService serviceUser;

    private static final String PATTERN_FORMAT = "dd.MM.yyyy hh:mm:ss";

    public TimeTableDto toDTO(TimeTable model) {
        if ( model == null ) {
            return null;
        }

        TimeTableDto timeTableDto = new TimeTableDto();

        Instant instant = model.getID().getDateTable();
        String  st = DateConverter.InstantToFullString(instant);
        timeTableDto.setDateTable(st);

        timeTableDto.setIdCarWash( model.getCarWash().getId());
        timeTableDto.setAddress( model.getCarWash().getAddress());

        if (model.getUserInfo() != null) {
            timeTableDto.setIdUser(model.getUserInfo().getId());
        }

        timeTableDto.setStatusFree( model.getStatusFree() );
        timeTableDto.setStatusWork( model.getStatusWork() );

        return timeTableDto;
    }

    public TimeTable toModel(TimeTableDto dto) {
        if ( dto == null ) {
            return null;
        }

        String  st = dto.getDateTable();
        Instant instant = DateConverter.FullStringToInstant(st);
        instant = instant.minus(3, ChronoUnit.HOURS);

        TimeTableID id = new TimeTableID();
        id.setDateTable(instant);
        id.setIdCarWash( dto.getIdCarWash());

        TimeTable timeTable = service.findById(id);

        UserInfo userInfo = null;
        if (dto.getIdUser() != null) {
            userInfo = serviceUser.findById(dto.getIdUser());
        }
        timeTable.setUserInfo(userInfo);

        timeTable.setStatusFree( dto.getStatusFree() );
        timeTable.setStatusWork( dto.getStatusWork() );

        return timeTable;
    }
}
