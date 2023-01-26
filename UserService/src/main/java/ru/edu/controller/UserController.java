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
@RequestMapping(value = "/api/v1/admin-service/userInfo/", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CityService cityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/findById/{id}")
    public UserInfo findById(@PathVariable Long id){
        UserInfo user = userService.findById(id);
        LOGGER.info("getting user by id: {}", user);
        return user;
    }

    @GetMapping("/getUserByName/{userName}")
    public UserInfo findByName(@PathVariable String userName){
        UserInfo user = userService.findByName(userName);
        LOGGER.info("getting user by name: {}", user);
        return user;
    }

    @GetMapping("/getUserByName/{username}")
    public UserInfo findById(@PathVariable String username){
        UserInfo user = userService.findByNameAndPhone(username,"8-910-001-01-01");
        return user;
    }

    @PostMapping(value = "/login/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginUser(@RequestBody UserInfoDTO userInfoDTO){
        try{
            UserInfo userInfo = userService.findByNameAndPhone(userInfoDTO.getName(), userInfoDTO.getPhone());
            LOGGER.info("logging by name and phone: {}", userInfo);
            if(userInfo.getRole() == UserRoles.USER) {
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.resolve(404)).body("User not found");
            }
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.resolve(404)).body(new RuntimeException(e));
        }
    }
    @PostMapping(value = "/login/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginOwner(@RequestBody UserInfoDTO userInfoDTO){
        UserInfo userInfo = userService.findByNameAndPhone(userInfoDTO.getName(), userInfoDTO.getPhone());
        LOGGER.info("logging by name and phone: {}", userInfo);
        if(userInfo.getRole() == UserRoles.OWNER) {
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.resolve(404)).body("Owner not found");
        }
    }

    @GetMapping("/getAllUsers")
    public List<UserInfo> getAllUsers(){
        List<UserInfo> list = userService.findAll();
        LOGGER.info("getting all users: {}", list);
        return list;
    }

    @GetMapping("/getAllCities")
    public List<City> getAllCities(){
        List<City> list = cityService.findAll();
        LOGGER.info("getting all cities: {}", list);
        return list;
    }

    @PostMapping(value = "/register/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> registerUser(@RequestBody UserInfoDTO userDTO){
        UserInfo userInfo = new UserInfo(userDTO);
        userInfo.setCity(cityService.findById(userDTO.getIdCity()));
        userInfo.setRole(UserRoles.USER);
        userInfo = userService.save(userInfo);
        LOGGER.info("register user: {}", userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
    }
    @PostMapping(value = "/register/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> registerOwner(@RequestBody UserInfoDTO userDTO){
        UserInfo userInfo = new UserInfo(userDTO);
        userInfo.setCity(cityService.findById(userDTO.getIdCity()));
        userInfo.setRole(UserRoles.OWNER);
        userInfo = userService.save(userInfo);
        LOGGER.info("register owner: {}", userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> updateUser(@RequestBody UserInfoDTO userDTO){
        UserInfo userInfo = new UserInfo(userDTO);
        userInfo.setCity(cityService.findById(userDTO.getIdCity()));
        userInfo = userService.updateUser(userInfo);
        LOGGER.info("updating user: {}", userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{userId}")
    public ResponseEntity<UserInfo> deleteById(@PathVariable Long userId){
        userService.deleteUser(userId);
        LOGGER.info("deleting user by id: {}", userId);

        return ResponseEntity.ok().build();
    }
}
