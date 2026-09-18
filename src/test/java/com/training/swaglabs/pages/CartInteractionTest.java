package com.training.swaglabs.pages;

import com.training.swaglabs.exceptions.FrameworkException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/** Browser-free regression checks for delayed DOM updates and ignored clicks. */
public class CartInteractionTest {
  private static final String PRODUCT = "Sauce Labs Backpack";

  @Test
  public void addWaitsForBothButtonAndBadgeWithoutClickingTwice() {
    DelayedCart cart = new DelayedCart(false, false);
    cart.page().add(PRODUCT);
    Assert.assertTrue(cart.inCart, "Add must finish before the next page action");
    Assert.assertEquals(cart.count, 1, "The badge may update after the button");
    Assert.assertEquals(cart.clicks, 1, "A repeated click would remove the product");
  }

  @Test
  public void removeWaitsForTheBadgeToDisappearWithoutClickingTwice() {
    DelayedCart cart = new DelayedCart(true, false);
    cart.page().remove(PRODUCT);
    Assert.assertFalse(cart.inCart);
    Assert.assertEquals(cart.count, 0);
    Assert.assertEquals(cart.clicks, 1);
  }

  @Test
  public void anIgnoredClickFailsAtTheAddStep() {
    DelayedCart cart = new DelayedCart(false, true);
    TimeoutException error =
        Assert.expectThrows(TimeoutException.class, () -> cart.page().add(PRODUCT));
    Assert.assertTrue(error.getMessage().contains("After Add to cart"));
    Assert.assertTrue(error.getMessage().contains(PRODUCT));
    Assert.assertTrue(error.getMessage().contains("/inventory.html"));
    Assert.assertEquals(cart.count, 0);
    Assert.assertEquals(cart.clicks, 1);
  }

  @Test
  public void addingAnExistingItemDoesNotToggleItOutOfTheCart() {
    DelayedCart cart = new DelayedCart(true, false);
    Assert.expectThrows(FrameworkException.class, () -> cart.page().add(PRODUCT));
    Assert.assertEquals(cart.count, 1);
    Assert.assertEquals(cart.clicks, 0);
  }

  // A small DOM double: the button changes first, then the cart badge. No
  // browser or SauceDemo requests are made by these four framework tests.
  private static final class DelayedCart {
    private boolean inCart;
    private int count;
    private int clicks;
    private int buttonPolls;
    private int badgePolls;
    private boolean pending;
    private boolean desired;
    private final boolean ignoreClicks;

    private DelayedCart(boolean inCart, boolean ignoreClicks) {
      this.inCart = inCart;
      this.count = inCart ? 1 : 0;
      this.ignoreClicks = ignoreClicks;
    }

    private ProductsPage page() {
      WebElement name = proxy(WebElement.class, (p, m, a) -> {
        if (m.getName().equals("getText")) return PRODUCT;
        throw unsupported(m.getName());
      });
      WebElement button = proxy(WebElement.class, (p, m, a) -> {
        switch (m.getName()) {
          case "getText":
            if (pending && ++buttonPolls >= 2) inCart = desired;
            return inCart ? "Remove" : "Add to cart";
          case "isDisplayed":
          case "isEnabled":
            return true;
          case "click":
            clicks++;
            if (!ignoreClicks) {
              pending = true;
              desired = !inCart;
            }
            return null;
          default:
            throw unsupported(m.getName());
        }
      });
      WebElement row = proxy(WebElement.class, (p, m, a) -> {
        if (m.getName().equals("findElement")) {
          if (a[0].equals(By.className("inventory_item_name"))) return name;
          if (a[0].equals(By.tagName("button"))) return button;
        }
        throw unsupported(m.getName());
      });
      WebElement badge = proxy(WebElement.class, (p, m, a) -> {
        if (m.getName().equals("getText")) return Integer.toString(count);
        throw unsupported(m.getName());
      });
      WebDriver driver = proxy(WebDriver.class, (p, m, a) -> {
        if (m.getName().equals("getCurrentUrl")) return "https://example.test/inventory.html";
        if (m.getName().equals("findElements")) {
          if (a[0].equals(By.className("inventory_item"))) return List.of(row);
          if (a[0].equals(By.className("shopping_cart_badge"))) {
            if (pending && ++badgePolls >= 3) count = desired ? 1 : 0;
            return count == 0 ? List.of() : List.of(badge);
          }
        }
        throw unsupported(m.getName());
      });
      return new ProductsPage(driver, Duration.ofSeconds(ignoreClicks ? 1 : 5));
    }

    private static UnsupportedOperationException unsupported(String name) {
      return new UnsupportedOperationException("DOM double does not support " + name);
    }
  }

  private static <T> T proxy(Class<T> type, InvocationHandler handler) {
    return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
        (p, m, a) -> {
          if (m.getDeclaringClass() == Object.class) {
            return switch (m.getName()) {
              case "toString" -> "Test " + type.getSimpleName();
              case "hashCode" -> System.identityHashCode(p);
              case "equals" -> p == a[0];
              default -> throw new UnsupportedOperationException(m.getName());
            };
          }
          return handler.invoke(p, m, a);
        }));
  }
}
