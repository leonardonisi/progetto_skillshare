package it.unibo;

import java.util.List;
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

public class ForYouSeleniumTest {

    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080/");
    private static final Duration TIMEOUT = Duration.ofSeconds(15);
    private static WebDriver driver;
    private static WebDriverWait wait;

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
    void loadAppAndLogin() {
        driver.get(BASE_URL);

        // Aspetta il login e superalo per arrivare alla tua Home Page
        wait = new WebDriverWait(driver, TIMEOUT);
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-username")));
        WebElement passwordField = driver.findElement(By.id("input-password"));
        WebElement loginButton = driver.findElement(By.id("btn-login"));

        usernameField.clear();
        usernameField.sendKeys("admin");
        passwordField.clear();
        passwordField.sendKeys("password");
        loginButton.click();

        WebElement forYouLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-perte")));
        forYouLink.click();
    }

    @Test
    void testPresenzaListaUtentiConsigliati() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='card-utente']")));
        List<WebElement> cardsUtenti = driver.findElements(By.xpath("//div[@id='card-utente']"));

        assertFalse(cardsUtenti.isEmpty());
    }

    @Test
    void cardUtenteMostraDettaglioEAnnunciUtente() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='card-utente']")));

        WebElement primaCard = driver.findElements(By.xpath("//div[@id='card-utente']")).get(0);
        String usernameNellaCard = primaCard.findElement(By.id("username-card-utente")).getText();

        primaCard.click();

        WebElement usernameProfiloDettaglio = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username-profilo")));
        assertEquals(usernameNellaCard, usernameProfiloDettaglio.getText());
    }

    @Test
    void cardUtenteCambiaVistaUtente() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='card-utente']")));
        List<WebElement> cards = driver.findElements(By.xpath("//div[@id='card-utente']"));

        assertTrue(cards.size() > 1);

        cards.get(0).click();
        WebElement usernameProfiloUI = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("username-profilo")));
        String username1 = usernameProfiloUI.getText();

        cards.get(1).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("username-profilo"), username1)));
        String username2 = driver.findElement(By.id("username-profilo")).getText();

        assertNotEquals(username1, username2);
    }
}