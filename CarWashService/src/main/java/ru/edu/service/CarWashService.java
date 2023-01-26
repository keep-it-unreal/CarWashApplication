package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.CarWash;
import ru.edu.entity.UserInfo;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CarWashRepository;
import ru.edu.util.DistanceCalculator;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarWashService {

    private final CarWashRepository repository;
    private final DistanceCalculator distanceCalculator;

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
        return repository.save(carWash);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public Collection<CarWash> findVacantCarWashByUserIdAndAtDate(Long idUser, Date date){
        return repository.findVacantCarWashByUserIdAndAtDate(idUser, date);
    }
    public Collection<CarWash> findNearCarWashByUserIdAndDateAndCoordinates(Long idUser, Date date, Double latitude, Double longitude){
        Collection<CarWash> carWashes = findVacantCarWashByUserIdAndAtDate(idUser, date);
        return distanceCalculator.getNearestCarWashes(latitude,longitude, carWashes.stream().toList(), 3);
    }
}
