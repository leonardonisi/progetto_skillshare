package it.unibo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testDoppiaConfermaPortaAStatoConcluso() {
        
        RichiestaScambio r = new RichiestaScambio(999, 1, "mario", "admin");
        r.setStato(RichiestaScambio.StatoRichiesta.ACCETTATO);
        DatabaseCore.getMappaRichieste().put(999, r);

        RichiestaScambio r1 = richiesteService.elaboraAzioneScambio(999, "admin", true);
        assertEquals(RichiestaScambio.StatoRichiesta.ACCETTATO, r1.getStato());
        assertTrue(r1.isConfermatoDaProprietario());
        assertFalse(r1.isConfermatoDaRichiedente());

        RichiestaScambio r2 = richiesteService.elaboraAzioneScambio(999, "mario", true);
        assertEquals(RichiestaScambio.StatoRichiesta.CONCLUSO, r2.getStato());
    }
}
