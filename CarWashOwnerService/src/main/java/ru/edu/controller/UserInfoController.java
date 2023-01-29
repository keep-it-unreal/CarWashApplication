package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.entity.City;
import ru.edu.entity.UserInfo;
import ru.edu.entity.dto.UserInfoDto;
import ru.edu.entity.enums.Roles;
import ru.edu.entity.mapper.UserInfoMapper;
import ru.edu.entity.mapper.UserInfoMapperManual;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.service.UserInfoService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/admin-service/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserInfoController {

    @Autowired
    //private UserInfoMapper mapper;
    private UserInfoMapperManual mapper;

    private final UserInfoService service;

    @Operation(summary = "Get all userInfo")
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUserInfos() {
        List<UserInfo> userInfos = service.findAll();
        log.info("getting userInfo list: {}", userInfos);
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
     * Проверить, зарегистрирован ли в сервисе user с таким именем и телефоном
     * После получения из базы UserInfo дополнительно проверяется, что роль USER
     * @param userInfoDTO - UserInfoDTO
     * @return ResponseEntity<UserInfo> details (HttpStatus.OK в случае успешной проверки)
     */
    @PostMapping(path = "/login/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDto> getUserInfoByNameAndPhone(@RequestBody UserInfoDto userInfoDTO) {

        UserInfo userInfo = mapper.toModel(userInfoDTO);
        userInfo = service.findByNameAndPhone(userInfo.getName(), userInfo.getPhone());

        HttpHeaders headers = new HttpHeaders();

        try {

            if (userInfo.getRole() == Roles.USER) {
                UserInfoDto userInfoDtoNew = mapper.toDTO(userInfo);
                headers = new HttpHeaders();
                headers.add("Location", "/api/v1/admin-service/userInfo/" + userInfoDtoNew.getId());
                return new ResponseEntity<>(userInfoDtoNew, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception ex) {
            log.info("User = {} not found, ex = {} ", userInfoDTO, ex);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /***
     * Проверить, зарегистрирован ли в сервисе owner с таким именем и телефоном
     * После получения из базы UserInfo дополнительно проверяется, что роль OWNER
     * @param userInfoDTO - UserInfoDTO
     * @return ResponseEntity<UserInfo> details (HttpStatus.OK в случае успешной проверки)
     */
    @PostMapping(path = "/login/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDto> getOwnerInfoByNameAndPhone(@RequestBody UserInfoDto userInfoDTO) {

        UserInfo userInfo = mapper.toModel(userInfoDTO);
        try {
            userInfo = service.findByNameAndPhone(userInfo.getName(), userInfo.getPhone());

            HttpHeaders headers = new HttpHeaders();
            if (userInfo.getRole() == Roles.OWNER) {
                UserInfoDto userInfoDtoNew = mapper.toDTO(userInfo);
                headers = new HttpHeaders();
                headers.add("Location", "/api/v1/admin-service/userInfo/" + userInfoDtoNew.getId());
                return new ResponseEntity<>(userInfoDtoNew, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /***
     * Зарегистрировать в сервисе user
     * Установить ему роль USER
     * @param userInfoDTO - UserInfoDTO
     * @return ResponseEntity<UserInfo> details (HttpStatus.OK в случае успешной регистрации)
     */
    @PostMapping(path = "/register/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDto> createUserInfo(@RequestBody UserInfoDto userInfoDTO) {
    //public UserInfoDto createUserInfo(@RequestBody UserInfoDto userInfoDTO) {

        UserInfo userInfo = mapper.toModel(userInfoDTO);
        userInfo.setRole(Roles.USER);
        userInfo = service.save(userInfo);
        UserInfoDto userInfoDtoNew = mapper.toDTO(userInfo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/userInfo/" + userInfoDtoNew.getId());
        return new ResponseEntity<>(userInfoDtoNew, HttpStatus.CREATED);
        //return userInfoDtoNew;
    }


    /***
     * Зарегистрировать в сервисе owner
     * Установить ему роль OWNER
     * @param userInfoDTO - UserInfoDTO
     * @return ResponseEntity<UserInfo> details (HttpStatus.OK в случае успешной регистрации)
     */
    @PostMapping(path = "/register/owner", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDto> createOwnerInfo(@RequestBody UserInfoDto userInfoDTO) {

        UserInfo userInfo = mapper.toModel(userInfoDTO);
        userInfo.setRole(Roles.OWNER);
        userInfo = service.save(userInfo);
        UserInfoDto userInfoDtoNew = mapper.toDTO(userInfo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/admin-service/userInfo/" + userInfoDtoNew.getId());
        return new ResponseEntity<>(userInfoDtoNew, HttpStatus.CREATED);
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
