package com.training.swaglabs.pages;

import com.training.swaglabs.config.Config;
import com.training.swaglabs.core.DriverFactory;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public abstract class BasePage<T extends BasePage<T>> {
  protected final WebDriver driver = DriverFactory.get();
  protected final WebDriverWait wait = new WebDriverWait(driver, Config.timeout());

  protected abstract By openMarker();

  @SuppressWarnings("unchecked")
  public T waitUntilOpen() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(openMarker()));
    return (T) this;
  }

  protected WebElement visible(By by) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
  }

  protected List<WebElement> all(By by) {
    return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
  }

  protected void click(By by) {
    wait.until(ExpectedConditions.elementToBeClickable(by)).click();
  }

  protected void type(By by, String text) {
    WebElement e = visible(by);
    e.clear();
    e.sendKeys(text);
  }

  protected String text(By by) {
    return visible(by).getText();
  }

  protected boolean displayed(By by) {
    return !driver.findElements(by).isEmpty() && driver.findElement(by).isDisplayed();
  }

  protected void select(By by, String text) {
    new Select(visible(by)).selectByVisibleText(text);
  }

  protected void scroll(By by) {
    ((JavascriptExecutor) driver)
        .executeScript("arguments[0].scrollIntoView({block:'center'})", visible(by));
  }

  public String header() {
    return text(By.className("title"));
  }

  public int cartBadge() {
    List<WebElement> b = driver.findElements(By.className("shopping_cart_badge"));
    return b.isEmpty() ? 0 : Integer.parseInt(b.get(0).getText());
  }

  public LoginPage logout() {
    click(By.id("react-burger-menu-btn"));
    click(By.id("logout_sidebar_link"));
    return new LoginPage().waitUntilOpen();
  }
}
