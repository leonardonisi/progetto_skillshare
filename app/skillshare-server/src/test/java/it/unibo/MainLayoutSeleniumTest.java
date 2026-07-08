package it.unibo;

import java.util.List;
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
        usernameField.sendKeys("admin");
        passwordField.clear();
        passwordField.sendKeys("password");
        loginButton.click();

        // Aspetta che l'header della tua MainLayoutGui sia visibile
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-market")));
    }

    @Test
    void marketLinkIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement marketLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-market")));
        assertTrue(marketLink.isDisplayed());
        assertEquals("MARKET", marketLink.getText());
    }

    @Test
    void forYouLinkIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement forYouLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-perte")));
        assertTrue(forYouLink.isDisplayed());
        assertEquals("PER TE", forYouLink.getText());
    }

    @Test
    void chatLinkIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement chatLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-chat")));
        assertTrue(chatLink.isDisplayed());
        assertEquals("CHAT", chatLink.getText());
    }

    @Test
    void profileIconIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement profileIcon = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-profilo")));
        assertTrue(profileIcon.isDisplayed());
    }

    @Test
    void searchBarIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement searchBar = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search-bar")));
        assertTrue(searchBar.isDisplayed());
        assertEquals("Cerca...", searchBar.getAttribute("placeholder"));
    }

    @Test
    void publishButtonIsPresent() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement publishButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn-pubblica")));
        assertTrue(publishButton.isDisplayed());
        assertEquals("PUBBLICA", publishButton.getText());
    }

    @Test
    public void testPresenzaETestoDefaultTendinaCategorie() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement tendinaElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tendina-categorie")));
        Select tendina = new Select(tendinaElement);
        assertEquals("Tutte le Categorie", tendina.getFirstSelectedOption().getText());
    }

    @Test
    void navigazioneVersoChatMostraPaginaChat() {
        // Trova il link della Chat nel tuo header e cliccalo
        WebElement navChat = driver.findElement(By.id("nav-chat"));
        navChat.click();

        // Verifica che si carichi correttamente la nuova interfaccia della chat reale
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement titoloChat = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-chat")));

        assertTrue(titoloChat.isDisplayed());
        assertEquals("I MIEI MESSAGGI", titoloChat.getText());
    }

    @Test
    void navigazioneVersoProfiloMostraPaginaProfilo() {
        WebElement navProfilo = driver.findElement(By.id("nav-profilo"));
        navProfilo.click();

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement titoloProfilo = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-profilo")));
        assertTrue(titoloProfilo.isDisplayed());
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

    @Test
    public void testSelezioneCategoriaSenzaAnnunciMostraAlert() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement tendinaElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tendina-categorie")));
        Select tendina = new Select(tendinaElement);

        tendina.selectByVisibleText("Yoga e Pilates");
        WebElement alert = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("alert-filtraggio-categorie")));

        assertTrue(alert.getText().equals("Nessun annuncio trovato"));
    }

    @Test
    public void testSelezioneCategoriaFiltraSkillCorrettamente() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement tendinaElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("tendina-categorie")));
        Select tendina = new Select(tendinaElement);

        tendina.selectByVisibleText("Sviluppo Software");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card-annuncio")));
        List<WebElement> cardList = driver.findElements(By.id("card-annuncio"));

        WebElement primaCard = cardList.get(0);
        primaCard.click();

        WebElement categoriaAnnuncio = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("lbl-categoria")));
        assertTrue(categoriaAnnuncio.getText().contains("Sviluppo Software"));
    }

    @Test
    public void testBarraDiRicercaFiltraSkillCorrettamente() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        WebElement searchBar = wait.until(ExpectedConditions.elementToBeClickable(By.id("search-bar")));

        searchBar.getText();
        searchBar.sendKeys("GWT");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card-annuncio")));
        List<WebElement> cardList = driver.findElements(By.id("card-annuncio"));

        WebElement primaCard = cardList.get(0);
        primaCard.click();

        WebElement titoloAnnuncio = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lbl-titolo")));
        WebElement descrizioneAnnuncio = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("lbl-descrizione")));

        String titoloTesto = titoloAnnuncio.getText().toLowerCase();
        String descrizioneTesto = descrizioneAnnuncio.getText().toLowerCase();

        assertTrue(titoloTesto.contains("gwt") || descrizioneTesto.contains("gwt"));
    }

    @Test
    void clickLogoutEConfermaRimandaAllaPaginaDiLogin() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        WebElement btnLogout = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-logout")));

        btnLogout.click();
        Alert alertConferma = wait.until(ExpectedConditions.alertIsPresent());
        alertConferma.accept();

        // Verifica che l'interfaccia non sia stata toccata e la navbar sia ancora
        // attiva
        WebElement titoloLogin = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("titolo-login")));
        WebElement inputUsernameLogin = driver.findElement(By.id("input-username"));
        assertTrue(titoloLogin.isDisplayed());
        assertTrue(inputUsernameLogin.isDisplayed());
    }
}
