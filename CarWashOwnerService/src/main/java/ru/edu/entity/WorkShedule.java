package ru.edu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edu.entity.enums.StatusShedule;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkShedule implements Serializable {

    @Id
    @Column(name="id_car_wash")
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule monday;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule tuesday;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule wednesday;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule thursday;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule friday;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule saturday;

    @Enumerated(EnumType.ORDINAL)
    private StatusShedule sunday;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_car_wash", referencedColumnName = "IdCarWash")
    @JsonIgnore
    private CarWash carWash;



}
