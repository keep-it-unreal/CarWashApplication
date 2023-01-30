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
import java.util.Optional;


public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableID> {

    /***
     * Для выбранной мойки на выбранную дату уже добавлялись записи в расписание (да >0, нет -0)
     * @param idCarWash - id мойки
     * @param date  - выбранная дата
     * @return - кол-во временных интервалов, существующих для данной мойки на указанную дату
     */
    @Query(value = "select count(*) from time_table tt " +
            "where " +
            "tt.id_car_wash = :id_car_wash and " +
            "cast(tt.date_table as date) = cast(:date as date)",
            nativeQuery = true)
    Long findByIdCarWashAndDate(@Param("id_car_wash") Long idCarWash, @Param("date") LocalDate date);

    /***
     * Для всех заказов, дата которых больше текущей и status_work=PLANNED установить status_work=COMPLETED
     * @return void
     */
    @Query(value = "update time_table tt " +
            "set status_work=2 " +
            "where " +
            "tt.status_work = 1 and " +
            "tt.date_table <= now()",
            nativeQuery = true)
    Optional<Void> setStatuWorkCompletedForDateLowNow();

    /***
     * Для выбранной мойки показать все заказы на дату
     * @param date
     * @param idCarWash
     * @return
     */
    @Query(value = "select * from time_table tt " +
            "where " +
            "cast(tt.date_table as date) = cast(:selectedDate as date) and " +
            "tt.id_car_wash = :id_car_wash and " +
            "tt.status_free = 1 " +
            "order by tt.date_table",
            nativeQuery = true)
    List<TimeTable> findByDateByIdCarWash(@Param("id_car_wash") Long idCarWash,@Param("selectedDate") Instant date);

    /***
     * Для всех записей, относящихся к удаляемой мойке id_user=null
     * @return void
     */
    @Query(value = "update time_table tt " +
            "set id_user=null " +
            "where " +
            "tt.id_car_wash = :id_car_wash",
            nativeQuery = true)
    Optional<Void> setUserIsNullForDeletedCarWash(@Param("id_car_wash") Long idCarWash);

    /***
     * Удалить все записи, относящиеся к выбранной мойке (при удалении мойки)
     */
    @Query(value = "delete time_table tt " +
            "where " +
            "tt.id_car_wash = :id_car_wash",
            nativeQuery = true)
    Optional<Void> deleteAllByIdCarWash(@Param("id_car_wash") Long idCarWash);

}
