package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.TimeTable;
import ru.edu.service.TimeTableService;
import ru.edu.service.TimeTableService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin-service/timeTable", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TimeTableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableController.class);

    private final TimeTableService service;

    @Operation(summary = "Get all timeTable")
    @GetMapping
    public ResponseEntity<List<TimeTable>> getAllTimeTables() {
        List<TimeTable> timeTables = service.findAll();
        LOGGER.info("getting timeTable list: {}", timeTables);
        return new ResponseEntity<>(timeTables, HttpStatus.OK);
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
