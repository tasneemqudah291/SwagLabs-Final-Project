package com.training.swaglabs.tests;

import com.training.swaglabs.core.BaseTest;
import com.training.swaglabs.pages.*;
import io.qameta.allure.*;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

@Epic("Swag Labs")
@Feature("Login")
public class LoginTests extends BaseTest {
  @Epic("Swag Labs")
  @Feature("Login")
  @Test(groups = "smoke")
  @Story("Successful login")
  @Severity(SeverityLevel.BLOCKER)
  public void standardUserCanLogIn() {
    ProductsPage p = signIn();
    Assert.assertTrue(p.url().endsWith("/inventory.html"), "Inventory URL should be open");
    Assert.assertEquals(p.header(), "Products", "Products heading should be shown");
  }

  @Epic("Swag Labs")
  @Feature("Login")
  @Test(dataProvider = "rejectedLogins", dataProviderClass = TestData.class, groups = "regression")
  @Story("Rejected login")
  @Severity(SeverityLevel.CRITICAL)
  public void rejectedLoginShowsTheRightError(String u, String p, String expected) {
    Assert.assertTrue(
        openLoginPage().attemptLogin(u, p).error().contains(expected),
        "Error should explain why login was rejected");
  }

  @Epic("Swag Labs")
  @Feature("Login")
  @Test(groups = "regression")
  @Story("Login UI")
  @Severity(SeverityLevel.NORMAL)
  public void loginPageRendersCorrectly() {
    LoginPage p = openLoginPage();
    SoftAssert s = new SoftAssert();
    s.assertTrue(p.usernameVisible(), "Username should be visible");
    s.assertTrue(p.passwordVisible(), "Password should be visible");
    s.assertTrue(p.loginVisible(), "Login button should be visible");
    s.assertTrue(p.logoVisible(), "Logo should be visible");
    s.assertAll();
  }

  @Epic("Swag Labs")
  @Feature("Login")
  @Test(groups = "regression", dependsOnMethods = "standardUserCanLogIn")
  @Story("Logout")
  @Severity(SeverityLevel.CRITICAL)
  public void userCanLogOut() {
    Assert.assertTrue(signIn().logout().loginVisible(), "Login page should reopen after logout");
  }
}
