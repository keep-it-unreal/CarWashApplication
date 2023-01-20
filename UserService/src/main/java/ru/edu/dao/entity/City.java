package ru.edu.dao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CITY")
@Getter @Setter
@RequiredArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
}
