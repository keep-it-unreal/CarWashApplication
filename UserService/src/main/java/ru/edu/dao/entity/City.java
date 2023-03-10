package ru.edu.dao.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "CITY")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
}
