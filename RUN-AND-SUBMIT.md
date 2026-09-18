# Run and submit

## Local prerequisites

Install JDK 17, Maven 3.9+, Git and Google Chrome. Confirm `java -version`, `mvn -version`, and `git --version` from a terminal. Unzip the whole project, keeping the included `.git` directory so the feature branches and commit history remain available.

From the folder containing `pom.xml`:

```sh
SAUCE_PASSWORD="<SauceDemo test password>" mvn clean test -Dheadless=true -DsuiteXmlFile=testng-smoke.xml
SAUCE_PASSWORD="<SauceDemo test password>" mvn clean test -Dheadless=true
SAUCE_PASSWORD="<SauceDemo test password>" mvn clean test -Dheadless=true -DsuiteXmlFile=testng-parallel.xml
mvn allure:serve
```

Smoke contains five methods; full and parallel suites contain 20 methods and 28 data-driven invocations before retries. Each `clean` removes previous reports, so export any evidence you need before starting a new run.

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
