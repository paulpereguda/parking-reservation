package org.pricingStrategy;

import java.math.BigDecimal;

import org.CarType;

public interface PricingStrategy {
    BigDecimal calculatePrice(CarType type, int days);
}
