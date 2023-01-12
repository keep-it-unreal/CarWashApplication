package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.entity.TimeInterrupt;
import ru.edu.entity.TimeInterruptID;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CityRepository;
import ru.edu.repository.TimeInterruptRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeInterruptService {

    private final TimeInterruptRepository repository;

    public List<TimeInterrupt> findAll() {
        return repository.findAll();
    }

    public TimeInterrupt findById(TimeInterruptID id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("TimeInterrupt not found, id = " + id));
    }

    public TimeInterrupt save(TimeInterrupt timeInterrupt) {
        return repository.save(timeInterrupt);
    }

    public TimeInterrupt update(TimeInterrupt timeInterrupt) {
        findById(timeInterrupt.getTimeInterruptID());
        return repository.save(timeInterrupt);
    }

    public void deleteById(TimeInterruptID id) {
        findById(id);
        repository.deleteById(id);
    }
}
