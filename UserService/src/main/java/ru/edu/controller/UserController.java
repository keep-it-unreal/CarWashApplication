package ru.edu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.dao.dto.UserInfoDTO;
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

    @GetMapping("/findById/{id}")
    public UserInfo findById(@PathVariable Long id){
        UserInfo user = userService.findById(id);
        return user;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> login(@RequestBody UserInfoDTO userInfoDTO){
        UserInfo userInfo = userService.findByNameAndPhone(userInfoDTO.getName(), userInfoDTO.getPhone());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public List<UserInfo> getAll(){
        return userService.findAll();
    }

    @PostMapping(value = "/register/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> registerUser(@RequestBody UserInfoDTO userDTO){
        UserInfo userInfo = new UserInfo(userDTO);
        userInfo.setCity(cityService.findById(userDTO.getIdCity()));
        userInfo.setRole(UserRoles.USER);
        userInfo = userService.save(userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
    }
    @PostMapping(value = "/register/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> registerOwner(@RequestBody UserInfoDTO userDTO){
        UserInfo userInfo = new UserInfo(userDTO);
        userInfo.setCity(cityService.findById(userDTO.getIdCity()));
        userInfo.setRole(UserRoles.OWNER);
        userInfo = userService.save(userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> updateUser(@RequestBody UserInfoDTO userDTO){
        UserInfo userInfo = new UserInfo(userDTO);
        userInfo.setCity(cityService.findById(userDTO.getIdCity()));
        userInfo = userService.updateUser(userInfo);

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{userId}")
    public ResponseEntity<UserInfo> deleteById(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}