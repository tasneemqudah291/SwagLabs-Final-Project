package com.training.swaglabs.pages;

import org.openqa.selenium.By;

public final class CheckoutInformationPage extends BasePage<CheckoutInformationPage> {
  private final By first = By.id("first-name"),
      last = By.id("last-name"),
      postal = By.id("postal-code"),
      continueButton = By.id("continue"),
      error = By.cssSelector("[data-test='error']");

  protected By openMarker() {
    return continueButton;
  }

  public CheckoutOverviewPage continueWith(String f, String l, String p) {
    type(first, f);
    type(last, l);
    type(postal, p);
    click(continueButton);
    return new CheckoutOverviewPage().waitUntilOpen();
  }

  public CheckoutInformationPage submitInvalid(String f, String l, String p) {
    type(first, f);
    type(last, l);
    type(postal, p);
    click(continueButton);
    return this;
  }

  public String error() {
    return text(error);
  }

  public CartPage cancel() {
    click(By.id("cancel"));
    return new CartPage().waitUntilOpen();
  }
}
