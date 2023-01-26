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

    @Query(value = "select cw from time_table tt " +
            "join car_wash cw on tt.id_car_wash = cw.id_car_wash " +
            "where tt.date_table = :date and tt.status_free = 1 and tt.id_user = :userId", nativeQuery=true)
    Collection<CarWash> findVacantCarWashByUserIdAndAtDate(@Param("userId") Long userId,
                                                           @Param("date") Date date);


}
