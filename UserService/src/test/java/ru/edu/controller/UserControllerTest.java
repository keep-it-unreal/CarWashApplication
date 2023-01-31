package ru.edu.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.edu.dao.dto.UserInfoDTO;
import ru.edu.dao.entity.City;
import ru.edu.dao.entity.UserInfo;
import ru.edu.dao.entity.UserRoles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests must be run separately
 */
@SpringBootTest
class UserControllerTest {

    @Autowired
    UserController controller;
    ResponseEntity<?> response;

    /**
     * This test checks method findById(Long id).
     */
    @Test
    void findById() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(1L);
        controller.registerOwner(userInfoDTO);
        response = controller.findById(1L);
        UserInfo userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getName()).isEqualTo("Ivan");
    }

    /**
     * This test checks method findByName(String userName).
     */
    @Test
    void findByName() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(1L);
        controller.registerOwner(userInfoDTO);
        response = controller.findByName("Ivan");
        UserInfo userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getRole()).isEqualTo(UserRoles.OWNER);
    }

    /**
     * This test checks method loginUser(UserInfoDTO userInfoDTO).
     */
    @Test
    void loginUser() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(1L);
        controller.registerUser(userInfoDTO);
        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setName("Ivan");
        userInfoDTO1.setPhone("1-123-123-12-12");
        response = controller.loginUser(userInfoDTO1);
        UserInfo userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getCity().getName()).isEqualTo("Moscow");
    }

    /**
     * This test checks method loginOwner(UserInfoDTO userInfoDTO).
     */
    @Test
    void loginOwner() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(2L);
        controller.registerOwner(userInfoDTO);
        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setName("Ivan");
        userInfoDTO1.setPhone("1-123-123-12-12");
        response = controller.loginOwner(userInfoDTO1);
        UserInfo userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getCity().getName()).isEqualTo("London");
    }

    /**
     * This test checks method getAllUsers().
     */
    @Test
    void getAllUsers() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(2L);
        controller.registerOwner(userInfoDTO);
        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setName("Denis");
        userInfoDTO1.setPhone("1-123-123-13-13");
        userInfoDTO1.setIdCity(3L);
        controller.registerUser(userInfoDTO1);
        response = controller.getAllUsers();
        List<UserInfo> list = (List<UserInfo>) response.getBody();
        assertThat(list.size()).isEqualTo(2);
    }

    /**
     * This test checks method getAllCities().
     */
    @Test
    void getAllCities() {
        response = controller.getAllCities();
        List<City> list = (List<City>) response.getBody();
        assertThat(list.size()).isEqualTo(3);
    }

    /**
     * This test checks method registerUser(UserInfoDTO userInfoDTO).
     */
    @Test
    void registerUser() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(1L);
        controller.registerUser(userInfoDTO);
        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setName("Ivan");
        userInfoDTO1.setPhone("1-123-123-12-12");
        response = controller.loginUser(userInfoDTO1);
        UserInfo userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getCity().getName()).isEqualTo("Moscow");
    }

    /**
     * This test checks method registerOwner(UserInfoDTO userInfoDTO).
     */
    @Test
    void registerOwner() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(2L);
        controller.registerOwner(userInfoDTO);
        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setName("Ivan");
        userInfoDTO1.setPhone("1-123-123-12-12");
        response = controller.loginOwner(userInfoDTO1);
        UserInfo userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getCity().getName()).isEqualTo("London");
    }

    /**
     * This test checks method updateUser(UserInfoDTO userInfoDTO).
     */
    @Test
    void updateUser() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(2L);
        response = controller.registerOwner(userInfoDTO);
        UserInfo userInfo = (UserInfo) response.getBody();
        userInfoDTO.setIdCity(1L);
        userInfoDTO.setId(userInfo.getId());
        response = controller.updateUser(userInfoDTO);
        userInfo = (UserInfo) response.getBody();
        assertThat(userInfo.getCity().getName()).isEqualTo("Moscow");
    }

    /**
     * This test checks method deleteById(Long id).
     */
    @Test
    void deleteById() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setName("Ivan");
        userInfoDTO.setPhone("1-123-123-12-12");
        userInfoDTO.setIdCity(2L);
        response = controller.registerOwner(userInfoDTO);
        UserInfo userInfo = (UserInfo) response.getBody();
        response = controller.deleteById(userInfo.getId());
        int status = response.getStatusCodeValue();
        assertThat(status).isEqualTo(200);
    }
}