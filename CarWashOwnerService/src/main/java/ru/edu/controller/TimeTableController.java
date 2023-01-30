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
import ru.edu.entity.CarWash;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.dto.CarWashForOwnerDto;
import ru.edu.entity.dto.TimeTableDto;
import ru.edu.entity.mapper.TimeTableListMapper;
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


    @Operation(summary = "Get all order by id Car wash by date")
    @GetMapping("/order-by-date")
    public ResponseEntity<List<TimeTableDto>> getAllTimeTables(@RequestParam("idCarWash") Long idCarWash, @RequestParam("dateOrder") String dateOrder) {

        try {
            Instant iDateOrder = DateConverter.StringToInstant(dateOrder);
            List<TimeTable> timeTables = service.findByDateByIdCarWash(idCarWash,iDateOrder);
            log.info("getting timeTable list: {}", timeTables);

            if (timeTables.size() ==0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            List<TimeTableDto> timeTableDtos = mapperList.toDtoList(timeTables);

            log.info("getting timeTableDtos list: {}", timeTableDtos);
            return new ResponseEntity<>(timeTableDtos, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all timeTable")
    @GetMapping("/all")
    public ResponseEntity<List<TimeTable>> getAllTimeTables() {
        List<TimeTable> timeTables = service.findAll();
        log.info("getting timeTable list: {}", timeTables);
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
