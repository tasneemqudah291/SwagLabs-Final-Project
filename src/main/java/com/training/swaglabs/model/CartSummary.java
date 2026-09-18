package com.training.swaglabs.model;

import com.training.swaglabs.utils.PriceUtils;
import java.math.BigDecimal;
import java.util.Collection;

public final class CartSummary {
  private final BigDecimal itemTotal, tax, total;

  public CartSummary(Collection<Product> products, BigDecimal rate) {
    itemTotal =
        PriceUtils.round(
            products.stream().map(Product::price).reduce(BigDecimal.ZERO, BigDecimal::add));
    tax = PriceUtils.round(itemTotal.multiply(rate));
    total = PriceUtils.round(itemTotal.add(tax));
  }

  public BigDecimal itemTotal() {
    return itemTotal;
  }

  public BigDecimal tax() {
    return tax;
  }

  public BigDecimal total() {
    return total;
  }
}
