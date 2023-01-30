package ru.edu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edu.entity.CarWash;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


public interface CarWashRepository extends JpaRepository<CarWash,Long> {
    List<CarWash> findByCity(String city);

    @Query(value = "select * from car_wash cw " +
            "where " +
            "cw.id_city = :id_city and " +
            "exists ( " +
            "select * from time_table tt " +
            "where " +
            "tt.id_car_wash = cw.id_car_wash and " +
            "cast(tt.date_table as date) = cast(:selectedDate as date) and " +
            "tt.status_free = 0 " +
            ")",
            nativeQuery = true)
    List<CarWash> getFreeCarWashesByCityByDate(@Param("id_city") Long idCity,
                                                @Param("selectedDate") Instant selectedDate);
}
