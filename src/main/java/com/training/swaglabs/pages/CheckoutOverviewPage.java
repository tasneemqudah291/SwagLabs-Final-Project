package com.training.swaglabs.pages;

import com.training.swaglabs.utils.PriceUtils;
import java.math.BigDecimal;
import org.openqa.selenium.By;

public final class CheckoutOverviewPage extends BasePage<CheckoutOverviewPage> {
  private final By summary = By.className("summary_info"),
      item = By.className("summary_subtotal_label"),
      tax = By.className("summary_tax_label"),
      total = By.className("summary_total_label");

  protected By openMarker() {
    return summary;
  }

  public BigDecimal itemTotal() {
    return PriceUtils.parse(text(item));
  }

  public BigDecimal tax() {
    return PriceUtils.parse(text(tax));
  }

  public BigDecimal total() {
    return PriceUtils.parse(text(total));
  }

  public ProductsPage cancel() {
    click(By.id("cancel"));
    return new ProductsPage().waitUntilOpen();
  }

  public CheckoutCompletePage finish() {
    scroll(By.id("finish"));
    click(By.id("finish"));
    return new CheckoutCompletePage().waitUntilOpen();
  }
}
