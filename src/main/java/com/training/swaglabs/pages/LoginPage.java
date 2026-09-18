package com.training.swaglabs.pages;

import org.openqa.selenium.By;

public final class LoginPage extends BasePage<LoginPage> {
  private final By username = By.id("user-name"),
      password = By.id("password"),
      login = By.id("login-button"),
      error = By.cssSelector("[data-test='error']"),
      logo = By.className("login_logo");

  protected By openMarker() {
    return login;
  }

  public ProductsPage loginAs(String u, String p) {
    type(username, u);
    type(password, p);
    click(login);
    return new ProductsPage().waitUntilOpen();
  }

  public LoginPage attemptLogin(String u, String p) {
    type(username, u);
    type(password, p);
    click(login);
    return this;
  }

  public String error() {
    return text(error);
  }

  public boolean usernameVisible() {
    return displayed(username);
  }

  public boolean passwordVisible() {
    return displayed(password);
  }

  public boolean loginVisible() {
    return displayed(login);
  }

  public boolean logoVisible() {
    return displayed(logo);
  }
}
