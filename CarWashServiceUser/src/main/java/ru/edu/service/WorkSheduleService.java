package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.entity.WorkShedule;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CityRepository;
import ru.edu.repository.WorkSheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkSheduleService {

    private final WorkSheduleRepository repository;

    public List<WorkShedule> findAll() {
        return repository.findAll();
    }

    public WorkShedule findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("WorkShedule not found, id = " + id));
    }

    public WorkShedule save(WorkShedule workShedule) {
        return repository.save(workShedule);
    }

    public WorkShedule update(WorkShedule workShedule) {
        findById(workShedule.getId());
        return repository.save(workShedule);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
