package it.unibo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSeleniumTest {

    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080/");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private static WebDriver driver;

    // -------------------------------------------------------------------------
    // SETUP
    // -------------------------------------------------------------------------
    @BeforeAll
    static void startDriver() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equalsIgnoreCase(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        // Selenium Manager (bundled since 4.6) downloads chromedriver automatically
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterAll
    static void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void loadApp() {
        driver.get(BASE_URL);
        waitForApp();
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------

    @Test
    void pageLoadsWithCorrectTitle() {
        // 1. Cerchiamo l'elemento del titolo tramite l'ID
        WebElement titleElement = driver.findElement(By.id("titolo-login"));
        // 2. Verifichiamo che il TESTO dell'elemento sia quello richiesto dalla User
        // Story
        assertEquals("ACCESSO A SKILLSHARE", titleElement.getText());
    }

    @Test
    void usernameFieldIsPresent() {
        // Cerchiamo username tramide id
        WebElement usernameField = driver.findElement(By.id("input-username"));
        // Verifichiamo che il campo username sia presente
        assertTrue(usernameField.isDisplayed());
        assertEquals("username", usernameField.getAttribute("value"));
    }

    @Test
    void passwordFieldIsPresent() {
        // Cerchiamo password tramite id
        WebElement passwordField = driver.findElement(By.id("input-password"));
        // Verifichiamo che il campo password sia presente
        assertTrue(passwordField.isDisplayed());
        assertEquals("password", passwordField.getAttribute("value"));
    }

    @Test
    void clickingLoginNavigatesToHome() {
        WebElement loginButton = driver.findElement(By.id("btn-login"));
        assertTrue(loginButton.isDisplayed());
        assertEquals("Login", loginButton.getText());

        // simulazione click
        loginButton.click();

        // verifico interfaccia cambiata in home
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement homeTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-home")));
        assertTrue(homeTitle.isDisplayed());
    }

    @Test
    void clickingRegisterNavigatesToRegister() {
        WebElement registerButton = driver.findElement(By.id("btn-register"));
        assertTrue(registerButton.isDisplayed());
        assertEquals("Register", registerButton.getText());

        // simulazione click
        registerButton.click();

        // verifico interfaccia cambiata in register
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement registerTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-register")));
        assertTrue(registerTitle.isDisplayed());
    }

    // -------------------------------------------------------------------------
    // HELPER
    // -------------------------------------------------------------------------
    /** Waits until the GWT app has finished bootstrapping (input is clickable). */
    private WebElement waitForApp() {
        return new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-login")));
    }

}