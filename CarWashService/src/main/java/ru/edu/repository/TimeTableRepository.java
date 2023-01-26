package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableID> {

    // для выбранной мойки на выбранную дату уже добавлялись записи в расписание (да >0, нет -0)
    long countByIdIdCarWashAndIdDateTable(Long idCarWash, Instant dBegin);

    @Query(value = "select * from time_table " +
            "where id_car_wash = :carWashId and cast(date_table as date) = cast(:date as date) and status_free = 0 " +
            "order by date_table", nativeQuery = true)
    List<TimeTable> getByCarWashIdAndDate(@Param("date") Date date, @Param("carWashId") Integer carWashId);

    @Query(value = "select * from time_table " +
            "where id_user = :idUser and date_table >= now() order by date_table", nativeQuery = true)
    List<TimeTable> getActiveOrdersByUser(@Param("idUser") Long idUser);

    @Query(value = "select * from time_table " +
            "where id_user = :idUser and date_table >= now() and status_work = 1 order by date_table", nativeQuery = true)
    List<TimeTable> getActiveOrdersByUserAndStatusPlanned(@Param("idUser") Long idUser);

    @Query(value = "select * from time_table " +
            "where id_user = :idUser order by date_table", nativeQuery = true)
    List<TimeTable> getAllOrdersByUser(@Param("idUser") Long idUser);

    @Modifying
    @Transactional
    @Query(value = "insert into time_table (date_table, id_car_wash, status_free, status_work, id_user) " +
            "values (:dateTable,:idCarWash,:statusFree,:statusWork,:idUser) " +
            "ON CONFLICT (date_table, id_car_wash) DO UPDATE SET date_table=:dateTable, id_car_wash=:idCarWash, status_free=:statusFree, " +
            "status_work=:statusWork, id_user=:idUser", nativeQuery = true)
    void createByUser(@Param("dateTable") Instant dateTable,
                                   @Param("idCarWash") Long idCarWash,
                                   @Param("statusFree") Integer statusFree,
                                   @Param("statusWork") Integer statusWork,
                                   @Param("idUser") Long idUser);

}
