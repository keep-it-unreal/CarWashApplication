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
import ru.edu.service.CarWashService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "${inEndpoint.carWashController}", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class CarWashController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarWashController.class);

    private final CarWashService carWashService;

    @Value("${outEndpoint.server}${outEndpoint.userService}")
    private String userServiceEndpoint;

    @Operation(summary = "Get all carWash")
    @GetMapping
    public ResponseEntity<List<CarWash>> getAllCarWashes() {
        List<CarWash> carWashes = carWashService.findAll();
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
        return carWashService.findById(carWashId);
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
    public ResponseEntity<CarWash> deleteCarWashById(@PathVariable long carWashId) {
        carWashService.deleteById(carWashId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-for-user-on-date")
    public ResponseEntity<Collection<CarWash>> getAllCarWashesByUserAndDate(@RequestParam String username,
                                                                            @RequestParam
                                                                            @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {
        UserInfo user = getUserByUsernameFromUserService(username);
        if(user == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(carWashService.findVacantCarWashByUserAndAtDate(user, date), HttpStatus.OK);
    }

    @GetMapping("/near-for-user-on-date")
    public ResponseEntity<Collection<CarWash>> getNearCarWashesByUserAndDate(@RequestParam String username,
                                                                             @RequestParam
                                                                             @DateTimeFormat(pattern = "dd.MM.yyyy") Date date,
                                                                             @RequestParam Double latitude,
                                                                             @RequestParam Double longitude) {
        UserInfo user = getUserByUsernameFromUserService(username);
        if(user == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(carWashService.findNearCarWashByUserAndDateAndCoordinates(user, date, latitude, longitude), HttpStatus.OK);
    }

    private UserInfo getUserByUsernameFromUserService (String username){
        RestTemplate restTemplate = new RestTemplate();
        UserInfo user = null;
        try {
            ResponseEntity<UserInfo> userInfoResponse = restTemplate.getForEntity(userServiceEndpoint + "/" + username, UserInfo.class);
            user = userInfoResponse.getBody();

        } catch (Exception exception) {
            log.error("UserService unavailable...");
        }
        return user;
    }

}
