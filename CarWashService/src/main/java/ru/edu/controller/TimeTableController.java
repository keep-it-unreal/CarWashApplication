package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.service.TimeTableService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "${inEndpoint.timeTableController}", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TimeTableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableController.class);

    private final TimeTableService timeTableService;

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
    public ResponseEntity<TimeTable> updateTimeTable(@RequestBody TimeTable timeTable) {
        timeTableService.update(timeTable);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timeTableId}")
    public ResponseEntity<TimeTable> deleteTimeTableById(@PathVariable TimeTableID timeTableId) {
        timeTableService.deleteById(timeTableId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byDate")
    public ResponseEntity<List<TimeTable>> getTimeTableByDate(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") Date date,
                                                        @RequestParam Integer carWashId) {
        return new ResponseEntity<>(timeTableService.findVacantAtDate(date,carWashId),HttpStatus.OK);
    }

    @GetMapping("/history-on")
    public ResponseEntity<List<TimeTable>> getActiveOrders(@RequestParam Long idUser) {
        //todo: конвертировать в TimeTableDTO
        List<TimeTable> activeOrdersByUser = timeTableService.getActiveOrdersByUser(idUser);
        return new ResponseEntity<>(activeOrdersByUser,HttpStatus.OK);
    }

    @DeleteMapping("/order_off")
    public ResponseEntity<TimeTable> abandonOrder(@RequestParam Date date,
                                          @RequestParam Long carWashId,
                                          @RequestParam Integer idUser) {
        return new ResponseEntity<>(timeTableService.abandonOrder(date, carWashId, idUser),HttpStatus.OK);
    }

    @GetMapping("/history-all")
    public ResponseEntity<List<TimeTable>> getAllOrdersByUser(@RequestParam Long idUser) {
        return new ResponseEntity<>(timeTableService.getAllOrdersByUser(idUser),HttpStatus.OK);
    }
}
