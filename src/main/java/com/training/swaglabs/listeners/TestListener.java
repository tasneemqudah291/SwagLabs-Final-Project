package com.training.swaglabs.listeners;

import com.training.swaglabs.core.DriverFactory;
import io.qameta.allure.Allure;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.*;
import org.openqa.selenium.*;
import org.testng.*;

public final class TestListener implements ITestListener {
  private static final Logger LOG = LogManager.getLogger(TestListener.class);

  public void onTestStart(ITestResult r) {
    LOG.info("START {}", r.getName());
  }

  public void onTestSuccess(ITestResult r) {
    LOG.info("PASS {}", r.getName());
  }

  public void onTestFailure(ITestResult r) {
    LOG.error("FAIL {}", r.getName(), r.getThrowable());
    captureFailure(r);
  }

  public void onTestSkipped(ITestResult r) {
    if (r.wasRetried()) {
      LOG.warn("RETRY {}", r.getName(), r.getThrowable());
      captureFailure(r);
    } else {
      LOG.info("SKIP {}", r.getName());
    }
  }

  private void captureFailure(ITestResult r) {
    try {
      byte[] png = ((TakesScreenshot) DriverFactory.get()).getScreenshotAs(OutputType.BYTES);
      Path dir = Paths.get("target/screenshots");
      Files.createDirectories(dir);
      Path file =
          dir.resolve(
              r.getName()
                  + "-"
                  + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS"))
                  + "-"
                  + java.util.UUID.randomUUID()
                  + ".png");
      Files.write(file, png);
      Allure.addAttachment(
          "Failure screenshot", "image/png", new ByteArrayInputStream(png), ".png");
    } catch (Exception e) {
      LOG.error("Could not save failure screenshot", e);
    }
  }
}
