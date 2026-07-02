package it.unibo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

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

    //verifica che la sezione foto del profilo sia presente e visibile
    @Test
    void photoIsPresent() {
        WebElement photo = driver.findElement(By.id("img-photo"));
        assertTrue(photo.isDisplayed());
    }

    //verifica che le TextBox dell'utente siano presenti e visibili
    @Test
    void userInfoIsPresentAndEditable() {
        WebElement usernameBox = driver.findElement(By.id("txt-username"));
        WebElement bioArea = driver.findElement(By.id("txt-bio"));
        
        assertTrue(usernameBox.isDisplayed());
        assertTrue(bioArea.isDisplayed());
        
        assertTrue(usernameBox.getAttribute("value").length() > 0);
        assertTrue(bioArea.getAttribute("value").length() > 0);
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
         WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.xpath("//select[@id='select-categorie']/option"), 1));

        int tagPrima = driver.findElements(By.cssSelector("#panel-tag-categorie .tag-categoria")).size();

        boolean selezionata = selezionaPrimaCategoriaDisponibile();
        assertTrue(selezionata, "Nessuna categoria disponibile: tutte risultano già selezionate.");

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("#panel-tag-categorie .tag-categoria"), tagPrima + 1));

        assertEquals(tagPrima + 1,
                driver.findElements(By.cssSelector("#panel-tag-categorie .tag-categoria")).size());
    } 

   
    // Verifica che la TextBox della locazione sia presente
    @Test
    void locationBoxIsPresent() {
        WebElement locazioneBox = driver.findElement(By.id("txt-locazione"));
        assertTrue(locazioneBox.isDisplayed());
        assertTrue(locazioneBox.getAttribute("value").length() > 0);
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

    // Verifica che il bottone Salva sia nascosto all'apertura del profilo
    @Test
    void saveButtonIsHiddenInitially() {
        WebElement btnModifica = driver.findElement(By.id("btn-modifica"));
        assertFalse(btnModifica.isDisplayed(), "Il bottone Salva deve essere invisibile finché non ci sono modifiche.");
    }

    // Verifica la comparsa del bottone Salva digitando testo
    @Test
    void saveButtonAppearsWhenTyping() {
        WebElement btnModifica = driver.findElement(By.id("btn-modifica"));
        WebElement txtBio = driver.findElement(By.id("txt-bio"));
        
        txtBio.sendKeys(" Test"); // Simula la digitazione
        
        assertTrue(btnModifica.isDisplayed(), "Il bottone Salva deve apparire dopo aver digitato qualcosa.");
    }

    // Verifica la comparsa del bottone Salva aggiungendo una categoria
    @Test
    void saveButtonAppearsWhenCategoryChanged() {
       WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//select[@id='select-categorie']/option"), 1));
        
        WebElement btnModifica = driver.findElement(By.id("btn-modifica"));
        WebElement categorieDropdown = driver.findElement(By.id("select-categorie"));
        
        Select select = new Select(categorieDropdown);
        select.selectByIndex(1);

       ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", categorieDropdown);
        
        wait.until(ExpectedConditions.visibilityOf(btnModifica));
        
        assertTrue(btnModifica.isDisplayed(), "Il bottone Salva deve apparire dopo aver aggiunto una categoria.");
    }

}