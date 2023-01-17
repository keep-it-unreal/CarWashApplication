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
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.UserInfoDTO;
import ru.edu.service.UserInfoService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin-service/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    private final UserInfoService service;

    @Operation(summary = "Get all userInfo")
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUserInfos() {
        List<UserInfo> userInfos = service.findAll();
        LOGGER.info("getting userInfo list: {}", userInfos);
        return new ResponseEntity<>(userInfos, HttpStatus.OK);
    }

    /***
     * Method for getting userInfo details
     * @param userInfoId - userInfo id
     * @return userInfo details
     */
    @GetMapping("/{userInfoId}")
    public UserInfo getUserInfoById(@PathVariable long userInfoId) {
        return service.findById(userInfoId);
    }

    /***
     * Method get userInfo by name, phone
     * @param userInfoDTO - UserInfoDTO
     * @return ResponseEntity<UserInfo> details
     */
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDTO> getUserInfoByNameAndPhone(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfo userInfo = new UserInfo(userInfoDTO);
        UserInfoDTO userInfoDTONew = new UserInfoDTO(service.findByNameAndPhone(userInfo));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/userInfo/" + userInfoDTONew.getId());
        return new ResponseEntity<>(userInfoDTONew, HttpStatus.OK);
    }

    /***
     * Method create userInfo
     * @param userInfo - userInfo
     * @return ResponseEntity<UserInfo> details
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfo> createUserInfo(@RequestBody UserInfo userInfo) {
        UserInfo userInfoNew=service.save(userInfo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/userInfo/" + userInfo.getId());
        return new ResponseEntity<>(userInfoNew, headers, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserInfo> updateUserInfo(@RequestBody UserInfo userInfo) {
        service.update(userInfo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userInfoId}")
    public ResponseEntity<UserInfo> deleteUserInfoById(@PathVariable long userInfoId) {
        service.deleteById(userInfoId);
        return ResponseEntity.ok().build();
    }
}
