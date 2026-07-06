package it.unibo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestRegisterServiceImpl {

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
        boolean esito = registerService.registraUtente("nuovoutente", "password", "password");
        assertTrue(esito);
        assertNotNull(DatabaseCore.getMappaUtenti().get("nuovoutente"));
    }

    @Test
    void testUsernameGiaUsato() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registraUtente("admin", "password", "password");
        });
        assertEquals("Username già usato", ex.getMessage());
    }

    @Test
    void testPasswordNonConforme() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registraUtente("utente1", "password", "pasword_diversa");
        });
        assertEquals("Password non conforme", ex.getMessage());
    }

    @Test
    void testUsernameTroppoCorto() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registraUtente("tu", "password", "password");
        });
        assertEquals("Username troppo corto", ex.getMessage());
    }

    @Test
    void testPasswordTroppoCorta() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            registerService.registraUtente("utente1", "pa", "pa");
        });
        assertEquals("Password troppo corta", ex.getMessage());
    }
}