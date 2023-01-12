package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.TimeInterrupt;
import ru.edu.entity.TimeInterruptID;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.TimeInterruptRepository;
import ru.edu.repository.TimeTableRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository repository;

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
