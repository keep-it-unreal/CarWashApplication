package ru.edu.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.dao.dto.UserInfoDTO;


@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_city", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_CITY"))
    private City city;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    public UserInfo(UserInfoDTO userInfoDTO) {
        this.id = userInfoDTO.getId();
        this.name = userInfoDTO.getName();
        this.phone = userInfoDTO.getPhone();
        this.password = userInfoDTO.getPassword();
        this.role = userInfoDTO.getRole();
        this.email = userInfoDTO.getEmail();
    }
}
