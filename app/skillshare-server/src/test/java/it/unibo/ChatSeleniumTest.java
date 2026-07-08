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

public class ChatSeleniumTest {
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

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));

        // Inserisci le credenziali per superare la login
        inputUsername.sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        // Attende il caricamento della Home e clicca sul pulsante per andare in CHAT
        WebElement btnChat = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-chat")));
        btnChat.click(); // Mancava anche il comando .click() effettivo per cambiare schermata!

        // Per i test simuliamo l'ingresso diretto o il click alla vista
        waitForApp();
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------

    @Test
    void pageLoadsWithCorrectTitle() {
        // Cerco l'elemento del titolo tramite l'ID
        WebElement titleElement = driver.findElement(By.id("titolo-chat"));
        // Verifico
        assertEquals("I MIEI MESSAGGI", titleElement.getText());
    }

    @Test
    void testFormElementsArePresent() {
        // Controlliamo solo i macro-componenti strutturali per garantire la stabilità
        // del test
        assertTrue(driver.findElement(By.id("lista-contatti")).isDisplayed());
        assertTrue(driver.findElement(By.id("input-messaggio")).isDisplayed());
    }

    @Test
    void testVisualizzazioneListaContatti() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement contatto1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contatto-UtenteScambio_1")));
        
        assertTrue(contatto1.isDisplayed());
        assertTrue(contatto1.getText().contains("UtenteScambio_1"));
    }

    @Test
    void testAperturaConversazioneMostraMessaggi() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement contatto1 = wait.until(ExpectedConditions.elementToBeClickable(By.id("contatto-UtenteScambio_1")));
        contatto1.click();

        WebElement areaMessaggi = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("area-cronologia-messaggi")));

        wait.until(ExpectedConditions.textToBePresentInElement(areaMessaggi, "Skillshare"));
        wait.until(ExpectedConditions.textToBePresentInElement(areaMessaggi, "Ciao!"));

        String testoTotale = areaMessaggi.getText();
        assertTrue(testoTotale.contains("Skillshare"));
        assertTrue(testoTotale.contains("Ciao!"));
    }

    @Test
    void testInvioMessaggioConSuccesso() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(ExpectedConditions.elementToBeClickable(By.id("contatto-UtenteScambio_1"))).click();

        WebElement inputMessaggio = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-messaggio")));
        WebElement btnInvia = driver.findElement(By.id("btn-invia-messaggio"));

        String testoMessaggio = "Questo è un messaggio di test automatizzato.";
        inputMessaggio.sendKeys(testoMessaggio);
        btnInvia.click();

        assertEquals("", inputMessaggio.getAttribute("value"));

        WebElement areaMessaggi = driver.findElement(By.id("area-cronologia-messaggi"));
        assertTrue(areaMessaggi.getText().contains(testoMessaggio));
    }

    // -------------------------------------------------------------------------
    // HELPER
    // -------------------------------------------------------------------------
    /** Waits until the GWT app has finished bootstrapping (input is clickable). */
    private WebElement waitForApp() {
        return new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-chat")));
    }
}