package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.TimeTableSumReportDto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TimeTableSumReportDtoRepository extends JpaRepository<TimeTableSumReportDto, Long> {

    @Query(value = "select " +
            "cw.id_car_wash, " +
            "cw.address, " +
            "cw.price, " +
            "count(tt.id_car_wash) as ch " +
            "from " +
            "car_wash cw left outer join " +
            "time_table tt on " +
            "cw.id_car_wash = tt.id_car_wash and " +
            "cast(tt.date_table as date) >= cast(:date_begin as date) and " +
            "cast(tt.date_table as date) <= cast(:date_end as date) and " +
            "not tt.id_user is null and " +
            "tt.status_work=2 " +
            "where " +
            "cw.id_owner = :id_user " +
            "group by " +
            "cw.id_car_wash, " +
            "cw.address, " +
            "cw.price " +
            "order by address",
            nativeQuery = true)
    List<TimeTableSumReportDto> getSumReportFromAllTimeTableByOwner(@Param("id_user") Long idUser, @Param("date_begin") Instant dateBegin, @Param("date_end") Instant dateEnd);



}
