package com.training.swaglabs.pages;

import org.openqa.selenium.By;

public final class CheckoutCompletePage extends BasePage<CheckoutCompletePage> {
  private final By complete = By.className("complete-header");

  protected By openMarker() {
    return complete;
  }

  public String confirmation() {
    return text(complete);
  }

  public ProductsPage backHome() {
    click(By.id("back-to-products"));
    return new ProductsPage().waitUntilOpen();
  }
}
