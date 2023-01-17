package ru.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.edu.entity.TimeInterrupt;
import ru.edu.entity.TimeInterruptID;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeInterruptRepository extends JpaRepository<TimeInterrupt, TimeInterruptID> {
/*
    //вернет >0, если в данную дату данная мойка не работает из-за исключительной ситуации
    @Query("SELECT " +
            "count(a) " +
            "from " +
            "TimeInterrupt as a" +
            "where " +
            "a.idCarWash = :idCarWash and " +
            "date(a.dateBegin) <= :dBegin1 and " +
            "(a.dateEnd is null or " +
            "date(a.dateEnd) >= :dBegin)")
    long findByTimeInterrupt_for_date(Long idCarWash, LocalDate dBegin, LocalDate dBegin1);
 */
    //List<TimeInterrupt> findByIdCarWashAndDateBeginBefore(Long idCarWash, LocalDate dBegin);

    //List<TimeInterrupt> findByIdCarWashAndDateBeginBeforeAndDateEndAfter(Long idCarWash, LocalDate dBegin, LocalDate dBegin1);

/*
    //вернет >0, если в данную дату и время данная мойка не работает из-за исключительной ситуации
    @Query("SELECT " +
            "count(a) " +
            "from " +
            "TimeInterrupt as a" +
            "where " +
            "a.idCarWash = :idCarWash and " +
            "(date(a.dateBegin) < :dBegin or date(a.dateBegin) = :dBegin1 and a.timeBegin < :tBegin) and " +
            "(a.dateEnd is null or " +
            "date(a.dateEnd) > :dBegin2 or date(a.DateEnd) = :dBegin3 and a.TimeEnd >= :tBegin1)")
    long findByTimeInterrupt_for_dateAndTime(Long idCarWash, LocalDate dBegin, LocalDate dBegin1, LocalDate dBegin2, LocalDate dBegin3, String tBegin, String tBegin1);

 */
}
