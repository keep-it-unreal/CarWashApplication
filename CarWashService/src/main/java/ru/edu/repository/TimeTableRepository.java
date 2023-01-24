package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableID> {

    // для выбранной мойки на выбранную дату уже добавлялись записи в расписание (да >0, нет -0)
    long countByIdIdCarWashAndIdDateTable(Long idCarWash, Instant dBegin);

    //todo: и здесь уточнить насчет правильности запросов
    @Query(value = "select tt from time_table tt " +
            "where tt.car_wash_id = :carWashId and date_trunc('day',tt.date_table) = date_trunc('day',':date')", nativeQuery=true)
    List<TimeTable> getByCarWashIdAndDate(Date date, Integer carWashId);

    @Query(value = "select tt from time_table tt " +
            "where tt.user_info_id_user = :idUser and date_trunc('day',tt.date_table) >= date_trunc('day',now())", nativeQuery=true)
    List<TimeTable> getActiveOrdersByUser(Long idUser);

    @Query(value = "select tt from time_table tt " +
            "where tt.user_info_id_user = :idUser", nativeQuery=true)
    List<TimeTable> getAllOrdersByUser(Long idUser);

    @Query(value = "delete from time_table tt " +
            "where date_trunc('minute',tt.date_table) = date_trunc('minute',':date') and tt.car_wash_id = :carWashId and tt.user_info_id_user = :idUser", nativeQuery=true)
    TimeTable abandonOrder(Date date, Long carWashId, Integer idUser);


}
