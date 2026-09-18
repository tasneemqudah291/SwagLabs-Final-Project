package com.training.swaglabs.config;

import com.training.swaglabs.exceptions.FrameworkException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Properties;

public final class Config {
  private static final Properties VALUES = new Properties();

  static {
    try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (in == null) throw new FrameworkException("config.properties was not found");
      VALUES.load(in);
    } catch (Exception e) {
      if (e instanceof FrameworkException f) throw f;
      throw new FrameworkException("Could not load configuration", e);
    }
  }

  private Config() {}

  public static String get(String key) {
    String override = System.getProperty(key);
    if (override != null) return override;

    String value = VALUES.getProperty(key);
    if (value != null && value.startsWith("${") && value.endsWith("}")) {
      String environmentVariable = value.substring(2, value.length() - 1);
      String environmentValue = System.getenv(environmentVariable);
      if (environmentValue != null && !environmentValue.isBlank()) return environmentValue;
      throw new FrameworkException("Missing environment variable: " + environmentVariable);
    }
    return value;
  }

  public static String require(String key) {
    String value = get(key);
    if (value == null || value.isBlank())
      throw new FrameworkException("Missing configuration: " + key);
    return value;
  }

  public static String baseUrl() {
    return require("base.url");
  }

  public static Duration timeout() {
    return Duration.ofSeconds(Long.parseLong(require("timeout")));
  }

  public static boolean headless() {
    return Boolean.parseBoolean(require("headless"));
  }

  public static BigDecimal taxRate() {
    return new BigDecimal(require("tax.rate"));
  }
}
