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
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.CarWashForUserDto;
import ru.edu.entity.mapper.CarWashForUserMapper;
import ru.edu.entity.mapper.CarWashListMapper;
import ru.edu.service.CarWashService;
import ru.edu.service.UserInfoService;
import ru.edu.util.DateConverter;
import ru.edu.util.DistanceCalculator;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/CarWash-service/carWash", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CarWashController {

    private static final int CAR_WASH_DISPLAY_AMOUNT = 3;
    private final CarWashService service;
    private final UserInfoService userInfoService;

    @Autowired
    private CarWashForUserMapper mapperForUser;

    @Autowired
    private CarWashListMapper mapperList;
    //private CarWashListMapperManual mapperList;

    @Operation(summary = "Get all carWash located in the user's city which have vacant positions in the schedule for the requested date")
    @GetMapping(path = "/all-for-user-on-date")
    public ResponseEntity<List<CarWashForUserDto>> getAllCarWashsForUser(@RequestParam("idUser") Long idUser, @RequestParam("date") String stringDate) {
        try {
            UserInfo userInfo = userInfoService.findById(idUser);
            log.info("getting userInfo: {}", userInfo);

            Long idCity = userInfo.getCity().getId();

            DateConverter dateConverter = new DateConverter();
            Instant instantDate = dateConverter.StringToInstant(stringDate);

            List<CarWash> carWasheList = service.getFreeCarWashesByCityByDate(idCity,instantDate);
            log.info("getting carWasheList: {}", carWasheList);

            List<CarWashForUserDto> listCarWashForUserDto = mapperList.toDtoList(carWasheList);

            log.info("getting listCarWashForUserDto list: {}", listCarWashForUserDto);
            return new ResponseEntity<>(listCarWashForUserDto, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all carWash located located near the given location which have vacant positions in the schedule for the requested date")
    @GetMapping(path = "/near-for-user-on-date")
    public ResponseEntity<List<CarWashForUserDto>> getNearestCarWashsByDate(@RequestParam("idUser") Long idUser,
                                                                            @RequestParam("date") String stringDate,
                                                                            @RequestParam("latitude") Double latitude,
                                                                            @RequestParam("longitude") Double longitude) {
        try {
            UserInfo userInfo = userInfoService.findById(idUser);
            log.info("getting userInfo: {}", userInfo);

            Long idCity = userInfo.getCity().getId();

            DateConverter dateConverter = new DateConverter();
            Instant instantDate = dateConverter.StringToInstant(stringDate);

            List<CarWash> carWasheList = service.getFreeCarWashesByCityByDate(idCity,instantDate);
            log.info("getting carWasheList: {}", carWasheList);

            List<CarWash> nearestCarWashList = DistanceCalculator.getNearestCarWashes(
                    latitude, longitude, carWasheList, CAR_WASH_DISPLAY_AMOUNT);

            List<CarWashForUserDto> listCarWashForUserDto = mapperList.toDtoList(nearestCarWashList);

            log.info("getting listCarWashForUserDto list: {}", listCarWashForUserDto);
            return new ResponseEntity<>(listCarWashForUserDto, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get all carWash")
    @GetMapping
    public ResponseEntity<List<CarWash>> getAllCarWashs() {
        List<CarWash> carWashes = service.findAll();
        log.info("getting CarWash list: {}", carWashes);
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
