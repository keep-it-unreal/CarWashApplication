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
import ru.edu.entity.CauseInterrupt;
import ru.edu.service.CauseInterruptService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/CarWash-service/causeInterrupt", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CauseInterruptController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CauseInterruptController.class);

    private final CauseInterruptService service;

    @Operation(summary = "Get all causeInterrupt")
    @GetMapping
    public ResponseEntity<List<CauseInterrupt>> getAllCauseInterrupts() {
        List<CauseInterrupt> causeInterrupt = service.findAll();
        LOGGER.info("getting causeInterrupt list: {}", causeInterrupt);
        return new ResponseEntity<>(causeInterrupt, HttpStatus.OK);
    }

    /***
     * Method for getting causeInterrupt details
     * @param causeInterruptId - causeInterrupt id
     * @return causeInterrupt details
     */
    @GetMapping("/{causeInterruptId}")
    public CauseInterrupt getCauseInterruptById(@PathVariable long causeInterruptId) {
        return service.findById(causeInterruptId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CauseInterrupt> createCauseInterrupt(@RequestBody CauseInterrupt causeInterrupt) {
        service.save(causeInterrupt);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/causeInterrupt/" + causeInterrupt.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CauseInterrupt> updateCauseInterrupt(@RequestBody CauseInterrupt causeInterrupt) {
        service.update(causeInterrupt);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{causeInterruptId}")
    public ResponseEntity<CauseInterrupt> deleteCauseInterruptById(@PathVariable long causeInterruptId) {
        service.deleteById(causeInterruptId);
        return ResponseEntity.ok().build();
    }
}
