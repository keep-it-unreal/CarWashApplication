package ru.edu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.edu.entity.CarWash;
import ru.edu.entity.TimeTableSumReportDto;

import java.time.Instant;
import java.util.List;


public interface CarWashRepository extends JpaRepository<CarWash,Long> {

    @Query(value = "select * from car_wash cw " +
            "where " +
            "cw.id_owner = :id_user " +
            "order by address",
            nativeQuery = true)
    List<CarWash> findByIdOwner(@Param("id_user") Long idUser);


}
