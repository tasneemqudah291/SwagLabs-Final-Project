@echo off
setlocal
cd /d "%~dp0"
title SwagLabs smoke tests
echo SwagLabs - run the five smoke tests in headless Chrome
echo.
where java >nul 2>nul
if errorlevel 1 (
  echo Java is missing from PATH. Install JDK 17 and open this file again.
  goto setup_failed
)
where mvn >nul 2>nul
if errorlevel 1 (
  echo Maven is missing from PATH. Reopen this file after fixing PATH.
  goto setup_failed
)
if not exist "pom.xml" (
  echo Extract the whole project before opening RUN-SMOKE.cmd.
  goto setup_failed
)
if not defined SAUCE_PASSWORD (
  echo Enter the demo password displayed on https://www.saucedemo.com/
  echo This is the demo site's password, not your GitHub password.
  set /p "SAUCE_PASSWORD=Demo password: "
)
if not defined SAUCE_PASSWORD (
  echo No password was entered.
  goto setup_failed
)
rem Keep evidence from an earlier run before Maven clean removes target.
call :archive
if errorlevel 1 (
  echo Could not archive the previous reports. They have not been deleted.
  goto setup_failed
)
call mvn clean test "-Dheadless=true" "-DsuiteXmlFile=testng-smoke.xml"
set "SWAGLABS_EXIT=%ERRORLEVEL%"
call :archive
if errorlevel 1 echo Could not create a ZIP. Reports remain in target.
echo.
if "%SWAGLABS_EXIT%"=="0" (
  echo Smoke tests passed. The full suite and Jenkins still need verification.
) else (
  echo Tests did not pass. Share the latest ZIP from the diagnostics folder.
)
echo.
pause
exit /b %SWAGLABS_EXIT%

:archive
powershell.exe -NoProfile -Command "$ErrorActionPreference='Stop'; $paths=@('target\surefire-reports','target\screenshots','target\allure-results') | Where-Object { Test-Path $_ }; if ($paths) { New-Item -ItemType Directory -Force 'diagnostics' | Out-Null; $zip=Join-Path 'diagnostics' ('SwagLabs-' + (Get-Date -Format 'yyyyMMdd-HHmmssfff') + '.zip'); Compress-Archive -Path $paths -DestinationPath $zip; Write-Host ('Reports saved: ' + $zip) }"
exit /b %ERRORLEVEL%

:setup_failed
echo.
pause
exit /b 1
