package it.unibo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MainLayoutSeleniumTest {

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
    void loadAppAndLogin() {
        driver.get(BASE_URL);

        // Aspetta il login e superalo per arrivare alla tua Home Page
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("input-username")));
        WebElement passwordField = driver.findElement(By.id("input-password"));
        WebElement loginButton = driver.findElement(By.id("btn-login"));

        usernameField.clear();
        usernameField.sendKeys("admin"); // Usa l'utente fittizio di Matteo
        passwordField.clear();
        passwordField.sendKeys("password");
        loginButton.click();

        // Aspetta che l'header della tua MainLayoutGui sia visibile
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-market")));
    }

    @Test void searchButtonIsPresent () {
        WebElement registerButton = driver.findElement(By.id("search-button"));
        assertTrue(registerButton.isDisplayed());
        assertEquals("CERCA", registerButton.getText());
    }

    @Test
    public void testPresenzaETestoDefaultTendinaCategorie() {
        WebElement tendinaElement = driver.findElement(By.id("tendina-categorie"));
        Select tendina = new Select(tendinaElement);
        assertEquals("Scegli categoria", tendina.getFirstSelectedOption().getText());
    }

    /*@Test TEST NON IMPLEMENTABILE
    public void testSelezioneCategoriaFiltraSkillCorrettamente() {
        WebElement tendinaElement = driver.findElement(By.id("tendina-categorie"));
        Select tendina = new Select(tendinaElement);
        tendina.selectByVisibleText("Sviluppo Software");

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // 4. Verifica che le card caricate a sinistra appartengano a quella categoria
        // Recuperiamo tutti gli elementi che contengono il testo della categoria nella colonna di sinistra.
        // (Presuppone che tu abbia aggiunto una classe es. "label-categoria" alle label dentro le card)
        List<WebElement> etichetteCategorie = driver.findElements(By.id("label-categoria"));

        // Assicuriamoci che la ricerca abbia prodotto almeno un risultato (altrimenti il ciclo for non gira e il test passa "a vuoto")
        assertFalse("La ricerca non ha prodotto risultati", etichetteCategorie.isEmpty());

        // Controlliamo che ogni singola card caricata abbia la dicitura corretta
        for (WebElement etichetta : etichetteCategorie) {
            String testoCategoria = etichetta.getText();
            assertTrue("Trovata una card con categoria errata: " + testoCategoria, 
                    testoCategoria.contains("Sviluppo Software"));
        }
    }*/

    @Test
    void navigazioneVersoChatMostraPlaceholder() {
        // Trova il link della Chat nel tuo header e cliccalo
        WebElement navChat = driver.findElement(By.id("nav-chat"));
        navChat.click();

        // Verifica che il SimplePanel si aggiorni con il placeholder corretto
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement placeholderTesto = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(text(), 'Pagina CHAT in costruzione...')]")));

        assertTrue(placeholderTesto.isDisplayed());
    }

    @Test
    void navigazioneVersoProfiloMostraPlaceholder() {
        WebElement navProfilo = driver.findElement(By.id("nav-profilo"));
        navProfilo.click();

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement placeholderTesto = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(text(), 'Pagina PROFILO in costruzione...')]")));

        assertTrue(placeholderTesto.isDisplayed());
    }

    @Test
    void pulsantePubblicaMostraPaginaCreazione() {
        WebElement btnPubblica = driver.findElement(By.id("btn-pubblica"));
        btnPubblica.click();

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement createTitle = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-create-ad")));
        assertTrue(createTitle.isDisplayed());
    }

}
