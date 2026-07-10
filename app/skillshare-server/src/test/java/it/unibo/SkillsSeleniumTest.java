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

public class SkillsSeleniumTest {
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
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterAll
    static void stopDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    void loadAppAndNavigateToSkills() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Nessun alert presente
        }

        DatabaseCore.enableTestMode();
        DatabaseCore.seedDatabase();
        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // LOGIN
        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));
        inputUsername.sendKeys("filker67");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        // NAVIGAZIONE VERSO LA TENDINA SKILL
        WebElement navSkill = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-skill")));

        // simulazione hover per far apparire la tendina
        new org.openqa.selenium.interactions.Actions(driver).moveToElement(navSkill).perform();

        // Clicchiamo la voce della tendina
        WebElement btnLeMieSkill = wait.until(ExpectedConditions.elementToBeClickable(By.id("menu-item-le-mie-skill")));
        btnLeMieSkill.click();

        // VERIFICA CARICAMENTO PAGINA
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("sidebar-mie-skills")));
    }

    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------
    @Test
    void sidebarMieSkillsIsPresent() {
        WebElement btnMieSkills = driver.findElement(By.id("sidebar-mie-skills"));
        assertTrue(btnMieSkills.isDisplayed());
        assertTrue(btnMieSkills.getText().contains("Mie Skills"), "Il testo dovrebbe contenere 'Mie Skills'");
    }

    @Test
    void sidebarSkillsAccettateIsPresent() {
        WebElement btnAccettate = driver.findElement(By.id("sidebar-skills-accettate"));
        assertTrue(btnAccettate.isDisplayed());
        assertTrue(btnAccettate.getText().contains("Skills Accettate"),
                "Il testo dovrebbe contenere 'Skills Accettate'");
    }

    @Test
    void sidebarSkillsConcluseIsPresent() {
        WebElement btnConcluse = driver.findElement(By.id("sidebar-skills-concluse"));
        assertTrue(btnConcluse.isDisplayed());
        assertTrue(btnConcluse.getText().contains("Skills Concluse"), "Il testo dovrebbe contenere 'Skills Concluse'");
    }

}
