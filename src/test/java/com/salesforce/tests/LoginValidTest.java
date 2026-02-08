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

public class LoginValidTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private static final String BASE_URL = "https://login.salesforce.com/?locale=in";
    private static final String VALID_USERNAME = "validuser@example.com";
    private static final String VALID_PASSWORD = "ValidPassword123";

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

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.performLogin(VALID_USERNAME, VALID_PASSWORD);
        Assert.assertTrue(loginPage.isHomePageDisplayed() || !loginPage.getPageTitle().contains("Login"),
            "User should be redirected to home page after successful login");
    }

    @Test(priority = 2, description = "Verify login page UI elements are displayed")
    public void testLoginPageUIElements() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "All login page elements should be visible");
        String pageTitle = loginPage.getPageTitle();
        Assert.assertTrue(pageTitle.contains("Login") || pageTitle.contains("Salesforce"),
            "Page title should contain Login or Salesforce");
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login.salesforce.com"), "URL should contain login.salesforce.com");
    }

    @Test(priority = 3, description = "Verify login with remember me checkbox selected")
    public void testLoginWithRememberMe() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");
        loginPage.enterUsername(VALID_USERNAME);
        loginPage.enterPassword(VALID_PASSWORD);
        loginPage.clickRememberMeCheckbox();
        Assert.assertTrue(loginPage.isRememberMeChecked(), "Remember me checkbox should be selected");
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isHomePageDisplayed() || !loginPage.getPageTitle().contains("Login"),
            "User should be redirected to home page after successful login with remember me");
    }
}
