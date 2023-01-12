package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.City;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository repository;

    public List<City> findAll() {

        //return repository.findAll();
        return repository.findAllByOrderByName();
    }

    public City findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("City not found, id = " + id));
    }

    public City save(City city) {
        return repository.save(city);
    }

    public City update(City city) {
        findById(city.getId());
        return repository.save(city);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
