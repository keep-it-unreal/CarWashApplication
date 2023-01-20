package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.dao.entity.City;
import ru.edu.dao.repository.CityRepository;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City findById(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new RuntimeException("City not found, cityId = " + cityId));
    }

}
