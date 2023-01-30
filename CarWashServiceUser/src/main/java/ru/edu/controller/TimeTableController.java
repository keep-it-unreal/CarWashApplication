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
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.TimeTableDto;
import ru.edu.entity.mapper.TimeTableListMapper;
import ru.edu.entity.mapper.TimeTableMapper;
import ru.edu.entity.mapper.TimeTableMapperManual;
import ru.edu.service.TimeTableService;
import ru.edu.service.UserInfoService;
import ru.edu.util.DateConverter;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/CarWash-service/timeTable", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TimeTableController {

   private final TimeTableService service;

    private final UserInfoService userInfoService;

    @Autowired
    //private TimeTableMapper mapper;
    private TimeTableMapperManual mapper;

    @Autowired
    private TimeTableListMapper mapperList;

    @Operation(summary = "Get all timeTable")
    @GetMapping("/all")
    public ResponseEntity<List<TimeTable>> getAllTimeTables() {
        List<TimeTable> timeTables = service.findAll();
        log.info("getting timeTable list: {}", timeTables);
        return new ResponseEntity<>(timeTables, HttpStatus.OK);
    }

    /***
     * Получить список всех свободных позиций времени для записи пользователя на автомойку
     * для выбранной пользователем даты, мойки
     * @param date - выбранная дата
     * @param idCarWash - id выбранной мойки
     * @return - список свободных позиций времени
     */
    @GetMapping("/byDate")
    public ResponseEntity<List<TimeTableDto>> getTimeTableByDateByIdCarWash(@RequestParam("date") String date, @RequestParam("idCarWash") Long idCarWash) {
        try {
            List<TimeTable> timeTableList = service.findByDateByIdCarWash(date,idCarWash);
            log.info("getting timeTableList list: {}", timeTableList);
            List<TimeTableDto> timeTableDtoList = mapperList.toDtoList(timeTableList);

            log.info("getting timeTableDto list: {}", timeTableDtoList);
            return new ResponseEntity<>(timeTableDtoList, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /***
     * Method for update timeTable details for create a user order
     * @param dto - timeTableDto details
     * @return updated instance timeTableDto details
     */
    @Operation(summary = "Place a user order for a car wash service")
    //@PutMapping(path = "/order-on", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(path = "/order-on")
    public ResponseEntity<TimeTableDto> createOrder(@RequestBody TimeTableDto dto) {
        TimeTable timeTable = mapper.toModel(dto);
        service.update(timeTable);
        //TimeTableDto timeTableDto = mapper.toDTO(timeTable);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /***
     * Method for get a list of all user orders for date >= current
     * @param idUser - id пользователя
     * @return - список заказов пользователя
     */
    @GetMapping(path="/history-on")
    public ResponseEntity<List<TimeTableDto>> getActiveOrder(@RequestParam("idUser") Long idUser) {
        try {
            List<TimeTable> timeTableList = service.findByIdUserAndByDatetableGreatesThanNow(idUser);
            log.info("getting timeTableList list: {}", timeTableList);
            List<TimeTableDto> timeTableDtoList = mapperList.toDtoList(timeTableList);

            log.info("getting timeTableDto list: {}", timeTableDtoList);
            return new ResponseEntity<>(timeTableDtoList, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /***
     * Method for get a list of all user orders for date >= current and status = planned
     * @param idUser - id пользователя
     * @return - список заказов пользователя
     */
    @GetMapping(path="/list-order-off")
    public ResponseEntity<List<TimeTableDto>> getActiveOrderStatusPlanned(@RequestParam("idUser") Long idUser) {
        try {
            List<TimeTable> timeTableList = service.findByIdUserAndByDatetableGreatesThanNowAndStatusPlanned(idUser);
            log.info("getting timeTableList list: {}", timeTableList);
            List<TimeTableDto> timeTableDtoList = mapperList.toDtoList(timeTableList);

            log.info("getting timeTableDto list: {}", timeTableDtoList);
            return new ResponseEntity<>(timeTableDtoList, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /***
     * Method for пet a history of all user orders
     * @param idUser - id пользователя
     * @return - список заказов пользователя
     */
    @GetMapping(path="/history")
    public ResponseEntity<List<TimeTableDto>> getHistory(@RequestParam("idUser") Long idUser) {
        try {
            List<TimeTable> timeTableList = service.findByIdUser(idUser);
            log.info("getting timeTableList list: {}", timeTableList);
            List<TimeTableDto> timeTableDtoList = mapperList.toDtoList(timeTableList);

            log.info("getting timeTableDto list: {}", timeTableDtoList);
            return new ResponseEntity<>(timeTableDtoList, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /***
     * Method for getting timeTable details
     * @param timeTableId - timeTable id
     * @return timeTable details
     */
    @GetMapping("/{timeTableId}")
    public TimeTable getTimeTableById(@PathVariable TimeTableID timeTableId) {
        return service.findById(timeTableId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeTable> createTimeTable(@RequestBody TimeTable timeTable) {
        service.save(timeTable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/timeTable/" + timeTable.getID());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TimeTable> updateTimeTable(@RequestBody TimeTable timeTable) {
        service.update(timeTable);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timeTableId}")
    public ResponseEntity<TimeTable> deleteTimeTableById(@PathVariable TimeTableID timeTableId) {
        service.deleteById(timeTableId);
        return ResponseEntity.ok().build();
    }
}
