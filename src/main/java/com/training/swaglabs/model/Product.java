package com.training.swaglabs.model;

import com.training.swaglabs.exceptions.FrameworkException;
import java.math.BigDecimal;
import java.util.Objects;

public final class Product implements Comparable<Product> {
  private final String name, description;
  private final BigDecimal price;

  public Product(String name, String description, BigDecimal price) {
    if (name == null
        || name.isBlank()
        || description == null
        || description.isBlank()
        || price == null
        || price.signum() < 0) throw new FrameworkException("Invalid product data");
    this.name = name;
    this.description = description;
    this.price = price;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public BigDecimal price() {
    return price;
  }

  public int compareTo(Product other) {
    return price.compareTo(other.price);
  }

  public boolean equals(Object o) {
    return o instanceof Product p
        && name.equals(p.name)
        && description.equals(p.description)
        && price.compareTo(p.price) == 0;
  }

  public int hashCode() {
    return Objects.hash(name, description, price.stripTrailingZeros());
  }

  public String toString() {
    return "Product{name='" + name + "', price=" + price + "}";
  }
}
