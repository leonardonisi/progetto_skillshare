package it.unibo;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RatingProfiloSeleniumTest {
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
        if (driver != null) { driver.quit(); }
    }

    @Test
    void testVisualizzazioneRatingEStorico() throws InterruptedException {
        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // Eseguiamo un login di test
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username"))).sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("btn-login")));
        Thread.sleep(2000);

        // Andiamo sul Profilo Pubblico cliccando sulla foto in alto a destra
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-profilo"))).click();
        Thread.sleep(1000);

        //  Verifichiamo che la label del rating sia visibile e contenga le stelle
        WebElement lblRating = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lbl-rating-medio")));
        assertTrue(lblRating.getText().contains("★") || lblRating.getText().matches(".*\\d\\.\\d.*") || lblRating.getText().contains("Nessuna"), 
                "Il rating medio deve contenere stelle, un valore numerico o indicare assenza di recensioni");

        // Verifichiamo che il contenitore dello storico esista
        WebElement storicoContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("container-storico-recensioni")));
        assertTrue(storicoContainer.isDisplayed(), "Lo storico delle recensioni deve essere visibile");
    }
    
}
