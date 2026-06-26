package it.unibo;

import java.util.List;
import jakarta.servlet.ServletException;
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
    public void testGetAnnunciRitornaListaAnnunci() throws ServletException {
        MarketServiceImpl marketService = new MarketServiceImpl();
        marketService.init();

        List<Annuncio> annunci = marketService.getAnnunci();

        assertNotNull(annunci, "La lista degli annunci non deve essere null");
        assertFalse(annunci.isEmpty(), "La lista deve contenere degli elementi di prova dopo l'init");
        
        Annuncio primoAnnuncio = annunci.get(0);
        assertNotNull(primoAnnuncio, "Il primo annuncio della lista non deve essere null");

        assertNotNull(primoAnnuncio.getTitolo(), "Il titolo non deve essere null");
        assertFalse(primoAnnuncio.getTitolo().trim().isEmpty(), "Il titolo non deve essere vuoto");

        assertNotNull(primoAnnuncio.getCategoria(), "La categoria non deve essere null");
        assertFalse(primoAnnuncio.getCategoria().trim().isEmpty(), "La categoria non deve essere vuota");

        assertNotNull(primoAnnuncio.getSkillOfferta(), "La skill offerta non deve essere null");
        assertFalse(primoAnnuncio.getSkillOfferta().trim().isEmpty(), "La skill offerta non deve essere vuota");

        assertNotNull(primoAnnuncio.getControprestazione(), "La controprestazione non deve essere null");
        assertFalse(primoAnnuncio.getControprestazione().trim().isEmpty(), "La controprestazione non deve essere vuota");

        assertNotNull(primoAnnuncio.getDisponibilita(), "La disponibilità non deve essere null");
        assertFalse(primoAnnuncio.getDisponibilita().trim().isEmpty(), "La disponibilità non deve essere vuota");

        assertNotNull(primoAnnuncio.getUtente(), "L'ID utente non deve essere null");
        assertFalse(primoAnnuncio.getUtente().trim().isEmpty(), "L'ID utente non deve essere vuoto");
    }
}