package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.TimeTableDTO;
import ru.edu.service.CarWashService;
import ru.edu.service.TimeTableService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "${inEndpoint.timeTableController}", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class TimeTableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableController.class);

    private final TimeTableService timeTableService;
    private final CarWashService carWashService;

    @Value("${outEndpoint.server}${outEndpoint.userService}")
    private String userServiceEndpoint;

    @Operation(summary = "Get all timeTable")
    @GetMapping
    public ResponseEntity<List<TimeTable>> getAllTimeTables() {
        List<TimeTable> timeTables = timeTableService.findAll();
        LOGGER.info("getting timeTable list: {}", timeTables);
        return new ResponseEntity<>(timeTables, HttpStatus.OK);
    }

    /***
     * Method for getting timeTable details
     * @param timeTableId - timeTable id
     * @return timeTable details
     */
    @GetMapping("/{timeTableId}")
    //todo: выяснить как такой PathVariable будет работать? Если будет)
    public TimeTable getTimeTableById(@PathVariable TimeTableID timeTableId) {
        return timeTableService.findById(timeTableId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeTable> createTimeTable(@RequestBody TimeTable timeTable) {
        timeTableService.save(timeTable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/timeTable/" + timeTable.getID());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping("/order-on")
    //Записать пользователя на мойку машины
    public ResponseEntity<?> updateTimeTable(@RequestBody TimeTableDTO timeTableDTO) {
        RestTemplate restTemplate = new RestTemplate();
        UserInfo user;
        try {
            ResponseEntity<UserInfo> userInfoResponse = restTemplate.getForEntity(userServiceEndpoint + "findById/" + timeTableDTO.getIdUser(), UserInfo.class);
            user = userInfoResponse.getBody();

        } catch (Exception exception) {
            log.error("UserService unavailable...");
            return new ResponseEntity<>("UserService unavailable, can't proceed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            TimeTable timeTable = TimeTable.builder()
                    .id(new TimeTableID(timeTableDTO.getDateTable(), timeTableDTO.getIdCarWash()))
                    .userInfo(user)
                    .carWash(carWashService.findById(timeTableDTO.getIdCarWash()))
                    .statusFree(timeTableDTO.getStatusFree())
                    .statusWork(timeTableDTO.getStatusWork())
                    .build();
            timeTableService.update(timeTable);
        } catch (Exception exception) {
            log.error("Error in CarWashService...");
            return new ResponseEntity<>("It looks like a CarWash with this id was not found.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timeTableId}")
    public ResponseEntity<TimeTable> deleteTimeTableById(@PathVariable TimeTableID timeTableId) {
        timeTableService.deleteById(timeTableId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byDate")
    //Получить список свободных временных позиций для заданной даты, мойки
    public ResponseEntity<List<TimeTableDTO>> getTimeTableByDate(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") Date date,
                                                                 @RequestParam Integer carWashId) {
        List<TimeTableDTO> resultTimeTable = timeTableService.findVacantAtDate(date, carWashId)
                .stream()
                .map(TimeTableDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resultTimeTable, HttpStatus.OK);
    }

    @GetMapping("/history-on")
    //Получить список всех активных заказов пользователя (все, что для данного пользователя, на дату и время > текущего)
    public ResponseEntity<List<TimeTableDTO>> getActiveOrders(@RequestParam Long idUser) {
        List<TimeTableDTO> activeOrdersByUser = timeTableService.getActiveOrdersByUser(idUser)
                .stream()
                .map(TimeTableDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(activeOrdersByUser, HttpStatus.OK);
    }

    @DeleteMapping("/order_off")
    //Получить список запланированных заказов пользователя (дата >= текущей, статус - запланировано )
    public ResponseEntity<TimeTableDTO> abandonOrder(@RequestParam Date date,
                                                  @RequestParam Long carWashId,
                                                  @RequestParam Long idUser) {
        TimeTableDTO plannedTables = new TimeTableDTO(timeTableService.abandonOrder(date, carWashId, idUser));
        return new ResponseEntity<>(plannedTables, HttpStatus.OK);
    }

    @GetMapping("/history")
    //Получить историю всех заказов пользователя
    public ResponseEntity<List<TimeTableDTO>> getAllOrdersByUser(@RequestParam Long idUser) {
        List<TimeTableDTO> allOrdersByUser = timeTableService.getAllOrdersByUser(idUser)
                .stream()
                .map(TimeTableDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(allOrdersByUser, HttpStatus.OK);
    }
}
