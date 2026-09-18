package com.training.swaglabs.pages;

import com.training.swaglabs.exceptions.ProductNotFoundException;
import com.training.swaglabs.model.Product;
import com.training.swaglabs.utils.PriceUtils;
import java.util.*;
import org.openqa.selenium.*;

public final class ProductsPage extends BasePage<ProductsPage> {
  private final By list = By.className("inventory_list"),
      items = By.className("inventory_item"),
      sort = By.className("product_sort_container");

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
    return all(items).stream()
        .filter(e -> e.findElement(By.className("inventory_item_name")).getText().equals(name))
        .findFirst()
        .orElseThrow(
            () ->
                new ProductNotFoundException(
                    name, products().stream().map(Product::name).toList()));
  }

  public ProductsPage add(String name) {
    item(name).findElement(By.tagName("button")).click();
    return this;
  }

  public ProductsPage remove(String name) {
    item(name).findElement(By.tagName("button")).click();
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
