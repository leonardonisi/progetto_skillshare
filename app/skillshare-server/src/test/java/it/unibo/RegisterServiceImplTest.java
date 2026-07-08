package it.unibo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceImplTest {

    private RegisterServiceImpl registerService;

    @BeforeEach
    void setUp() {
        DatabaseCore.enableTestMode();
        DatabaseCore.seedDatabase();
        
        registerService = new RegisterServiceImpl();
    }

    @AfterEach
    void tearDown() {
        DatabaseCore.close();
    }

    @Test
    void testRegistrazioneCompletataConSuccesso() {
        String esito = registerService.register("nuovoutente", "password", "password");
        assertEquals("ok", esito);
        assertNotNull(DatabaseCore.getMappaUtenti().get("nuovoutente"));
    }

    @Test
    void testUsernameGiaUsato() {
        String result = registerService.register("admin", "password", "password");
        assertEquals("Username già usato", result);
    }

    @Test
    void testPasswordNonConforme() {
        String result = registerService.register("utente1", "password", "pasword_diversa");
        assertEquals("Password non conforme", result);
    }

    @Test
    void testUsernameTroppoCorto() {
        String result = registerService.register("tu", "password", "password");
        assertEquals("Username troppo corto", result);
    }

    @Test
    void testPasswordTroppoCorta() {
        String result = registerService.register("utente1", "pa", "pa");
        assertEquals("Password troppo corta", result);
    }
}