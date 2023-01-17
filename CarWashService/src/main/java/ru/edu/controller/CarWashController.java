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
import ru.edu.entity.CarWash;
import ru.edu.service.CarWashService;


import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/CarWash-service/carWash", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CarWashController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarWashController.class);

    private final CarWashService service;

    @Operation(summary = "Get all carWash")
    @GetMapping
    public ResponseEntity<List<CarWash>> getAllCarWashs() {
        List<CarWash> carWashes = service.findAll();
        LOGGER.info("getting CarWash list: {}", carWashes);
        return new ResponseEntity<>(carWashes, HttpStatus.OK);
    }

    /***
     * Method for getting carWash details
     * @param carWashId - carWash id
     * @return carWash details
     */
    @GetMapping("/{carWashId}")
    public CarWash getCarWashById(@PathVariable long carWashId) {
        return service.findById(carWashId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarWash> createCarWash(@RequestBody CarWash carWash) {
        service.save(carWash);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/CarWash-service/carWash/" + carWash.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CarWash> updateCarWash(@RequestBody CarWash carWash) {
        service.update(carWash);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{carWashId}")
    public ResponseEntity<CarWash> deleteCarWashById(@PathVariable long carWashId) {
        service.deleteById(carWashId);
        return ResponseEntity.ok().build();
    }
}
