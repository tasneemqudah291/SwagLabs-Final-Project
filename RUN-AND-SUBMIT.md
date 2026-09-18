# Run and submit

## Local prerequisites

Install JDK 17, Maven 3.9+ and Google Chrome. Confirm `java -version` and `mvn -version` from a terminal. Extract the whole project before running it. Git is only needed for Git commands; GitHub's Download ZIP does not contain Git history.

## Windows: run smoke with a double-click

1. Extract the downloaded ZIP with **Extract All**. Open the extracted folder that contains `pom.xml`.
2. Double-click `RUN-SMOKE.cmd` (Windows may display it as `RUN-SMOKE`).
3. When asked, enter the demo password displayed on [SauceDemo](https://www.saucedemo.com/) and press Enter. This is not your GitHub password. If `SAUCE_PASSWORD` is already set, the runner uses it.
4. Wait for Maven to finish. Chrome runs headlessly, so no browser window is expected.
5. Confirm `BUILD SUCCESS`, five tests, zero failures, zero errors and zero skipped tests. The runner saves report ZIPs in `diagnostics/` and leaves the window open. If a test fails, retain the most recent ZIP for diagnosis.

The runner archives any previous test reports before `clean`. Generated reports and diagnostic ZIPs are excluded from Git.

## PowerShell alternative

Open PowerShell in the folder containing `pom.xml`. Set the public demo password for this terminal session, then run the required suite:

```powershell
$env:SAUCE_PASSWORD = Read-Host 'Enter the password displayed on saucedemo.com'
mvn clean test "-Dheadless=true" "-DsuiteXmlFile=testng-smoke.xml"
```

After smoke passes, run the full suite and then the parallel suite, saving each report before the next `clean`:

```powershell
mvn clean test "-Dheadless=true" "-DsuiteXmlFile=testng.xml"
mvn clean test "-Dheadless=true" "-DsuiteXmlFile=testng-parallel.xml"
mvn allure:serve
```

Quote each complete `-D` argument in PowerShell, especially when it contains a filename. `SAUCE_PASSWORD` must be set again in a new terminal unless configured separately in the operating system.

## Bash alternative

From the folder containing `pom.xml`:

```sh
SAUCE_PASSWORD="<SauceDemo test password>" mvn clean test -Dheadless=true -DsuiteXmlFile=testng-smoke.xml
SAUCE_PASSWORD="<SauceDemo test password>" mvn clean test -Dheadless=true
SAUCE_PASSWORD="<SauceDemo test password>" mvn clean test -Dheadless=true -DsuiteXmlFile=testng-parallel.xml
mvn allure:serve
```

Smoke contains five methods; full and parallel suites contain 20 methods and 28 data-driven invocations before retries. Each `clean` removes previous reports, so export any evidence you need before starting a new run.

## Framework regression checks (no browser)

```powershell
mvn test "-DsuiteXmlFile=testng-framework.xml"
```

These four checks exercise delayed button/badge updates, ignored clicks and duplicate additions. They use a small DOM double and do not require Chrome or a password. Passing them does not establish that the live UI suites pass.

## GitHub upload with history

Create a GitHub repository and replace `YOUR_REPOSITORY_URL` below with its HTTPS URL:

```sh
git remote add origin YOUR_REPOSITORY_URL
git push -u origin main
git push origin --all
```

If `origin` already exists, inspect `git remote -v` first. Do not overwrite an unrelated remote or force-push. Authenticate through Git's normal browser/credential-manager flow; never put a token in source files. Uploading files manually in the GitHub UI does not preserve this local branch history.

## Jenkins

1. Install Jenkins with Pipeline, Git, JUnit, Maven Integration, and Allure Jenkins plugins. Ensure the agent has Chrome.
2. Under Manage Jenkins → Tools, configure JDK `JDK17`, Maven `Maven3`, and an Allure command-line installation.
3. Add a Jenkins **Secret text** credential with the SauceDemo test password and ID `saucedemo-test-password`. Create a Pipeline job using “Pipeline script from SCM”, select Git, enter your repository URL, choose branch `*/main`, and script path `Jenkinsfile`. Add a Jenkins credential if the repository is private.
4. Run the job with `SUITE=testng-smoke.xml` and `HEADLESS=true`. The default parameters also select smoke on its first run.
5. Inspect Checkout, Build and Test. Open the Allure report from the build page and retain the successful build URL/screenshot as evidence.

The pipeline supports Windows and Unix agents. On Windows, the Jenkins service account needs access to the configured Java, Maven and Chrome installations.

## Verify screenshots once

On a temporary local branch, change one smoke assertion to an intentionally incorrect expected value. Run smoke and confirm the failed attempt creates a PNG under `target/screenshots/` and an attachment in Allure. Restore the assertion, rerun smoke successfully, and do not submit the deliberately failing change. The listener captures both retry attempts and final failures.

## Submission

Submit the GitHub repository URL, as requested by the trainer. Make sure the trainer can access it; a private repository requires appropriate access. Confirm full-suite success, the green Jenkins smoke build and the report before calling the submission complete. Never commit `target/` or `allure-results/`.
