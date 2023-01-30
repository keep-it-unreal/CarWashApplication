package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edu.entity.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City,Long> {
    List<City> findAllByOrderByName();
}
