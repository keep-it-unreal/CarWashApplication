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
        findById(carWash.getId());
        return repository.save(carWash);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    public Collection<CarWash> findVacantCarWashByUserAndAtDate(UserInfo userInfo, Date date){
        return repository.findVacantCarWashByUserIdAndAtDate(userInfo.getId(), date);
    }
    public Collection<CarWash> findNearCarWashByUserAndDateAndCoordinates(UserInfo userInfo, Date date, Double latitude, Double longitude){
        Collection<CarWash> carWashes = findVacantCarWashByUserAndAtDate(userInfo, date);
        return distanceCalculator.getNearestCarWashes(latitude,longitude, carWashes.stream().toList(), 3);
    }
}
