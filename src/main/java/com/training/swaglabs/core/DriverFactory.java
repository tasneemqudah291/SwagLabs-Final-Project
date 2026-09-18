package com.training.swaglabs.core;

import com.training.swaglabs.config.Config;
import com.training.swaglabs.exceptions.FrameworkException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

  private DriverFactory() {}

  public static void create() {
    if (!"chrome".equalsIgnoreCase(Config.require("browser")))
      throw new FrameworkException("Unsupported browser: " + Config.require("browser"));
    ChromeOptions o = new ChromeOptions();
    if (Config.headless()) o.addArguments("--headless=new");
    o.addArguments("--window-size=1440,1000", "--disable-dev-shm-usage", "--no-sandbox");
    DRIVER.set(new ChromeDriver(o));
  }

  public static WebDriver get() {
    WebDriver d = DRIVER.get();
    if (d == null) throw new FrameworkException("No WebDriver was created for this thread");
    return d;
  }

  public static void quit() {
    WebDriver d = DRIVER.get();
    if (d != null) {
      try {
        d.quit();
      } finally {
        DRIVER.remove();
      }
    }
  }
}
