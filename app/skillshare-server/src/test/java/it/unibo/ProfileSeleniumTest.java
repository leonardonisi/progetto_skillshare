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

public class ProfileSeleniumTest {

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

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        driver.get(BASE_URL);

        WebElement inputUsername = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-username")));
        inputUsername.sendKeys("admin");
        driver.findElement(By.id("input-password")).sendKeys("password");
        driver.findElement(By.id("btn-login")).click();

        WebElement navProfilo = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-profilo")));
        navProfilo.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-profilo")));
    }

    // Verifica che la pagina del profilo si carichi correttamente e che il titolo sia presente
    @Test
    void pageLoadsWithCorrectTitle() {
        WebElement titleElement = driver.findElement(By.id("titolo-profilo"));
        assertEquals("IL MIO PROFILO", titleElement.getText());
    }

    //verifica che il bottone "MODIFICA PROFILO" sia presente e visibile
    @Test
    void editButtonIsPresent() {
        WebElement editButton = driver.findElement(By.id("btn-modifica"));
        assertTrue(editButton.isDisplayed());
        assertEquals("MODIFICA PROFILO", editButton.getText());
    }

    //verifica che la sezione foto del profilo sia presente e visibile
    @Test
    void avatarIsPresent() {
        WebElement avatar = driver.findElement(By.id("img-avatar"));
        assertTrue(avatar.isDisplayed());
    }

    //verifica che le informazioni dell'utente (username e biografia) siano presenti e visibili
    @Test
    void userInfoIsPresent() {
        WebElement username = driver.findElement(By.id("lbl-username"));
        WebElement bio = driver.findElement(By.id("lbl-bio"));
        
        assertTrue(username.isDisplayed());
        assertTrue(bio.isDisplayed());
        
        assertTrue(username.getText().contains("Username:"));
        assertTrue(bio.getText().contains("Biografia:"));
    }

    // Controlla l'esistenza e la visibilità del menu a tendina per le categorie
    @Test
    void categoriesDropdownIsPresent() {
        WebElement categorieDropdown = driver.findElement(By.id("select-categorie"));
        assertTrue(categorieDropdown.isDisplayed());
    }

    //verifica che il pannello per le categorie selezionate sia presente e visibile
    @Test
    void categoriesAreaIsPresent() {
        WebElement categorieDropdown = driver.findElement(By.id("select-categorie"));
        assertTrue(categorieDropdown.isDisplayed());

        WebElement tagPanel = driver.findElement(By.id("panel-tag-categorie"));
        assertNotNull(tagPanel);
    } 

    // Verifica che selezionando una categoria dal menu a tendina, venga aggiunta un'etichetta nel pannello delle categorie selezionate
    @Test
    void selectingCategoryAddsTag() {
        WebElement categorieDropdown = driver.findElement(By.id("select-categorie"));
        
        org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(categorieDropdown);
        
        select.selectByIndex(1);

        WebElement tagPanel = driver.findElement(By.id("panel-tag-categorie"));
        assertFalse(tagPanel.getText().isEmpty());

        WebElement removeButton = driver.findElement(By.xpath("//div[@id='panel-tag-categorie']//button[text()='X']"));
        org.junit.jupiter.api.Assertions.assertTrue(removeButton.isDisplayed());
    }

    // Verifica che l'etichetta della locazione sia presente con il testo iniziale corretto
    @Test
    void locationLabelIsPresent() {
        WebElement locazioneLabel = driver.findElement(By.id("lbl-locazione"));
        assertTrue(locazioneLabel.isDisplayed());
        assertTrue(locazioneLabel.getText().contains("Locazione:"));
    }

    //verifica che il bottone "Torna alla Home" sia presente e funzioni correttamente
    @Test
    void backToHomeButtonWorks() {
        WebElement btnHome = driver.findElement(By.id("btn-torna-home"));
        assertTrue(btnHome.isDisplayed());
        btnHome.click();

        WebElement titoloHome = new WebDriverWait(driver, TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-home")));
        assertTrue(titoloHome.isDisplayed());
    }
}