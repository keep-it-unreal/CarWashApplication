package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.dto.TimeTableDTO;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusWork;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableID> {

    // для выбранной мойки на выбранную дату уже добавлялись записи в расписание (да >0, нет -0)
    long countByIdIdCarWashAndIdDateTable(Long idCarWash, Instant dBegin);

    //todo: и здесь уточнить насчет правильности запросов
    @Query(value = "select tt from time_table tt " +
            "where tt.car_wash_id = :carWashId and date_trunc('day',tt.date_table) = date_trunc('day',':date')", nativeQuery = true)
    List<TimeTable> getByCarWashIdAndDate(@Param("date") Date date, @Param("carWashId") Integer carWashId);

    @Query(value = "select tt from time_table tt " +
            "where tt.id_user = :idUser and tt.date_table >= now()", nativeQuery = true)
    List<TimeTable> getActiveOrdersByUser(@Param("idUser") Long idUser);

    @Query(value = "select tt from time_table tt " +
            "where tt.user_info_id_user = ?1", nativeQuery = true)
    List<TimeTable> getAllOrdersByUser(@Param("idUser") Long idUser);

    @Modifying
    @Transactional
    @Query(value = "insert into time_table (date_table, id_car_wash, status_free, status_work, id_user)" +
            "values (:dateTable,:idCarWash,:statusFree,:statusWork,:idUser)" +
            "ON CONFLICT (date_table, id_car_wash) DO UPDATE SET date_table=:dateTable, id_car_wash=:idCarWash, status_free=:statusFree, " +
            "status_work=:statusWork, id_user=:idUser", nativeQuery = true)
    void createByUser(@Param("dateTable") Instant dateTable,
                                   @Param("idCarWash") Long idCarWash,
                                   @Param("statusFree") Integer statusFree,
                                   @Param("statusWork") Integer statusWork,
                                   @Param("idUser") Long idUser);

}
