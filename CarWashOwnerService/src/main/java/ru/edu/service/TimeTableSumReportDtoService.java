package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.TimeTable;
import ru.edu.entity.TimeTableID;
import ru.edu.entity.TimeTableSumReportDto;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.TimeTableRepository;
import ru.edu.repository.TimeTableSumReportDtoRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableSumReportDtoService {

    private final TimeTableSumReportDtoRepository repository;

    public List<TimeTableSumReportDto> getSumReportFromAllTimeTableByOwner(Long idUser, Instant dateBegin, Instant dateEnd) {
        return repository.getSumReportFromAllTimeTableByOwner(idUser, dateBegin, dateEnd);
    }

    public List<TimeTableSumReportDto> findAll() {
        return repository.findAll();
    }

    public TimeTableSumReportDto findById(Long idCarWash) {
        return repository.findById(idCarWash).orElseThrow(() -> new ItemNotFoundException("TimeTableSumReportDtoService not found, idCarWash = " + idCarWash));
    }

    public TimeTableSumReportDto save(TimeTableSumReportDto timeTableSumReportDto) {
        return repository.save(timeTableSumReportDto);
    }

    public TimeTableSumReportDto update(TimeTableSumReportDto timeTableSumReportDto) {
        findById(timeTableSumReportDto.getIdCarWash());
        return repository.save(timeTableSumReportDto);
    }

    public void deleteById(Long idCarWash) {
        findById(idCarWash);
        repository.deleteById(idCarWash);
    }
}
