package org.repo;

import java.util.List;
import java.util.Optional;

import org.Reservation;

public interface ReservationRepository {
    void save(Reservation reservation);
    Optional<Reservation> findById(String id);
    List<Reservation> findByCarId(String carId);
    List<Reservation> findAll();
}
