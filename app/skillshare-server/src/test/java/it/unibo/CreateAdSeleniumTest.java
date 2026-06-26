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
        // Accetta preventivamente eventuali alert rimasti appesi
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Nessun alert presente
        }

        driver.get(BASE_URL);

        // Inserisci le credenziali per superare la login
        driver.findElement(By.id("input-username")).sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        // Attende il caricamento e clicca sul pulsante "PUBBLICA" della Home
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement btnPubblicaHome = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-pubblica")));
        btnPubblicaHome.click();

        waitForApp();
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------

    @Test
    void pageLoadsWithCorrectTitle() {
        // Cerco l'elemento del titolo tramite l'ID
        WebElement titleElement = driver.findElement(By.id("titolo-create-ad"));
        // Verifico
        assertEquals("PUBBLICA ANNUNCIO", titleElement.getText());
    }

    @Test
    void testFormElementsArePresent() {
        // Verifica la presenza di tutti i widget assegnati nella task grafica
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

        // Compila solo il titolo, lasciando vuoti "offro" e "cerco"
        driver.findElement(By.id("titolo-annuncio")).sendKeys("Ripetizioni Java");

        // Clicca sul pulsante pubblica
        driver.findElement(By.id("btn-pubblica")).click();

        // Verifica la comparsa dell'alert bloccante
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Devi specificare sia cosa offri sia cosa cerchi sia la disponibilità", alert.getText());
        alert.accept();
    }

    @Test
    void testPubblicazioneConSuccesso() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Compila interamente tutti i campi del form
        driver.findElement(By.id("titolo-annuncio")).sendKeys("Ripetizioni Java");
        driver.findElement(By.id("offerta-skill")).sendKeys("Spiegazione Socket e Concorrenza");
        driver.findElement(By.id("disponibilita")).sendKeys("Venerdì pomeriggio");
        driver.findElement(By.id("ricerca-skill")).sendKeys("Esercizi diagrammi UML");

        driver.findElement(By.id("btn-pubblica")).click();

        // Verifica l'alert di successo finale
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertEquals("Annuncio pubblicato con successo", alert.getText());
        alert.accept();
    }

    // -------------------------------------------------------------------------
    // HELPER
    // -------------------------------------------------------------------------
    /** Waits until the GWT app has finished bootstrapping (input is clickable). */
    private WebElement waitForApp() {
        return new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-create-ad")));
    }

}
