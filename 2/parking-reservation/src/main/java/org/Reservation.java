package org;

import static org.ReservationStatus.ACTIVE;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Reservation {

    private final String id = UUID.randomUUID().toString();
    private Car car;
    private DateRange period;
    private final BigDecimal price;
    private ReservationStatus status = ACTIVE;

    public Reservation(Car car, DateRange period, BigDecimal price) {
        this.car = car;
        this.period = period;
        this.price = price;
    }

    public void cancel() { this.status = ReservationStatus.CANCELLED; }


}
