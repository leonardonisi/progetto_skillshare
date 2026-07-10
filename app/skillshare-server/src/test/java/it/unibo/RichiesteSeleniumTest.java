package it.unibo;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

public class RichiesteSeleniumTest {
    public static final String BASE_URL = System.getProperty("app.url", "http://localhost:8080/");
    public static final Duration TIMEOUT = Duration.ofSeconds(15);
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
    void loadAppAndNavigateToRichieste() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Nessun alert presente
        }

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // LOGIN
        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));
        inputUsername.sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        // NAVIGAZIONE VERSO LA TENDINA
        WebElement navSkill = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-skill")));
        try {
            new org.openqa.selenium.interactions.Actions(driver).moveToElement(navSkill).perform();
            WebElement btnLeMieRichieste = wait
                    .until(ExpectedConditions.elementToBeClickable(By.id("menu-item-le-mie-richieste")));
            btnLeMieRichieste.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("document.getElementById('menu-item-le-mie-richieste').click();");
        }

        // VERIFICA CARICAMENTO PAGINA ASPETTANDO LA PRIMA TENDINA
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sidebar-skills-richieste")));
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------

    @Test
    void sidebarSkillsRichiesteIsPresent() {
        WebElement btnRichieste = driver.findElement(By.id("sidebar-skills-richieste"));
        assertTrue(btnRichieste.isDisplayed());
        assertTrue(btnRichieste.getText().contains("Skills Richieste"),
                "Il testo dovrebbe contenere 'Skills Richieste'");
    }

    @Test
    void sidebarSkillsAccettateIsPresent() {
        WebElement btnAccettate = driver.findElement(By.id("sidebar-skills-accettate"));
        assertTrue(btnAccettate.isDisplayed());
        assertTrue(btnAccettate.getText().contains("Skills Accettate"),
                "Il testo dovrebbe contenere 'Skills Accettate'");
    }

    @Test
    void sidebarSkillsRifiutateIsPresent() {
        WebElement btnRifiutate = driver.findElement(By.id("sidebar-skills-rifiutate"));
        assertTrue(btnRifiutate.isDisplayed());
        assertTrue(btnRifiutate.getText().contains("Skills Rifiutate"),
                "Il testo dovrebbe contenere 'Skills Rifiutate'");
    }

    @Test
    void sidebarSkillsConcluseIsPresent() {
        WebElement btnConcluse = driver.findElement(By.id("sidebar-skills-concluse"));
        assertTrue(btnConcluse.isDisplayed());
        assertTrue(btnConcluse.getText().contains("Skills Concluse"), "Il testo dovrebbe contenere 'Skills Concluse'");
    }
}