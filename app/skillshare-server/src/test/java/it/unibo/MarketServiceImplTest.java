package it.unibo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MarketServiceImplTest {

    private MarketServiceImpl marketService;

    @BeforeEach
    public void setUp() {
        // Inizializza il servizio prima di ogni test
        marketService = new MarketServiceImpl();
    }

    @Test
    public void testGetAnnunciRitornaListaMock() {
        // Chiama il metodo del backend
        List<String> annunci = marketService.getAnnunci();

        // Verifica che la lista esista e non sia vuota
        assertNotNull(annunci, "La lista degli annunci non deve essere null");
        assertFalse(annunci.isEmpty(), "La lista mockata deve contenere degli elementi di prova");
        
        // Verifica che il mock contenga effettivamente dei dati validi
        assertNotNull(annunci.get(0), "Il primo annuncio della lista non deve essere null");
    }
}