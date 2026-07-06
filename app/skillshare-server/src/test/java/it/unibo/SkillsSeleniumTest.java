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

        driver.get(BASE_URL);
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // LOGIN
        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));
        inputUsername.sendKeys("admin");
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

    @Test
    void testScenarioModificaDatiAnnuncioEsistente() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement btnSidebar = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-skill-0")));
        btnSidebar.click();

        // Click sul pulsante Modifica
        WebElement btnModifica = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-modifica-annuncio")));
        assertTrue(btnModifica.isDisplayed());
        btnModifica.click();

        WebElement inputTitolo = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("input-modifica-titolo")));
        WebElement inputDescrizione = driver.findElement(By.id("input-modifica-descrizione"));
        WebElement btnConferma = driver.findElement(By.id("btn-modifica-conferma"));

        inputTitolo.clear();
        inputTitolo.sendKeys("CUCINA POLLO MODIFICATO");
        inputDescrizione.clear();
        inputDescrizione.sendKeys("Nuova descrizione per ricetta pollo");

        btnConferma.click();

        WebElement titoloAggiornato = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lbl-titolo")));
        WebElement descAggiornata = driver.findElement(By.id("lbl-descrizione"));

        assertEquals("CUCINA POLLO MODIFICATO", titoloAggiornato.getText());
        assertEquals("DETTAGLI OGGETTO: Nuova descrizione per ricetta pollo", descAggiornata.getText());
    }
}
