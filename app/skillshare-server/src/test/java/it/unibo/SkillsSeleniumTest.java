package it.unibo;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // 1. LOGIN (Stesso stile esatto di ProfileSeleniumTest)
        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));
        inputUsername.sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        // 2. NAVIGAZIONE VERSO LA TENDINA SKILL
        WebElement navSkill = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-skill")));
        
        // Il tuo menu si apre al passaggio del mouse, quindi lo simuliamo
        new org.openqa.selenium.interactions.Actions(driver).moveToElement(navSkill).perform();

        // Clicchiamo la voce della tendina
        WebElement btnLeMieSkill = wait.until(ExpectedConditions.elementToBeClickable(By.id("menu-item-le-mie-skill")));
        btnLeMieSkill.click();

        // 3. VERIFICA CARICAMENTO PAGINA
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-skills-dashboard")));
    }
   
    // -------------------------------------------------------------------------
    // TEST
    // -------------------------------------------------------------------------

    @Test
    void pageLoadsWithCorrectTitle() {
        WebElement titleElement = driver.findElement(By.id("titolo-skills-dashboard"));
        assertEquals("Le Mie Skill", titleElement.getText());
    }

    @Test
    void sidebarMieSkillsIsPresent() {
        WebElement btnMieSkills = driver.findElement(By.id("sidebar-mie-skills"));
        assertTrue(btnMieSkills.isDisplayed());
        assertEquals("Mie Skills", btnMieSkills.getText());
    }

    @Test
    void sidebarSkillsAccettateIsPresent() {
        WebElement btnAccettate = driver.findElement(By.id("sidebar-skills-accettate"));
        assertTrue(btnAccettate.isDisplayed());
        assertEquals("Skills Accettate", btnAccettate.getText());
    }

    @Test
    void sidebarSkillsConcluseIsPresent() {
        WebElement btnConcluse = driver.findElement(By.id("sidebar-skills-concluse"));
        assertTrue(btnConcluse.isDisplayed());
        assertEquals("Skills Concluse", btnConcluse.getText());
    }
    
}
