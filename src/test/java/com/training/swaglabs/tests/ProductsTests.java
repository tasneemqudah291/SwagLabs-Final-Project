package com.training.swaglabs.tests;

import com.training.swaglabs.core.BaseTest;
import com.training.swaglabs.exceptions.ProductNotFoundException;
import com.training.swaglabs.model.Product;
import com.training.swaglabs.pages.*;
import io.qameta.allure.*;
import java.util.*;
import org.testng.*;
import org.testng.annotations.*;

@Epic("Swag Labs")
@Feature("Products")
public class ProductsTests extends BaseTest {
  @Epic("Swag Labs")
  @Feature("Products")
  @Test(groups = "smoke")
  @Story("Catalogue")
  @Severity(SeverityLevel.CRITICAL)
  public void allProductsAreListed() {
    List<Product> p = signIn().products();
    Assert.assertEquals(p.size(), 6, "Catalogue should contain six products");
    Assert.assertTrue(
        p.stream().allMatch(x -> x.price().signum() > 0), "Every product price should be positive");
  }

  @Epic("Swag Labs")
  @Feature("Products")
  @Test(groups = "smoke")
  @Story("Cart badge")
  @Severity(SeverityLevel.CRITICAL)
  public void addingAndRemovingUpdatesTheCartBadge() {
    ProductsPage p = signIn();
    Assert.assertEquals(p.cartBadge(), 0, "Badge starts empty");
    p.add("Sauce Labs Backpack");
    Assert.assertEquals(p.cartBadge(), 1, "Badge should become one");
    p.add("Sauce Labs Bike Light");
    Assert.assertEquals(p.cartBadge(), 2, "Badge should become two");
    p.remove("Sauce Labs Backpack");
    Assert.assertEquals(p.cartBadge(), 1, "Badge should decrease");
    Assert.assertEquals(
        p.buttonText("Sauce Labs Bike Light"), "Remove", "Added item should show Remove");
  }

  @Epic("Swag Labs")
  @Feature("Products")
  @Test(groups = "regression")
  @Story("Price sorting")
  @Severity(SeverityLevel.NORMAL)
  public void sortingByPriceLowToHigh() {
    List<Product> p = signIn().sort("Price (low to high)").products();
    List<Product> sorted = new ArrayList<>(p);
    Collections.sort(sorted);
    Assert.assertEquals(p, sorted, "Prices should be ascending");
  }

  @Epic("Swag Labs")
  @Feature("Products")
  @Test(groups = "regression")
  @Story("Name sorting")
  @Severity(SeverityLevel.NORMAL)
  public void sortingByNameZtoA() {
    List<String> names =
        signIn().sort("Name (Z to A)").products().stream().map(Product::name).toList();
    List<String> sorted = new ArrayList<>(names);
    sorted.sort(Comparator.reverseOrder());
    Assert.assertEquals(names, sorted, "Names should be reverse alphabetical");
  }

  @Epic("Swag Labs")
  @Feature("Products")
  @Test(groups = "regression")
  @Story("Product details")
  @Severity(SeverityLevel.NORMAL)
  public void productDetailsMatchTheCatalogue() {
    ProductsPage page = signIn();
    Product expected = page.products().get(0), actual = page.open(expected.name()).product();
    Assert.assertEquals(actual, expected, "Details should match catalogue");
  }

  @Epic("Swag Labs")
  @Feature("Products")
  @Test(
      groups = "regression",
      expectedExceptions = ProductNotFoundException.class,
      expectedExceptionsMessageRegExp = ".*Missing Product.*Available products.*")
  @Story("Missing product")
  @Severity(SeverityLevel.MINOR)
  public void unknownProductRaisesAClearError() {
    signIn().add("Missing Product");
  }
}
