package com.training.swaglabs.pages;

import com.training.swaglabs.model.Product;
import com.training.swaglabs.utils.PriceUtils;
import java.util.*;
import org.openqa.selenium.*;

public final class CartPage extends BasePage<CartPage> {
  private final By list = By.className("cart_list"), rows = By.className("cart_item");

  protected By openMarker() {
    return list;
  }

  public List<Product> products() {
    return driver.findElements(rows).stream()
        .map(
            e ->
                new Product(
                    e.findElement(By.className("inventory_item_name")).getText(),
                    e.findElement(By.className("inventory_item_desc")).getText(),
                    PriceUtils.parse(
                        e.findElement(By.className("inventory_item_price")).getText())))
        .toList();
  }

  public List<Integer> quantities() {
    return driver.findElements(By.className("cart_quantity")).stream()
        .map(e -> Integer.parseInt(e.getText()))
        .toList();
  }

  public CartPage remove(String name) {
    row(name).findElement(By.tagName("button")).click();
    wait.until(d -> d.findElements(rows).stream().noneMatch(e -> e.getText().contains(name)));
    return this;
  }

  private WebElement row(String name) {
    return driver.findElements(rows).stream()
        .filter(e -> e.getText().contains(name))
        .findFirst()
        .orElseThrow();
  }

  public ProductsPage continueShopping() {
    click(By.id("continue-shopping"));
    return new ProductsPage().waitUntilOpen();
  }

  public CheckoutInformationPage checkout() {
    click(By.id("checkout"));
    return new CheckoutInformationPage().waitUntilOpen();
  }
}
