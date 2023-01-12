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
import ru.edu.entity.WorkShedule;
import ru.edu.service.WorkSheduleService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin-service/workShedule", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WorkSheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkSheduleController.class);

    private final WorkSheduleService service;

    @Operation(summary = "Get all workShedule")
    @GetMapping
    public ResponseEntity<List<WorkShedule>> getAllWorkShedules() {
        List<WorkShedule> workShedules = service.findAll();
        LOGGER.info("getting workShedule list: {}", workShedules);
        return new ResponseEntity<>(workShedules, HttpStatus.OK);
    }

    /***
     * Method for getting workShedule details
     * @param workSheduleId - workShedule id
     * @return workShedule details
     */
    @GetMapping("/{workSheduleId}")
    public WorkShedule getWorkSheduleById(@PathVariable long workSheduleId) {
        return service.findById(workSheduleId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkShedule> createWorkShedule(@RequestBody WorkShedule workShedule) {
        service.save(workShedule);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/workShedule/" + workShedule.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<WorkShedule> updateWorkShedule(@RequestBody WorkShedule workShedule) {
        service.update(workShedule);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{workSheduleId}")
    public ResponseEntity<WorkShedule> deleteWorkSheduleById(@PathVariable long workSheduleId) {
        service.deleteById(workSheduleId);
        return ResponseEntity.ok().build();
    }
}
