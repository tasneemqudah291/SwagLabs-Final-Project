package com.training.swaglabs.listeners;

import org.testng.*;

public final class RetryAnalyzer implements IRetryAnalyzer {
  private int attempts;

  public boolean retry(ITestResult result) {
    return attempts++ < 1;
  }
}
