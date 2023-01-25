package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.dto.TimeTableDTO;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusWork;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.TimeTableRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final TimeTableRepository repository;

    public List<TimeTable> findAll() {
        return repository.findAll();
    }

    public List<TimeTable> findVacantAtDate(Date date, Integer carWashId) {
        return repository.getByCarWashIdAndDate(date, carWashId);
    }

    public List<TimeTable> getActiveOrdersByUser(Long idUser) {
        return repository.getActiveOrdersByUser(idUser);
    }

    public List<TimeTable> getAllOrdersByUser(Long idUser) {
        return repository.getAllOrdersByUser(idUser);
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

    public void createOrDeleteByUser(TimeTableDTO timeTableDTO) {
        repository.createByUser(timeTableDTO.getDateTable(),
                timeTableDTO.getIdCarWash(),
                timeTableDTO.getStatusFree().getValue(),
                timeTableDTO.getStatusWork().getValue(),
                timeTableDTO.getIdUser());
    }
}
