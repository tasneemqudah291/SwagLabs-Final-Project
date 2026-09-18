package com.training.swaglabs.pages;

import com.training.swaglabs.exceptions.FrameworkException;
import com.training.swaglabs.exceptions.ProductNotFoundException;
import com.training.swaglabs.model.Product;
import com.training.swaglabs.utils.PriceUtils;
import java.time.Duration;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class ProductsPage extends BasePage<ProductsPage> {
  private final By list = By.className("inventory_list"),
      items = By.className("inventory_item"),
      sort = By.className("product_sort_container");

  public ProductsPage() {}

  ProductsPage(WebDriver driver, Duration timeout) {
    super(driver, timeout);
  }

  protected By openMarker() {
    return list;
  }

  public List<Product> products() {
    return all(items).stream()
        .map(
            e ->
                new Product(
                    e.findElement(By.className("inventory_item_name")).getText(),
                    e.findElement(By.className("inventory_item_desc")).getText(),
                    PriceUtils.parse(
                        e.findElement(By.className("inventory_item_price")).getText())))
        .toList();
  }

  private WebElement item(String name) {
    return driver.findElements(items).stream()
        .filter(e -> e.findElement(By.className("inventory_item_name")).getText().equals(name))
        .findFirst()
        .orElseThrow(
            () ->
                new ProductNotFoundException(
                    name, products().stream().map(Product::name).toList()));
  }

  public ProductsPage add(String name) {
    return changeCart(name, "Add to cart", "Remove", 1);
  }

  public ProductsPage remove(String name) {
    return changeCart(name, "Remove", "Add to cart", -1);
  }

  private ProductsPage changeCart(String name, String before, String after, int change) {
    WebElement button = item(name).findElement(By.tagName("button"));
    String actual = button.getText();
    if (!before.equals(actual)) {
      throw new FrameworkException(
          "Cannot " + before + " '" + name + "': button currently says '" + actual + "'");
    }
    int expectedCount = cartBadge() + change;
    await(
            "Click " + before + " for '" + name + "'",
            ExpectedConditions.elementToBeClickable(button))
        .click();

    // Wait for the effect, not just the click. Re-find after React updates the DOM.
    // Never click again here: a second click can undo an add/remove operation.
    await(
        "After " + before + " for '" + name + "': expected button '" + after
            + "' and cart badge " + expectedCount,
        ExpectedConditions.refreshed(
            d -> after.equals(buttonText(name)) && cartBadge() == expectedCount));
    return this;
  }

  public String buttonText(String name) {
    return item(name).findElement(By.tagName("button")).getText();
  }

  public ProductDetailsPage open(String name) {
    item(name).findElement(By.className("inventory_item_name")).click();
    return new ProductDetailsPage().waitUntilOpen();
  }

  public ProductsPage sort(String option) {
    select(sort, option);
    return this;
  }

  public CartPage cart() {
    click(By.className("shopping_cart_link"));
    return new CartPage().waitUntilOpen();
  }

  public String url() {
    return driver.getCurrentUrl();
  }
}
