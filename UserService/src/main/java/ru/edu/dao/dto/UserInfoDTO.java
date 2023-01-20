package ru.edu.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.dao.entity.UserInfo;
import ru.edu.dao.entity.UserRoles;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private String password;

    private UserRoles role;

    private Long idCity;

    public UserInfoDTO(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.name = userInfo.getName();
        this.phone = userInfo.getPhone();
        this.password = userInfo.getPassword();
        this.email= userInfo.getEmail();
        this.role = userInfo.getRole();
        this.idCity = userInfo.getCity().getId();
    }
}