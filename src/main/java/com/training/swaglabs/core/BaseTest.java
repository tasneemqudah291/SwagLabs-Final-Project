package com.training.swaglabs.core;

import com.training.swaglabs.config.Config;
import com.training.swaglabs.exceptions.FrameworkException;
import com.training.swaglabs.pages.*;
import java.io.IOException;
import java.nio.file.*;
import org.apache.logging.log4j.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public abstract class BaseTest {
  private static final Logger LOG = LogManager.getLogger(BaseTest.class);

  @BeforeSuite(alwaysRun = true)
  public void beforeSuite() {
    Config.baseUrl();
    Config.timeout();
    try {
      Files.createDirectories(Path.of("target/screenshots"));
    } catch (IOException e) {
      throw new FrameworkException("Cannot prepare screenshot directory", e);
    }
    LOG.info("Starting suite");
  }

  @AfterSuite(alwaysRun = true)
  public void afterSuite() {
    LOG.info("Suite finished");
  }

  @BeforeClass(alwaysRun = true)
  public void beforeClass() {
    LOG.info("Starting class {}", getClass().getSimpleName());
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() {
    LOG.info("Finished class {}", getClass().getSimpleName());
  }

  @BeforeMethod(alwaysRun = true)
  public void createDriver() {
    DriverFactory.create();
  }

  @AfterMethod(alwaysRun = true)
  public void closeDriver() {
    DriverFactory.quit();
  }

  protected WebDriver driver() {
    return DriverFactory.get();
  }

  protected LoginPage openLoginPage() {
    driver().get(Config.baseUrl());
    return new LoginPage().waitUntilOpen();
  }

  protected ProductsPage signIn() {
    return openLoginPage().loginAs(Config.require("standard.user"), Config.require("password"));
  }
}
