package com.training.swaglabs.listeners;

import java.lang.reflect.*;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

public final class RetryTransformer implements IAnnotationTransformer {
  public void transform(ITestAnnotation a, Class c, Constructor k, Method m) {
    a.setRetryAnalyzer(RetryAnalyzer.class);
  }
}
