package com.training.swaglabs.model;

import java.util.List;

public record Customer(
    String firstName, String lastName, String postalCode, List<String> products) {
  public Customer {
    products = List.copyOf(products);
  }

  public String toString() {
    return firstName + " " + lastName + " (" + postalCode + ") -> " + products;
  }
}
