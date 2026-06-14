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


public class RegisterSeleniumTest {

    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080/register");
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
            new WebDriverWait(driver, TIMEOUT)
            .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-registrazione")));
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------

    @Test
    void pageLoadsWithCorrectTitle() {

        WebElement titleElement = driver.findElement(By.id("titolo-registrazione"));

        assertEquals("Registrazione a SkillShare", titleElement.getText());
    }

    @Test
    void usernameFieldIsPresent() {

        WebElement usernameField = driver.findElement(By.id("input-username"));

        assertTrue(usernameField.isDisplayed());
        assertEquals("username", usernameField.getAttribute("value"));
    }

    @Test
    void passwordFieldIsPresent() {

        WebElement passwordField = driver.findElement(By.id("input-password"));

        assertTrue(passwordField.isDisplayed());
        assertEquals("password", passwordField.getAttribute("value"));
    }

    @Test
    void confirmPasswordFieldIsPresent() {

        WebElement confirmPasswordField = driver.findElement(By.id("input-confirm-password"));

        assertTrue(confirmPasswordField.isDisplayed());
        assertEquals("conferma password", confirmPasswordField.getAttribute("value"));
    }

    @Test
    void confirmRegisterButtonIsPresent(){
        WebElement registerButton = driver.findElement(By.id("register-button"));
        assertTrue(registerButton.isDisplayed());
        assertEquals("CREA ACCOUNT", registerButton.getText());
    }

    @Test
    void clickingAnnullaNavigatesToLogin() {
        WebElement cancelButton = driver.findElement(By.id("cancel-button"));
        assertTrue(cancelButton.isDisplayed());
        assertEquals("ANNULLA", cancelButton.getText());

        cancelButton.click();

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement loginTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-login")));
        assertTrue(loginTitle.isDisplayed());
    }

    @Test
    void registerWithAlreadyUsedUsernameShowsError() {
        executeRegistration("usato", "password", "password");

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Username già usato", alert.getText());
        alert.accept();
    }

    @Test
    void registerWithInvalidConfirmPasswordShowsError() {
        executeRegistration("admin", "password", "pasword");

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Password non conforme", alert.getText());
        alert.accept();
    }

    @Test
    void registrationShowsSuccessMessageAndLoginPageButton() {
        executeRegistration("admin", "password", "password");

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("register-button")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("cancel-button")));
        WebElement loginPageButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bottone-pagina-login")));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        assertTrue(loginPageButton.isDisplayed());
        assertEquals("Registrazione Completata", alert.getText());
        alert.accept();
    }

    @Test
    void clickingLoginPageButtonNavigatesToLogin() {
        executeRegistration("admin", "password", "password");

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement loginPageButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bottone-pagina-login")));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());

        alert.accept();
        loginPageButton.click();

        WebElement loginTitle = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-login")));
        assertTrue(loginTitle.isDisplayed());
    }


    // -------------------------------------------------------------------------
    // HELPER
    // -------------------------------------------------------------------------
    // aspetta che la pagina sia caricata
    private void executeRegistration(String username, String password, String confirm_password){
        WebElement usernameField = driver.findElement(By.id("input-username"));
        WebElement passwordField = driver.findElement(By.id("input-password"));
        WebElement confirmPasswordField = driver.findElement(By.id("input-confirm-password"));
        WebElement registerButton = driver.findElement(By.id("register-button"));
        
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(confirm_password);
        
        registerButton.click();
    }
}