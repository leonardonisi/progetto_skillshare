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

public class CreateAdSeleniumTest {
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
        driver = new ChromeDriver(options);
        // CORREZIONE: Rimosso l'implicitlyWait globale per evitare conflitti con WebDriverWait
    }

    @AfterAll
    static void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void loadApp() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Nessun alert presente
        }

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Attesa esplicita per l'input username
        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));
        inputUsername.clear();
        inputUsername.sendKeys("admin");

        // Attesa esplicita per l'input password
        WebElement inputPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-password")));
        inputPassword.clear();
        inputPassword.sendKeys("password");

        // CORREZIONE: Attendi che il pulsante di login sia cliccabile prima di fare click
        WebElement btnLogin = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-login")));
        btnLogin.click();

        // CORREZIONE: Se il login richiede un'autenticazione asincrona RPC, la Home potrebbe metterci un istante a caricarsi.
        // NOTA: Assicurati che il pulsante di navigazione nella HOME si chiami davvero "btn-pubblica" 
        // e non vada in conflitto con il "btn-pubblica" interno al form.
        WebElement btnPubblicaHome = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-pubblica")));
        btnPubblicaHome.click();

        waitForApp();
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------
    @Test
    void pageLoadsWithCorrectTitle() {
        WebElement titleElement = driver.findElement(By.id("titolo-create-ad"));
        assertEquals("PUBBLICA ANNUNCIO", titleElement.getText());
    }

    @Test
    void testFormElementsArePresent() {
        assertTrue(driver.findElement(By.id("titolo-annuncio")).isDisplayed());
        assertTrue(driver.findElement(By.id("categoria-annuncio")).isDisplayed());
        assertTrue(driver.findElement(By.id("offerta-skill")).isDisplayed());
        assertTrue(driver.findElement(By.id("disponibilita")).isDisplayed());
        assertTrue(driver.findElement(By.id("ricerca-skill")).isDisplayed());
        assertTrue(driver.findElement(By.id("btn-pubblica")).isDisplayed());
    }

    @Test
    void testCampiObbligatoriMancantiMostraAvviso() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        driver.findElement(By.id("titolo-annuncio")).sendKeys("Ripetizioni Java");
        driver.findElement(By.id("btn-pubblica")).click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Devi specificare sia cosa offri sia cosa cerchi sia la disponibilità", alert.getText());
        alert.accept();
    }

    @Test
    void testPubblicazioneConSuccesso() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        driver.findElement(By.id("titolo-annuncio")).sendKeys("Ripetizioni Java");
        driver.findElement(By.id("offerta-skill")).sendKeys("Spiegazione Socket e Concorrenza");
        driver.findElement(By.id("disponibilita")).sendKeys("Venerdì pomeriggio");
        driver.findElement(By.id("ricerca-skill")).sendKeys("Esercizi diagrammi UML");

        driver.findElement(By.id("btn-pubblica")).click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Annuncio pubblicato con successo", alert.getText());
        alert.accept();
    }

    // -------------------------------------------------------------------------
    // HELPER
    // -------------------------------------------------------------------------
    private WebElement waitForApp() {
        return new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-create-ad")));
    }
}