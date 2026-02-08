# Salesforce Login Automation Framework

## Prerequisites

1. **Java JDK 11+**
   - Download: https://adoptium.net/
   - Set JAVA_HOME environment variable

2. **Apache Maven 3.8+**
   - Download: https://maven.apache.org/download.cgi
   - Add `bin` folder to PATH

## Project Structure

```
SeleniumCode/
├── pom.xml                          # Maven configuration
├── testng.xml                       # TestNG suite file
├── run-tests.bat                    # Windows batch runner
├── src/
│   ├── main/java/com/salesforce/pages/LoginPage.java
│   └── test/java/com/salesforce/tests/
│       ├── LoginValidTest.java      # Valid test cases
│       └── LoginInvalidTest.java    # Invalid test cases
```

## How to Run

### Option 1: Command Line
```bash
mvn clean test
```

### Option 2: Double-click
Run `run-tests.bat` (Windows)

### Option 3: IntelliJ IDEA
1. File → Open → Select project folder
2. Wait for Maven to download dependencies
3. Right-click `testng.xml` → Run

### Option 4: Eclipse
1. File → Import → Existing Maven Projects
2. Select project folder
3. Right-click `testng.xml` → Run As → TestNG Suite

## Update Test Data

Edit credentials in test files:
- `LoginValidTest.java` - Update VALID_USERNAME, VALID_PASSWORD
- `LoginInvalidTest.java` - Update test data as needed

## Reports

TestNG reports generated in: `target/surefire-reports/`
