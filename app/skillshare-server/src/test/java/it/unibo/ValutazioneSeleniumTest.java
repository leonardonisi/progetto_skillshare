package it.unibo;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ValutazioneSeleniumTest {
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
        driver.manage().window().maximize();
    }

    @AfterAll
    static void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void loadApp() throws InterruptedException {
        try { driver.switchTo().alert().accept(); } catch (Exception e) {}

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username"))).sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn-login")));
        Thread.sleep(2000); 
    }

    @Test
    void testAperturaPopupValutazione() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        Actions action = new Actions(driver);

        // 1. Apri Menu
        WebElement navSkill = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-skill")));
        action.moveToElement(navSkill).perform();
        Thread.sleep(500); 

        // Clicca su "LE MIE SKILL"
        WebElement menuMieSkill = wait.until(ExpectedConditions.elementToBeClickable(By.id("menu-item-le-mie-skill")));
        menuMieSkill.click();
        Thread.sleep(1000); 

        // Apri la tendina "Skills Concluse"
        WebElement panelConcluse = wait.until(ExpectedConditions.elementToBeClickable(By.id("sidebar-skills-concluse")));
        panelConcluse.click();
        Thread.sleep(500);

        // Seleziona l'annuncio per far apparire il bottone Valuta
        WebElement btnAnnuncio = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), 'Allenamento Tennis')]")
        ));
        btnAnnuncio.click();
        Thread.sleep(1000); // Pausa per far generare la card a destra

        // Clicca sul bottone "Valuta"
        WebElement btnValuta = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-valuta-scambio")));
        btnValuta.click();

        // Compila il popup
        WebElement txtRecensione = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-recensione")));
        txtRecensione.clear();
        txtRecensione.sendKeys("Ottimo scambio, utente molto preparato!");

        driver.findElement(By.id("star-5")).click();
        driver.findElement(By.id("btn-invia-valutazione")).click();

        // Verifica l'alert finale
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        
        // Accetta sia il primo salvataggio che il caso in cui il DB abbia già la recensione
        assertTrue(alertText.equals("Valutazione salvata con successo!") || 
                   alertText.equals("Errore: Hai già valutato questo scambio."),
                   "Messaggio inatteso: " + alertText);
                   
        alert.accept();
    }
}