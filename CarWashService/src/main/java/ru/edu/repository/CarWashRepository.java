package ru.edu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.entity.CarWash;

import java.util.List;

@Repository
public interface CarWashRepository extends JpaRepository<CarWash,Long> {
    List<CarWash> findByCity(String city);

}
