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
import ru.edu.entity.TimeInterrupt;
import ru.edu.entity.TimeInterruptID;
import ru.edu.service.TimeInterruptService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/CarWash-service/timeInterrupt", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TimeInterruptController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeInterruptController.class);

    private final TimeInterruptService service;

    @Operation(summary = "Get all timeInterrupt")
    @GetMapping
    public ResponseEntity<List<TimeInterrupt>> getAllTimeInterrupts() {
        List<TimeInterrupt> timeInterrupts = service.findAll();
        LOGGER.info("getting timeInterrupt list: {}", timeInterrupts);
        return new ResponseEntity<>(timeInterrupts, HttpStatus.OK);
    }

    /***
     * Method for getting timeInterrupt details
     * @param timeInterruptId - timeInterrupt id
     * @return timeInterrupt details
     */
    @GetMapping("/{timeInterruptId}")
    public TimeInterrupt getTimeInterruptById(@PathVariable TimeInterruptID timeInterruptId) {
        return service.findById(timeInterruptId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeInterrupt> createTimeInterrupt(@RequestBody TimeInterrupt timeInterrupt) {
        service.save(timeInterrupt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/timeInterrupt/" + timeInterrupt.getTimeInterruptID());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TimeInterrupt> updateTimeInterrupt(@RequestBody TimeInterrupt timeInterrupt) {
        service.update(timeInterrupt);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timeInterruptId}")
    public ResponseEntity<TimeInterrupt> deleteTimeInterruptById(@PathVariable TimeInterruptID timeInterruptId) {
        service.deleteById(timeInterruptId);
        return ResponseEntity.ok().build();
    }
}
