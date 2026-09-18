package com.training.swaglabs.exceptions;

import java.util.Collection;

public class ProductNotFoundException extends FrameworkException {
  public ProductNotFoundException(String product, Collection<String> available) {
    super("Product '" + product + "' was not found. Available products: " + available);
  }
}
