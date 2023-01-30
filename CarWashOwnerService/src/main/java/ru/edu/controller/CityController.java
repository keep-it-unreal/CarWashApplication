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
import ru.edu.entity.City;
import ru.edu.service.CityService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin-service/city", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityController.class);

    private final CityService service;

    /**
     *
     * @return
     */
    @Operation(summary = "Get all city")
    @GetMapping
    public ResponseEntity<List<City>> getAllCitys() {
        List<City> cities = service.findAll();
        LOGGER.info("getting city list: {}", cities);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    /***
     * Method for getting city details
     * @param cityId - city id
     * @return city details
     */
    @GetMapping("/{cityId}")
    public ResponseEntity<City> getCityById(@PathVariable long cityId) {
        return new ResponseEntity<>(service.findById(cityId), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> createCity(@RequestBody City city) {
        service.save(city);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/city/" + city.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<City> updateCity(@RequestBody City city) {
        service.update(city);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<City> deleteCityById(@PathVariable long cityId) {
        service.deleteById(cityId);
        return ResponseEntity.ok().build();
    }
}
