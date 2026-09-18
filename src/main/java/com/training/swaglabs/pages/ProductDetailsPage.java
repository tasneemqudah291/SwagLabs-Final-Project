package com.training.swaglabs.pages;

import com.training.swaglabs.model.Product;
import com.training.swaglabs.utils.PriceUtils;
import org.openqa.selenium.By;

public final class ProductDetailsPage extends BasePage<ProductDetailsPage> {
  private final By name = By.className("inventory_details_name"),
      desc = By.className("inventory_details_desc"),
      price = By.className("inventory_details_price"),
      back = By.id("back-to-products");

  protected By openMarker() {
    return name;
  }

  public Product product() {
    return new Product(text(name), text(desc), PriceUtils.parse(text(price)));
  }

  public ProductsPage back() {
    click(back);
    return new ProductsPage().waitUntilOpen();
  }
}
