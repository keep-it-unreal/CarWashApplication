package ru.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class CauseInterrupt implements Serializable {

    @Id
    @Column(name = "id_cause")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "causeInterrupt")
    //private List<TimeInterrupt> timeInterruptList;

}
