package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.CarWash;
import ru.edu.entity.TimeTableSumReportDto;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CarWashRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarWashService {

    private final CarWashRepository repository;

    public List<CarWash> findByIdOwner(Long id) {
        return repository.findByIdOwner(id);
    }


    public List<CarWash> findAll() {
        return repository.findAll();
    }

    public CarWash findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("CarWash not found, id = " + id));
    }

    public CarWash save(CarWash carWash) {
        return repository.save(carWash);
    }

    public CarWash update(CarWash carWash) {
        findById(carWash.getId());
        return repository.save(carWash);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }


}
