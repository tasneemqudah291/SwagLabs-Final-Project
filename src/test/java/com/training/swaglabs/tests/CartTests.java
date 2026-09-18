package com.training.swaglabs.tests;

import com.training.swaglabs.core.BaseTest;
import com.training.swaglabs.model.Product;
import com.training.swaglabs.pages.*;
import io.qameta.allure.*;
import java.util.*;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

@Epic("Swag Labs")
@Feature("Cart")
public class CartTests extends BaseTest {
  private static final String A = "Sauce Labs Backpack", B = "Sauce Labs Bike Light";

  private CartPage cart() {
    return signIn().add(A).add(B).cart();
  }

  @Epic("Swag Labs")
  @Feature("Cart")
  @Test(groups = "smoke")
  @Story("Cart contents")
  @Severity(SeverityLevel.CRITICAL)
  public void cartShowsTheItemsThatWereAdded() {
    CartPage c = cart();
    SoftAssert s = new SoftAssert();
    s.assertEquals(
        c.products().stream().map(Product::name).toList(),
        List.of(A, B),
        "Cart names should match");
    s.assertEquals(c.quantities(), List.of(1, 1), "Each quantity should be one");
    s.assertEquals(c.products().size(), 2, "Two rows should be shown");
    s.assertAll();
  }

  @Epic("Swag Labs")
  @Feature("Cart")
  @Test(groups = "regression")
  @Story("Cart prices")
  @Severity(SeverityLevel.CRITICAL)
  public void cartPricesMatchTheCatalogue() {
    ProductsPage p = signIn();
    Map<String, Object> expected = new HashMap<>();
    p.products().forEach(x -> expected.put(x.name(), x.price()));
    List<Product> actual = p.add(A).add(B).cart().products();
    Assert.assertTrue(
        actual.stream().allMatch(x -> x.price().equals(expected.get(x.name()))),
        "Cart prices should match catalogue");
  }

  @Epic("Swag Labs")
  @Feature("Cart")
  @Test(groups = "regression")
  @Story("Remove cart item")
  @Severity(SeverityLevel.NORMAL)
  public void removingAnItemUpdatesTheCartAndTheBadge() {
    CartPage c = cart().remove(A);
    Assert.assertFalse(
        c.products().stream().anyMatch(x -> x.name().equals(A)), "Removed row should disappear");
    Assert.assertEquals(c.cartBadge(), 1, "Badge should decrease");
  }

  @Epic("Swag Labs")
  @Feature("Cart")
  @Test(groups = "regression")
  @Story("Continue shopping")
  @Severity(SeverityLevel.NORMAL)
  public void continueShoppingKeepsTheCart() {
    ProductsPage p = cart().continueShopping();
    Assert.assertEquals(p.header(), "Products", "Products page should reopen");
    Assert.assertEquals(p.cartBadge(), 2, "Cart should be preserved");
  }
}
