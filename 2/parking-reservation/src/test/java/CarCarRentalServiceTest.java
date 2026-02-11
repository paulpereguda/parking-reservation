import static org.CarType.SEDAN;
import static org.CarType.SUV;
import static org.CarType.VAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.CarStock;
import org.CarRentalService;
import org.CarType;
import org.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pricingStrategy.DefaultPricingStrategy;
import org.repo.InMemoryReservationRepository;

class CarCarRentalServiceTest {

    private CarRentalService service;
    CarStock stock = new CarStock(Map.of(
            SEDAN, 1,
            SUV, 1,
            VAN, 1
    ));

    @BeforeEach
    void setup() {
        service = new CarRentalService(stock, new InMemoryReservationRepository(), new DefaultPricingStrategy());
    }

    @Test
    void shouldReserveCarWhenAvailable() {
        Optional<Reservation> result = service.reserve(SEDAN, LocalDateTime.now(), 3);
        assertTrue(result.isPresent());
        assertEquals(SEDAN, result.get().getCar().getCarType());
    }

    @Test
    void shouldNotReserveWhenNoCarsAvailable() {
        LocalDateTime now = LocalDateTime.now();

        assertTrue(service.reserve(SEDAN, LocalDateTime.now(), 5).isPresent());
        assertTrue(service.reserve(SEDAN, now.plusDays(1), 3).isEmpty());
    }

    @Test
    void shouldAllowReservationAfterPeriodEnds() {
        LocalDateTime now = LocalDateTime.now();

        assertTrue(service.reserve(SEDAN, LocalDateTime.now(), 2).isPresent());
        assertTrue(service.reserve(SEDAN, now.plusDays(2), 2).isPresent());
    }

    @Test
    void shouldHandleDifferentCarTypesIndependently() {
        LocalDateTime now = LocalDateTime.now();

        assertTrue(service.reserve(SEDAN, LocalDateTime.now(), 3).isPresent());
        assertTrue(service.reserve(SUV, LocalDateTime.now(), 3).isPresent());
        assertTrue(service.reserve(VAN, LocalDateTime.now(), 3).isPresent());
    }

    @Test
    void shouldCalculateCorrectPrice() {
        var repo = new InMemoryReservationRepository();
        var service = new CarRentalService(stock, repo, new DefaultPricingStrategy());

        var res = service.reserve(CarType.SUV, LocalDateTime.now(), 2).get();
        assertEquals(BigDecimal.valueOf(160), res.getPrice());
    }

    @Test
    void cancelledReservationFreesCar() {
        var res = service.reserve(CarType.SEDAN, LocalDateTime.now(), 3).get();
        service.cancelReservation(res.getId());

        assertTrue(service.reserve(CarType.SEDAN, LocalDateTime.now().plusDays(1), 2).isPresent());
    }

    @Test
    void shouldNotDoubleBookSameCarConcurrently() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Boolean> task = () ->
                service.reserve(CarType.SEDAN, LocalDateTime.now(), 2).isPresent();

        var results = executor.invokeAll(List.of(task, task));
        long successCount = results.stream().filter(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).count();

        assertEquals(1, successCount);
    }



}
