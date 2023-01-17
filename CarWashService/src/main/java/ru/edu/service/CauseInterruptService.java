package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.CauseInterrupt;
import ru.edu.entity.City;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CauseInterruptRepository;
import ru.edu.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CauseInterruptService {

    private final CauseInterruptRepository repository;

    public List<CauseInterrupt> findAll() {
        return repository.findAll();
    }

    public CauseInterrupt findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("CauseInterrupt not found, id = " + id));
    }

    public CauseInterrupt save(CauseInterrupt causeInterrupt) {
        return repository.save(causeInterrupt);
    }

    public CauseInterrupt update(CauseInterrupt causeInterrupt) {
        findById(causeInterrupt.getId());
        return repository.save(causeInterrupt);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
