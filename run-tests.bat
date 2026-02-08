@echo off
echo Checking prerequisites...

java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found. Please install Java 11 or higher.
    echo Download from: https://adoptium.net/
    pause
    exit /b 1
)

call mvn -v >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Maven not found. Please install Maven.
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo [INFO] Running Salesforce Login Tests...
call mvn clean test

if errorlevel 1 (
    echo [ERROR] Tests failed.
    pause
    exit /b 1
) else (
    echo [SUCCESS] Tests completed successfully.
    pause
)
