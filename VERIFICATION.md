# Verification status — 2026-09-18

- PASS: Maven test-compile after final corrections (Java 17).
- PASS: 20 test methods; required Allure annotations on every method.
- PASS: POM and three suite XML files parse successfully.
- PASS: no Thread.sleep or Selenium locators in test classes.
- PASS: five feature branches merged into main; more than eight commits.
- BLOCKED: final smoke execution could not create Chrome sessions in this environment (SessionNotCreatedException: Chrome instance exited). No live UI pass is claimed.
- PENDING: full and parallel browser suites, screenshot attachment verification, and a green Jenkins smoke build with a viewable Allure report.
- PASS: project prepared for public GitHub upload; the SauceDemo password is supplied through `SAUCE_PASSWORD` and is not stored in the repository.

An initial smoke attempt exposed that setup hooks were excluded by group filtering. BeforeSuite, BeforeClass and BeforeMethod now use alwaysRun=true. The subsequent run executed those hooks and reached Chrome session creation, where the environment failed. The final source also improves retry screenshot capture and E2E money/basket assertions; it was recompiled successfully after these edits.

See RUN-AND-SUBMIT.md for execution, Jenkins setup and Git upload commands.
