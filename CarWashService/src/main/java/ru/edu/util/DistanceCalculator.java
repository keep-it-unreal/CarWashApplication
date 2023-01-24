package ru.edu.util;

import org.springframework.stereotype.Component;
import ru.edu.entity.CarWash;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Component
public class DistanceCalculator {

    public List<CarWash> getNearestCarWashes(double pointLatitude,
                                                    double pointLongitude,
                                                    List<CarWash> carWashList,
                                                    int carWashAmount) {
        double pointLatitudeRad = convertGradusesToRadians(pointLatitude);
        double pointLongitudeRad = convertGradusesToRadians(pointLongitude);
        SortedMap<Integer, CarWash> distanceToCarWashMap = new TreeMap<>();
        for (CarWash carWash : carWashList) {
            distanceToCarWashMap.put(calculateDistance(pointLatitudeRad, pointLongitudeRad,
                    convertGradusesToRadians(carWash.getLatitude()), convertGradusesToRadians(carWash.getLongitude())),
                    carWash);
        }
        List<CarWash> nearestCarWashList = new ArrayList<>();
        while (nearestCarWashList.size() < Math.min(carWashAmount, carWashList.size())) {
            nearestCarWashList.add(distanceToCarWashMap.remove(distanceToCarWashMap.firstKey()));
        }
        return nearestCarWashList;
    }

    public double convertGradusesToRadians(double graduses) {
        return Math.toRadians(graduses);
    }

    private int calculateDistance(double lat1, double long1, double lat2, double long2) {
        double radiusOfEarth = 6372.795; //radius of Earth in km
        return ((Double) (Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.cos(long2 - long1)) * radiusOfEarth)).intValue();
    }


}
