package org;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import org.pricingStrategy.PricingStrategy;
import org.repo.ReservationRepository;

public class CarRentalService {

    private final CarStock inventory;
    private final ReservationRepository reservationRepo;
    private final PricingStrategy pricingStrategy;

    private final ReentrantLock lock = new ReentrantLock(); // thread safety

    public CarRentalService(CarStock inventory,
                            ReservationRepository repo,
                            PricingStrategy pricingStrategy) {
        this.inventory = inventory;
        this.reservationRepo = repo;
        this.pricingStrategy = pricingStrategy;
    }

    public Optional<Reservation> reserve(CarType type, LocalDateTime start, int days) {
        lock.lock();
        try {
            DateRange requested = new DateRange(start, days);

            for (Car car : inventory.getCarsByType(type)) {
                if (car.getStatus() == CarStatus.MAINTENANCE) continue;

                if (isAvailable(car, requested)) {
                    BigDecimal price = pricingStrategy.calculatePrice(type, days);
                    Reservation r = new Reservation(car, requested, price);
                    reservationRepo.save(r);
                    return Optional.of(r);
                }
            }
            return Optional.empty();
        } finally {
            lock.unlock();
        }
    }

    public void cancelReservation(String reservationId) {
        reservationRepo.findById(reservationId)
                .ifPresent(Reservation::cancel);
    }

    private boolean isAvailable(Car car, DateRange requested) {
        List<Reservation> reservations = reservationRepo.findByCarId(car.getId());

        return reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .noneMatch(r -> r.getPeriod().isDatesOverlaps(requested));
    }
}
