# Swag Labs Final Project

Java 17 UI automation framework for the complete SauceDemo purchase journey. It uses Selenium, TestNG, Allure, Page Objects, thread-local drivers, data-driven tests, automatic retry, and failure screenshots.

## Run

Requirements: Java 17, Maven, and Google Chrome.

```bash
SAUCE_PASSWORD="<SauceDemo test password>" mvn test
SAUCE_PASSWORD="<SauceDemo test password>" mvn test -Dheadless=true
SAUCE_PASSWORD="<SauceDemo test password>" mvn test -DsuiteXmlFile=testng-smoke.xml -Dheadless=true
SAUCE_PASSWORD="<SauceDemo test password>" mvn test -DsuiteXmlFile=testng-parallel.xml -Dheadless=true
mvn test -Dbase.url=https://www.saucedemo.com/ -Dtimeout=30
mvn allure:serve
```

The full suite contains 20 test methods. Configuration lives in `src/test/resources/config.properties`; matching `-D` system properties override file values. The SauceDemo password is supplied through the `SAUCE_PASSWORD` environment variable and is intentionally not committed to the repository.

## CI

The Jenkins pipeline accepts `SUITE` and `HEADLESS`, then checks out, builds, tests, and always publishes JUnit, Allure results, and screenshots.

## What I would automate next

I would add browser-matrix execution, accessibility checks, visual regression for the checkout summary, API-assisted test-data setup, and Dockerized Selenium Grid execution.

## Verification status

Maven `test-compile` passed on 2026-09-18. The 20 test methods have been written, but the live browser suites, failure screenshot attachment, Allure report and green Jenkins build still require execution. Do not treat static checks as a passing test run. Configure Jenkins tools named `JDK17` and `Maven3`, install the Allure plugin and its command-line tool, and make Chrome available on the Linux agent. Data providers produce 28 test invocations from the 20 methods.

See [RUN-AND-SUBMIT.md](RUN-AND-SUBMIT.md) for Windows/Unix Jenkins setup and Git upload steps, and [VERIFICATION.md](VERIFICATION.md) for the actual verification status.
