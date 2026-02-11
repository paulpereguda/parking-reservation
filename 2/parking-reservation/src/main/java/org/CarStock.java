package org;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class CarStock {

    private final Map<CarType, List<Car>> cars = new EnumMap<>(CarType.class);

    public CarStock(Map<CarType, Integer> carAmount) {
        carAmount.forEach((key, value) -> {
            List<Car> list = new ArrayList<>();
            IntStream.range(0, value).forEach(t -> list.add(new Car(UUID.randomUUID().toString(), key)));
            cars.put(key, list);
        });
    }

    public List<Car> getCarsByType(CarType carType) {
        return cars.getOrDefault(carType, Collections.emptyList());
    }
}
