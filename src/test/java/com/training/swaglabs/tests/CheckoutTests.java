package com.training.swaglabs.tests;

import com.training.swaglabs.config.Config;
import com.training.swaglabs.core.BaseTest;
import com.training.swaglabs.model.*;
import com.training.swaglabs.pages.*;
import io.qameta.allure.*;
import java.util.*;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

@Epic("Swag Labs")
@Feature("Checkout")
public class CheckoutTests extends BaseTest {
  private static final String A = "Sauce Labs Backpack", B = "Sauce Labs Bike Light";

  @Epic("Swag Labs")
  @Feature("Checkout")
  @Test(
      dataProvider = "missingCheckoutFields",
      dataProviderClass = TestData.class,
      groups = "regression")
  @Story("Required delivery fields")
  @Severity(SeverityLevel.CRITICAL)
  public void missingFieldsAreRejected(String f, String l, String p, String expected) {
    String error = signIn().add(A).cart().checkout().submitInvalid(f, l, p).error();
    Assert.assertTrue(error.contains(expected), "The missing field should be named");
  }

  @Epic("Swag Labs")
  @Feature("Checkout")
  @Test(groups = "smoke")
  @Story("Order totals")
  @Severity(SeverityLevel.BLOCKER)
  public void overviewTotalsAreCorrect() {
    ProductsPage page = signIn();
    List<Product> selected =
        page.products().stream().filter(p -> List.of(A, B).contains(p.name())).toList();
    CheckoutOverviewPage o =
        page.add(A).add(B).cart().checkout().continueWith("Ahmed", "Ali", "11111");
    CartSummary expected = new CartSummary(selected, Config.taxRate());
    SoftAssert s = new SoftAssert();
    s.assertEquals(o.itemTotal(), expected.itemTotal(), "Item total should match Java calculation");
    s.assertEquals(o.tax(), expected.tax(), "Tax should match Java calculation");
    s.assertEquals(o.total(), expected.total(), "Total should match Java calculation");
    s.assertAll();
  }

  @Epic("Swag Labs")
  @Feature("Checkout")
  @Test(groups = "regression")
  @Story("Cancel checkout")
  @Severity(SeverityLevel.NORMAL)
  public void cancellingTheOrderReturnsToTheProducts() {
    ProductsPage p =
        signIn().add(A).cart().checkout().continueWith("Ahmed", "Ali", "11111").cancel();
    Assert.assertEquals(p.header(), "Products", "Cancel should return to products");
    Assert.assertEquals(p.cartBadge(), 1, "Cart should survive cancellation");
  }

  @Epic("Swag Labs")
  @Feature("Checkout")
  @Test(groups = "regression")
  @Story("Finish order")
  @Severity(SeverityLevel.BLOCKER)
  public void finishingTheOrderConfirmsIt() {
    CheckoutCompletePage c =
        signIn().add(A).cart().checkout().continueWith("Ahmed", "Ali", "11111").finish();
    Assert.assertEquals(
        c.confirmation(), "Thank you for your order!", "Confirmation should be shown");
    Assert.assertEquals(c.cartBadge(), 0, "Badge should be empty after checkout");
  }
}
