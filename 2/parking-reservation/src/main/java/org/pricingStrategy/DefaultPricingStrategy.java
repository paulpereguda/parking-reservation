package org.pricingStrategy;

import java.math.BigDecimal;
import java.util.Map;

import org.CarType;

public class DefaultPricingStrategy implements PricingStrategy {

    private static final Map<CarType, BigDecimal> DAILY_RATE = Map.of(
            CarType.SEDAN, BigDecimal.valueOf(50),
            CarType.SUV, BigDecimal.valueOf(80),
            CarType.VAN, BigDecimal.valueOf(100)
    );

    @Override
    public BigDecimal calculatePrice(CarType type, int days) {
        return DAILY_RATE.get(type).multiply(BigDecimal.valueOf(days));
    }
}
