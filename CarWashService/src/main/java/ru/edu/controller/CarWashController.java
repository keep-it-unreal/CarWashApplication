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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.edu.entity.CarWash;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.CarWashDTO;
import ru.edu.service.CarWashService;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "${inEndpoint.carWashController}", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class CarWashController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarWashController.class);

    private final CarWashService carWashService;
    @Value("${outEndpoint.userController.findById}")
    private String urlUserControllerFindById;

    @Operation(summary = "Get all carWash")
    @GetMapping
    public ResponseEntity<List<CarWash>> getAllCarWashes() {
        List<CarWash> carWashes = carWashService.findAll();
        LOGGER.info("getting CarWash list: {}", carWashes);
        return new ResponseEntity<>(carWashes, HttpStatus.OK);
    }

    @GetMapping("/{carWashId}")
    public ResponseEntity<CarWash> getCarWashById(@PathVariable Long carWashId) {
        return new ResponseEntity<>(carWashService.findById(carWashId), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarWash> createCarWash(@RequestBody CarWash carWash) {
        carWashService.save(carWash);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/CarWash-service/carWash/" + carWash.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CarWash> updateCarWash(@RequestBody CarWash carWash) {
        carWashService.update(carWash);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{carWashId}")
    public ResponseEntity<CarWash> deleteCarWashById(@PathVariable Long carWashId) {
        carWashService.deleteById(carWashId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-for-user-on-date")
    //Получить список всех автомоек, расположенных в городе пользователя, имеющих свободные позиции по времени на запрошенную дату
    public ResponseEntity<Collection<CarWashDTO>> getAllCarWashesByUserAndDate(@RequestParam Long idUser,
                                                                               @RequestParam
                                                                               @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<UserInfo> botUserResponse = restTemplate.getForEntity(urlUserControllerFindById + idUser, UserInfo.class);

            Collection<CarWashDTO> resultCarWashes = carWashService.findVacantCarWashByUserIdAndAtDate(botUserResponse.getBody().getCity().getId(), date)
                    .stream()
                    .map(CarWashDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(resultCarWashes, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("UserService unavailable. idUser = {} not found, or idCity not found", idUser);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/near-for-user-on-date")
    //Получить список всех автомоек, расположенных в городе пользователя, ближайших к заданным координатам (не более 3), имеющих свободные позиции по времени на запрошенную дату
    public ResponseEntity<Collection<CarWashDTO>> getNearCarWashesByUserAndDate(@RequestParam Long idUser,
                                                                                @RequestParam
                                                                                @DateTimeFormat(pattern = "dd.MM.yyyy") Date date,
                                                                                @RequestParam Double latitude,
                                                                                @RequestParam Double longitude) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<UserInfo> botUserResponse = restTemplate.getForEntity(urlUserControllerFindById + idUser, UserInfo.class);

            Collection<CarWashDTO> resultCarWashes = carWashService.findNearCarWashByUserIdAndDateAndCoordinates(botUserResponse.getBody().getCity().getId(), date, latitude, longitude)
                    .stream()
                    .map(CarWashDTO::new)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(resultCarWashes, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("UserService unavailable. idUser = {} not found, or idCity not found", idUser);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
