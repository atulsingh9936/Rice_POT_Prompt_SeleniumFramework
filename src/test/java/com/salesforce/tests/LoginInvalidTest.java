package com.salesforce.tests;

import com.salesforce.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;

public class LoginInvalidTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private static final String BASE_URL = "https://login.salesforce.com/?locale=in";
    private static final String VALID_USERNAME = "validuser@example.com";
    private static final String INVALID_USERNAME = "invaliduser@example.com";
    private static final String INVALID_PASSWORD = "WrongPassword123";
    private static final String EMPTY_STRING = "";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.get(BASE_URL);
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1, description = "Verify login fails with invalid username and password")
    public void testInvalidUsernameAndPassword() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.performLogin(INVALID_USERNAME, INVALID_PASSWORD);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid credentials");
        String errorText = loginPage.getErrorMessageText();
        Assert.assertTrue(errorText.toLowerCase().contains("username and password") || 
                         errorText.toLowerCase().contains("login") ||
                         errorText.toLowerCase().contains("incorrect"),
            "Error message should indicate invalid credentials");
    }

    @Test(priority = 2, description = "Verify login fails with valid username and invalid password")
    public void testValidUsernameInvalidPassword() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.performLogin(VALID_USERNAME, INVALID_PASSWORD);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid password");
        String errorText = loginPage.getErrorMessageText();
        Assert.assertTrue(errorText.toLowerCase().contains("username and password") || 
                         errorText.toLowerCase().contains("login") ||
                         errorText.toLowerCase().contains("incorrect"),
            "Error message should indicate authentication failure");
    }

    @Test(priority = 3, description = "Verify login fails with empty username and password")
    public void testEmptyUsernameAndPassword() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.performLogin(EMPTY_STRING, EMPTY_STRING);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.isLoginPageLoaded(),
            "Error should be shown or user should remain on login page");
        Assert.assertTrue(loginPage.getPageTitle().contains("Login"), "User should remain on login page");
    }

    @Test(priority = 4, description = "Verify login fails with empty username")
    public void testEmptyUsername() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.performLogin(EMPTY_STRING, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "User should remain on login page with empty username");
        Assert.assertTrue(loginPage.getPageTitle().contains("Login"), "Page title should still be login page");
    }

    @Test(priority = 5, description = "Verify login fails with empty password")
    public void testEmptyPassword() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.performLogin(VALID_USERNAME, EMPTY_STRING);
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "User should remain on login page with empty password");
        Assert.assertTrue(loginPage.getPageTitle().contains("Login"), "Page title should still be login page");
    }

    @Test(priority = 6, description = "Verify login fails with SQL injection attempt in username")
    public void testSQLInjectionInUsername() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        String sqlInjection = "' OR '1'='1' --";
        loginPage.performLogin(sqlInjection, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.isLoginPageLoaded(),
            "System should handle SQL injection gracefully");
        Assert.assertFalse(loginPage.isHomePageDisplayed(), "User should not be logged in with SQL injection");
    }

    @Test(priority = 7, description = "Verify login fails with XSS attempt in username")
    public void testXSSAttemptInUsername() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        String xssPayload = "<script>alert('xss')</script>";
        loginPage.performLogin(xssPayload, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isLoginPageLoaded() || loginPage.isErrorMessageDisplayed(),
            "System should handle XSS attempt gracefully");
        Assert.assertFalse(loginPage.isHomePageDisplayed(), "User should not be logged in with XSS payload");
    }

    @Test(priority = 8, description = "Verify login fails with special characters in password")
    public void testSpecialCharactersInPassword() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        String specialCharsPassword = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        loginPage.performLogin(VALID_USERNAME, specialCharsPassword);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || loginPage.isLoginPageLoaded(),
            "Error should be shown for password with special characters");
        Assert.assertFalse(loginPage.isHomePageDisplayed(), "User should not be logged in with special character password");
    }

    @Test(priority = 9, description = "Verify login fails with very long username")
    public void testVeryLongUsername() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        String longUsername = "a".repeat(256) + "@example.com";
        loginPage.performLogin(longUsername, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isLoginPageLoaded() || loginPage.isErrorMessageDisplayed(),
            "System should handle long username gracefully");
        Assert.assertFalse(loginPage.isHomePageDisplayed(), "User should not be logged in with very long username");
    }

    @Test(priority = 10, description = "Verify login fails with incorrect email format")
    public void testIncorrectEmailFormat() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        String invalidEmail = "notanemail";
        loginPage.performLogin(invalidEmail, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isLoginPageLoaded() || loginPage.isErrorMessageDisplayed(),
            "Error should be shown for invalid email format");
        Assert.assertFalse(loginPage.isHomePageDisplayed(), "User should not be logged in with invalid email format");
    }
}
