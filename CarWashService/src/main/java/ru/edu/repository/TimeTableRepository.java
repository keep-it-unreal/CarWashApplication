package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.edu.entity.TimeInterrupt;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableID> {

    // для выбранной мойки на выбранную дату уже добавлялись записи в расписание (да >0, нет -0)
    long countByIdIdCarWashAndIdDateTable(Long idCarWash, Instant dBegin);
}
