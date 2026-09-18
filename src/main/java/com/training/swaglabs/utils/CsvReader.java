package com.training.swaglabs.utils;

import com.training.swaglabs.exceptions.FrameworkException;
import com.training.swaglabs.model.Customer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class CsvReader {
  private CsvReader() {}

  public static List<Customer> readCustomers(String resource) {
    try (InputStream in = CsvReader.class.getClassLoader().getResourceAsStream(resource)) {
      if (in == null) throw new FrameworkException("CSV not found: " + resource);
      try (BufferedReader br =
          new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        return br.lines().skip(1).filter(s -> !s.isBlank()).map(CsvReader::customer).toList();
      }
    } catch (Exception e) {
      if (e instanceof FrameworkException f) throw f;
      throw new FrameworkException("Could not read CSV: " + resource, e);
    }
  }

  private static Customer customer(String line) {
    String[] p = line.split(",", 4);
    if (p.length != 4) throw new FrameworkException("Invalid customer row: " + line);
    return new Customer(p[0], p[1], p[2], Arrays.asList(p[3].split("\\|")));
  }
}
