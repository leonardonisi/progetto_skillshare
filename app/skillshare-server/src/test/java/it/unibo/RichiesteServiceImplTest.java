package it.unibo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RichiesteServiceImplTest {

    private RichiesteServiceImpl richiesteService;

    @BeforeEach
    void setUp() {
        richiesteService = new RichiesteServiceImpl();
        // Inizializza il DB di test prima di ogni chiamata
        DatabaseCore.seedDatabase(); 
    }

    @Test
    void testGetMieRichiesteReturnsData() {
        List<Annuncio> richieste = richiesteService.getMieRichieste("admin");
        
        assertNotNull(richieste, "La lista delle richieste non deve essere nulla");
        assertFalse(richieste.isEmpty(), "La lista delle richieste dovrebbe contenere i mock di test");
    }
}
