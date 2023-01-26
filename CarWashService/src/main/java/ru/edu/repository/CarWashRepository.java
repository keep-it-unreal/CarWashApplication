package ru.edu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edu.entity.CarWash;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface CarWashRepository extends JpaRepository<CarWash,Long> {
    List<CarWash> findByCity(String city);

    @Query(value = "select * from car_wash cw where " +
            "cw.id_city = :idCity and  exists ( " +
            "select * from time_table tt  where " +
            "tt.id_car_wash = cw.id_car_wash and  cast(tt.date_table as date) = :date and " +
            "tt.status_free = 0 )",
            nativeQuery = true)
    Collection<CarWash> findVacantCarWashByUserIdAndAtDate(@Param("idCity") Long idCity,
                                                           @Param("date") Date date);


}
