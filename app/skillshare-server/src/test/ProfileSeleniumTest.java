package it.unibo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileSeleniumTest {

    // NOTA: Assicurati che l'app sia avviata sulla porta 8080 prima di lanciare il test!
    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080/");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private static WebDriver driver;

    @BeforeAll
    static void startDriver() {
        ChromeOptions options = new ChromeOptions();
        if (!"false".equalsIgnoreCase(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
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
        // Aspettiamo che carichi il titolo del profilo
        new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-profilo")));
    }

    @Test
    void pageLoadsWithCorrectTitle() {
        WebElement titleElement = driver.findElement(By.id("titolo-profilo"));
        assertEquals("IL MIO PROFILO", titleElement.getText());
    }

    @Test
    void editButtonIsPresent() {
        WebElement editButton = driver.findElement(By.id("btn-modifica"));
        assertTrue(editButton.isDisplayed());
        assertEquals("MODIFICA PROFILO", editButton.getText());
    }

    @Test
    void avatarIsPresent() {
        WebElement avatar = driver.findElement(By.id("img-avatar"));
        assertTrue(avatar.isDisplayed());
    }

    @Test
    void userInfoIsPresent() {
        WebElement username = driver.findElement(By.id("lbl-username"));
        WebElement bio = driver.findElement(By.id("lbl-bio"));
        
        assertTrue(username.isDisplayed());
        assertTrue(bio.isDisplayed());
        
        // Verifichiamo che contengano del testo di base (placeholder)
        assertTrue(username.getText().contains("Username:"));
        assertTrue(bio.getText().contains("Biografia:"));
    }
}