package ru.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.enums.Roles;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class UserInfo implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String addressMail;

    private String password;

    @Column(nullable = false)
    //@Enumerated(EnumType.ORDINAL)
    @Enumerated(EnumType.STRING)
    private Roles role;

    //private Integer IdCity;

    //@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_city", foreignKey = @ForeignKey(name = "FK_USER_CITY"))
    private City city;

    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerInfo")
    @JsonIgnore
    private List<CarWash> carWashesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userInfo")
    @JsonIgnore
    private List<TimeTable> timeTableList;
    */

    /*
    public UserInfo(UserInfoDTO userInfoDTO) {
        this.id = userInfoDTO.getId();
        this.name = userInfoDTO.getName();
        this.phone = userInfoDTO.getPhone();
        this.password = userInfoDTO.getPassword();
        this.role = userInfoDTO.getRole();
        this.addressMail = userInfoDTO.getAddressMail();
        this.city = City.builder().id(userInfoDTO.getIdCity()).build();
    }

     */
}
