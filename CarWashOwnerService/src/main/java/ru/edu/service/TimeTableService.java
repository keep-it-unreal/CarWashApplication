package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.TimeTableRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository repository;

    public Long findByIdCarWashAndDate(Long idCarWash, LocalDate date) {
        return repository.findByIdCarWashAndDate(idCarWash,date);
    }

    public void setStatuWorkCompletedForDateLowNow() {
        repository.setStatuWorkCompletedForDateLowNow();
    }

        public void setUserIsNullForDeletedCarWash(Long idCarWash) {
        repository.setUserIsNullForDeletedCarWash(idCarWash);
    }
    public void deleteAllByIdCarWash(Long idCarWash) {
        repository.deleteAllByIdCarWash(idCarWash);
    }
    public List<TimeTable> findByDateByIdCarWash(Long idCarWash, Instant date) {
        return repository.findByDateByIdCarWash(idCarWash,date);
    }

    public List<TimeTable> findAll() {
        return repository.findAll();
    }

    public TimeTable findById(TimeTableID id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("TimeTable not found, id = " + id));
    }

    public TimeTable save(TimeTable timeTable) {
        return repository.save(timeTable);
    }

    public TimeTable update(TimeTable timeTable) {
        findById(timeTable.getID());
        return repository.save(timeTable);
    }

    public void deleteById(TimeTableID id) {
        findById(id);
        repository.deleteById(id);
    }
}
