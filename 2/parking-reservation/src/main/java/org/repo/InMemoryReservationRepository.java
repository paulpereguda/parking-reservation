package org.repo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.Reservation;

public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<String, Reservation> storage = new ConcurrentHashMap<>();

    public void save(Reservation r) { storage.put(r.getId(), r); }

    public Optional<Reservation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Reservation> findByCarId(String carId) {
        return storage.values().stream()
                .filter(r -> r.getCar().getId().equals(carId))
                .toList();
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(storage.values());
    }
}
