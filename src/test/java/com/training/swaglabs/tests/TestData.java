package com.training.swaglabs.tests;

import com.training.swaglabs.config.Config;
import com.training.swaglabs.utils.CsvReader;
import org.testng.annotations.DataProvider;

public final class TestData {
  private TestData() {}

  @DataProvider(name = "rejectedLogins")
  public static Object[][] rejected() {
    return new Object[][] {
      {Config.require("locked.user"), Config.require("password"), "locked out"},
      {"bad_user", Config.require("password"), "Username and password"},
      {Config.require("standard.user"), "bad_password", "Username and password"},
      {"", Config.require("password"), "Username is required"},
      {Config.require("standard.user"), "", "Password is required"}
    };
  }

  @DataProvider(name = "missingCheckoutFields")
  public static Object[][] missing() {
    return new Object[][] {
      {"", "Ali", "11111", "First Name is required"},
      {"Ahmed", "", "11111", "Last Name is required"},
      {"Ahmed", "Ali", "", "Postal Code is required"}
    };
  }

  @DataProvider(name = "customers")
  public static Object[][] customers() {
    return CsvReader.readCustomers("testdata/customers.csv").stream()
        .map(c -> new Object[] {c})
        .toArray(Object[][]::new);
  }
}
