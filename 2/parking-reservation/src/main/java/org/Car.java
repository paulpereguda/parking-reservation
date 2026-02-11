package org;

import static org.CarStatus.AVAILABLE;
import static org.CarStatus.MAINTENANCE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Car {

    private String id;
    private CarType carType;
    private CarStatus status = AVAILABLE;

    public Car(String id, CarType carType) {
        this.id = id;
        this.carType = carType;
    }

    public void sendToMaintenance() { this.status = MAINTENANCE; }
    public void markAvailable() { this.status = AVAILABLE; }


}
