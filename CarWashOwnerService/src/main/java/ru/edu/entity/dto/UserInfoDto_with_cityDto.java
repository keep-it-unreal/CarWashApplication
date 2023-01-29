package ru.edu.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.enums.Roles;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto_with_cityDto {

    private Long id;

    private String name;

    private String phone;

    private String addressMail;

    private String password;

    private Roles role;

    private CityDto city;
    //private Long idCity;

    //private List<CarWash> carWashesList;

    //private List<TimeTable> timeTableList;

    /*
    public UserInfoDTO(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.name = userInfo.getName();
        this.phone = userInfo.getPhone();
        this.password = userInfo.getPassword();
        this.role = userInfo.getRole();
        this.addressMail = userInfo.getAddressMail();
        this.IdCity = userInfo.getCity().getId();
    }
     */
}
