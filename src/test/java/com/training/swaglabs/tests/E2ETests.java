package com.training.swaglabs.tests;

import com.training.swaglabs.config.Config;
import com.training.swaglabs.core.BaseTest;
import com.training.swaglabs.model.CartSummary;
import com.training.swaglabs.model.Customer;
import com.training.swaglabs.model.Product;
import com.training.swaglabs.pages.*;
import io.qameta.allure.*;
import java.util.List;
import org.testng.*;
import org.testng.annotations.*;

@Epic("Swag Labs")
@Feature("End to end")
public class E2ETests extends BaseTest {
  @Epic("Swag Labs")
  @Feature("End to end")
  @Test(dataProvider = "customers", dataProviderClass = TestData.class, groups = "e2e")
  @Story("Complete purchase")
  @Severity(SeverityLevel.BLOCKER)
  public void customerCanCompleteAPurchase(Customer c) {
    ProductsPage p = signIn();
    List<Product> selected =
        p.products().stream().filter(item -> c.products().contains(item.name())).toList();
    CartSummary expected = new CartSummary(selected, Config.taxRate());
    c.products().forEach(p::add);
    CartPage cart = p.cart();
    Assert.assertEquals(
        cart.products(), selected, "Customer basket should match selected catalogue items");
    CheckoutOverviewPage overview =
        cart.checkout().continueWith(c.firstName(), c.lastName(), c.postalCode());
    Assert.assertEquals(
        overview.itemTotal(), expected.itemTotal(), "E2E subtotal should match Java calculation");
    Assert.assertEquals(overview.tax(), expected.tax(), "E2E tax should match Java calculation");
    Assert.assertEquals(
        overview.total(), expected.total(), "E2E total should match Java calculation");
    CheckoutCompletePage done = overview.finish();
    Assert.assertEquals(
        done.confirmation(), "Thank you for your order!", "Customer should complete purchase");
    Assert.assertEquals(done.cartBadge(), 0, "Completed purchase should clear basket");
  }

  @Epic("Swag Labs")
  @Feature("End to end")
  @Test(groups = "e2e")
  @Story("Cancel halfway")
  @Severity(SeverityLevel.CRITICAL)
  public void cancellingHalfwayKeepsTheBasket() {
    CartPage cart = signIn().add("Sauce Labs Backpack").add("Sauce Labs Bike Light").cart();
    List<Product> before = cart.products();
    CartPage after = cart.checkout().cancel();
    Assert.assertEquals(
        after.products(), before, "Cancelling delivery details must preserve names and prices");
    Assert.assertEquals(after.quantities(), List.of(1, 1), "Cancelling must preserve quantities");
    Assert.assertEquals(after.cartBadge(), 2, "Basket badge must remain unchanged");
  }
}
