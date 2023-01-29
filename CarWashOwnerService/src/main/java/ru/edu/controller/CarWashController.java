package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.entity.*;
import ru.edu.entity.dto.CarWashForOwnerDto;
import ru.edu.entity.TimeTableSumReportDto;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusShedule;
import ru.edu.entity.enums.StatusWork;
import ru.edu.entity.mapper.CarWashForOwnerMapperManual;
import ru.edu.entity.mapper.CarWashListMapper;
import ru.edu.service.CarWashService;
import ru.edu.service.TimeTableService;
import ru.edu.service.UserInfoService;
import ru.edu.service.WorkSheduleService;
import ru.edu.util.DateConverter;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/CarWash-service/carWash", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CarWashController {

    private static final int CAR_WASH_DISPLAY_AMOUNT = 3;
    private final CarWashService service;
    private final UserInfoService userInfoService;

    private final WorkSheduleService workSheduleService;
    private final TimeTableService timeTableService;

    @Autowired
    //private CarWashForOwnerMapper mapper;
    private CarWashForOwnerMapperManual mapper;

    @Autowired
    private CarWashListMapper mapperList;
    //private CarWashListMapperManual mapperList;


    @Operation(summary = "Получить список всех моек для данного владельца")
    @GetMapping(path = "/list-for-owner")
    public ResponseEntity<List<CarWashForOwnerDto>> findByIdOwner(@RequestParam("idUser") Long idUser) {
        try {
            List<CarWash> carWasheList = service.findByIdOwner(idUser);
            log.info("getting carWasheList: {}", carWasheList);
            if (carWasheList.size() ==0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            List<CarWashForOwnerDto> listCarWashForOwnerDto = mapperList.toDtoList(carWasheList);

            log.info("getting listCarWashForUserDto list: {}", listCarWashForOwnerDto);
            return new ResponseEntity<>(listCarWashForOwnerDto, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Добавить мойку для данного владельца")
    @PostMapping(path = "/add-by-owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarWashForOwnerDto> createCarWashByOwner(@RequestBody CarWashForOwnerDto carWashForOwnerDto) {
        CarWash carWash = mapper.toModel(carWashForOwnerDto);
        carWash = service.save(carWash);

        WorkShedule workShedule = new WorkShedule();
        workShedule.setCarWash(carWash);
        workShedule.setId(carWash.getId());
        workShedule.setMonday(StatusShedule.WORKING_DAY);
        workShedule.setTuesday(StatusShedule.WORKING_DAY);
        workShedule.setWednesday(StatusShedule.WORKING_DAY);
        workShedule.setThursday(StatusShedule.WORKING_DAY);
        workShedule.setFriday(StatusShedule.WORKING_DAY);
        workShedule.setSaturday(StatusShedule.WORKING_DAY);
        workShedule.setSunday(StatusShedule.WORKING_DAY);
        workSheduleService.save(workShedule);

        CarWashForOwnerDto newCarWashForOwnerDto = mapper.toDTO(carWash);
        return new ResponseEntity<>(newCarWashForOwnerDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить информацию о мойке по ее Id")
    @GetMapping("/{carWashId}")
    public CarWash getCarWashById(@PathVariable long carWashId) {
        return service.findById(carWashId);
    }

    @Operation(summary = "Для всех моек данного владельца на каждый день текущего месяца, начиная с завтрашнего дня внести строки в график работы в соответствии с рабочим временем и выходными")
    @GetMapping(path = "/time-add-cur")
    public ResponseEntity<CarWashForOwnerDto>ForEachCarWashByOwnerAddTimeTableForCurrentMonth(@RequestParam("idUser") Long idUser) {
        try {
            List<CarWash> carWasheList = service.findByIdOwner(idUser);
            log.info("getting carWasheList: {}", carWasheList);

            LocalDate ldTomorrow = LocalDate.now().plus(1,ChronoUnit.DAYS);
            Boolean Result = addTimeTableForEachDayPeriodForEachIdCarWash(carWasheList,ldTomorrow);

            if (Result) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.SEE_OTHER);
            }

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Для всех моек данного владельца на каждый день следующего месяца внести строки в график работы в соответствии с рабочим временем и выходными")
    @GetMapping(path = "/time-add-next")
    public ResponseEntity<CarWashForOwnerDto>ForEachCarWashByOwnerAddTimeTableForNextMonth(@RequestParam("idUser") Long idUser) {
        try {
            List<CarWash> carWasheList = service.findByIdOwner(idUser);
            log.info("getting carWasheList: {}", carWasheList);

            LocalDate ldFirstDayNextMonth = LocalDate.now().plus(1,ChronoUnit.MONTHS).withDayOfMonth(1);
            Boolean Result = addTimeTableForEachDayPeriodForEachIdCarWash(carWasheList,ldFirstDayNextMonth);

            if (Result) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.SEE_OTHER);
            }

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Создать мойку")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarWash> createCarWash(@RequestBody CarWash carWash) {
        service.save(carWash);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/CarWash-service/carWash/" + carWash.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить информацию о мойке")
    @PutMapping
    public ResponseEntity<CarWash> updateCarWash(@RequestBody CarWash carWash) {
        service.update(carWash);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить мойку по Id")
    @DeleteMapping("/{carWashId}")
    public ResponseEntity<CarWash> deleteCarWashById(@PathVariable Long idCarWash) {

        try {
            CarWash carWash = service.findById(idCarWash);
            carWash.setUserInfo(null);
            carWash.setCity(null);
            carWash.setWorkShedule(null);

            try {
                timeTableService.setUserIsNullForDeletedCarWash(idCarWash);
            } catch (Exception ex) {
                log.info("Exception = {}", ex);
            }

            try {
                timeTableService.deleteAllByIdCarWash(idCarWash);
            } catch (Exception ex) {
                log.info("Exception = {}", ex);
            }

            service.deleteById(idCarWash);
            return ResponseEntity.ok().build();

        } catch (Exception ex) {
            log.info("Exception = {}", ex);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Boolean addTimeTableForEachDayPeriodForEachIdCarWash (List<CarWash> carWasheList, LocalDate periodBegin) {
        Boolean Result = true;

        try {
            LocalDate periodEnd = periodBegin.withDayOfMonth(periodBegin.lengthOfMonth());
            DayOfWeek day;
            Long ch;
            String stBegin, stEnd;
            Instant instantBegin, instantEnd;
            CarWash carWash;
            int i=0;

            while (Result & (i < carWasheList.size())) {
                carWash = carWasheList.get(i);

                WorkShedule workShedule = carWash.getWorkShedule();
                LocalDate d=periodBegin;
                while (Result & (d.getDayOfYear() <= periodEnd.getDayOfYear())) {

                    day = d.getDayOfWeek();
                    if ( (day == DayOfWeek.MONDAY) & (workShedule.getMonday() == StatusShedule.WORKING_DAY) |
                            (day == DayOfWeek.TUESDAY) & (workShedule.getTuesday() == StatusShedule.WORKING_DAY) |
                            (day == DayOfWeek.WEDNESDAY) & (workShedule.getWednesday() == StatusShedule.WORKING_DAY) |
                            (day == DayOfWeek.THURSDAY) & (workShedule.getThursday() == StatusShedule.WORKING_DAY) |
                            (day == DayOfWeek.FRIDAY) & (workShedule.getFriday() == StatusShedule.WORKING_DAY) |
                            (day == DayOfWeek.SATURDAY) & (workShedule.getSaturday() == StatusShedule.WORKING_DAY) |
                            (day == DayOfWeek.SUNDAY) & (workShedule.getSunday() == StatusShedule.WORKING_DAY) ) {

                        ch = timeTableService.findByIdCarWashAndDate(carWash.getId(),d);

                        if (ch == 0) {
                            stBegin = d.toString();
                            stEnd =  stBegin.substring(8) + "." + stBegin.substring(5,7) + "." + stBegin.substring(0,4) + " " + carWash.getDailyEndTime() + ":00:00";
                            stBegin = stBegin.substring(8) + "." + stBegin.substring(5,7) + "." + stBegin.substring(0,4) + " " + carWash.getDailyStartTime() + ":00:00";

                            instantBegin = DateConverter.FullStringToInstant(stBegin);
                            instantEnd = DateConverter.FullStringToInstant(stEnd).minus(1,ChronoUnit.SECONDS);

                            for (Instant curInstant = instantBegin; instantEnd.isAfter(curInstant); curInstant = curInstant.plus(1,ChronoUnit.HOURS)) {

                                Result = Result & AddTimeTable(carWash.getId(),curInstant);
                                Result = Result & AddTimeTable(carWash.getId(),curInstant.plus(30,ChronoUnit.MINUTES));
                            }

                        }
                    }
                    d = d.plus(1,ChronoUnit.DAYS);
                }
                i++;
            }

        } catch (Exception ex) {
            log.info("Exception = {}", ex);
            Result = false;
        }
        return (Result);
    }

    private Boolean AddTimeTable(Long IdCarWash, Instant instant) {
        Boolean Result=false;

        try {
            TimeTable timeTable;
            TimeTableID timeTableID;

            timeTableID = new TimeTableID();
            timeTableID.setIdCarWash(IdCarWash);
            timeTableID.setDateTable(instant.minus(3,ChronoUnit.HOURS));

            timeTable = new TimeTable();
            timeTable.setId(timeTableID);
            timeTable.setStatusFree(StatusFree.FREE);
            timeTable.setStatusWork(StatusWork.NONE);

            timeTableService.save(timeTable);

            Result = true;
        } catch (Exception ex) {
            log.info("Exception = {}", ex);
        }
        return Result;
    }
}
