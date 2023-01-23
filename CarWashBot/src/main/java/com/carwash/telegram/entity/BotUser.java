package com.carwash.telegram.entity;


import com.carwash.telegram.entity.enums.BotUserStepService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotUser implements Serializable {

    private Long idUser;

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String phone;

    @Column(name = "is_login", nullable = false)
    private boolean islogin;

    @Column(name = "id_city")
    private Long idCity;

    @Enumerated(EnumType.ORDINAL)
    private BotUserStepService stepService;

    @Column(name = "id_car_wash")
    private Long idCarWash;

    //предварительно проверили, что дата конвентируется в Instant без ошибок
    @Column(name = "date_order")
    private String dateOrder;

    @Column(name = "date_table")
    private String dateTable;

}