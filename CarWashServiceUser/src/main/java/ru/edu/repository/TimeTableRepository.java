package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edu.entity.CarWash;
import ru.edu.entity.TimeInterrupt;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableID> {

    // для выбранной мойки на выбранную дату уже добавлялись записи в расписание (да >0, нет -0)
    long countByIdIdCarWashAndIdDateTable(Long idCarWash, Instant dBegin);

    @Query(value = "select * from time_table tt " +
            "where " +
            "tt.id_user = :id_user " +
            "order by tt.date_table",
            nativeQuery = true)
    List<TimeTable>  findByIduser(@Param("id_user") Long idUser);

    @Query(value = "select * from time_table tt " +
            "where " +
            "cast(tt.date_table as date) = cast(:selectedDate as date) and " +
            "tt.id_car_wash = :id_car_wash and " +
            "tt.status_free = 0 " +
            "order by tt.date_table",
            nativeQuery = true)
    List<TimeTable> findByDateByIdCarWash(@Param("selectedDate") Instant date,
                                               @Param("id_car_wash") Long idCarWash);

    @Query(value = "select * from time_table tt " +
            "where " +
            "tt.date_table >= now() and " +
            "tt.id_user = :id_user " +
            "order by tt.date_table",
            nativeQuery = true)
    List<TimeTable> findByIdUserAndByDatetableGreatesThanNow(@Param("id_user") Long idUser);

    @Query(value = "select * from time_table tt " +
            "where " +
            "tt.date_table >= now() and " +
            "tt.id_user = :id_user and " +
            "tt.status_work = 1 " +
            "order by tt.date_table",
            nativeQuery = true)
    List<TimeTable> findByIdUserAndByDatetableGreatesThanNowAndStatusPlanned(@Param("id_user") Long idUser);


}
