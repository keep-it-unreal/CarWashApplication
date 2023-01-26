package ru.edu.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.dao.dto.UserInfoDTO;
import ru.edu.dao.entity.City;
import ru.edu.dao.entity.UserInfo;
import ru.edu.dao.entity.UserRoles;
import ru.edu.service.CityService;
import ru.edu.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin-service/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CityService cityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/userInfo/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try{
            UserInfo user = userService.findById(id);
            LOGGER.info("getting user by id: {}", user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/userInfo/getUserByName/{userName}")
    public ResponseEntity<?> findByName(@PathVariable String userName){
        try {
            UserInfo user = userService.findByName(userName);
            LOGGER.info("getting user by name: {}", user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/userInfo/login/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestBody UserInfoDTO userInfoDTO){
        try{
            UserInfo userInfo = userService.findByNameAndPhone(userInfoDTO.getName(), userInfoDTO.getPhone());
            LOGGER.info("logging by name and phone: {}", userInfo);
            if(userInfo.getRole() == UserRoles.USER) {
                return ResponseEntity.status(HttpStatus.OK).body(userInfo);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Client with role User not found, role is: " + userInfo.getRole());
            }
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping(value = "/userInfo/login/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginOwner(@RequestBody UserInfoDTO userInfoDTO){
        try {
            UserInfo userInfo = userService.findByNameAndPhone(userInfoDTO.getName(), userInfoDTO.getPhone());
            LOGGER.info("logging by name and phone: {}", userInfo);
            if (userInfo.getRole() == UserRoles.OWNER) {
                return ResponseEntity.status(HttpStatus.OK).body(userInfo);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Client with role Owner not found, role is: " + userInfo.getRole());
            }
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/userInfo/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        try{
            List<UserInfo> list = userService.findAll();
            LOGGER.info("getting all users: {}", list);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/city")
    public ResponseEntity<?> getAllCities(){
        try {
            List<City> list = cityService.findAll();
            LOGGER.info("getting all cities: {}", list);
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/userInfo/register/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> registerUser(@RequestBody UserInfoDTO userDTO){
        try {
            UserInfo userInfo = new UserInfo(userDTO);
            userInfo.setCity(cityService.findById(userDTO.getIdCity()));
            userInfo.setRole(UserRoles.USER);
            userInfo = userService.save(userInfo);
            LOGGER.info("register user: {}", userInfo);

            return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    @PostMapping(value = "/userInfo/register/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> registerOwner(@RequestBody UserInfoDTO userDTO){
        try{
            UserInfo userInfo = new UserInfo(userDTO);
            userInfo.setCity(cityService.findById(userDTO.getIdCity()));
            userInfo.setRole(UserRoles.OWNER);
            userInfo = userService.save(userInfo);
            LOGGER.info("register owner: {}", userInfo);

            return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping(value = "/userInfo/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestBody UserInfoDTO userDTO){
        try{
            UserInfo userInfo = new UserInfo(userDTO);
            userInfo.setCity(cityService.findById(userDTO.getIdCity()));
            userInfo = userService.updateUser(userInfo);
            LOGGER.info("updating user: {}", userInfo);

            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @DeleteMapping("/userInfo/deleteById/{userId}")
    public ResponseEntity<?> deleteById(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            LOGGER.info("deleting user by id: {}", userId);

            return ResponseEntity.ok().build();
        } catch (RuntimeException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
