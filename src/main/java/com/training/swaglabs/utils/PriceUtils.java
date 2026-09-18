package com.training.swaglabs.utils;

import com.training.swaglabs.exceptions.FrameworkException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtils {
  private PriceUtils() {}

  public static BigDecimal parse(String value) {
    try {
      return new BigDecimal(value.replaceAll("[^0-9.-]", ""));
    } catch (Exception e) {
      throw new FrameworkException("Invalid price: " + value, e);
    }
  }

  public static BigDecimal round(BigDecimal value) {
    return value.setScale(2, RoundingMode.HALF_UP);
  }
}
