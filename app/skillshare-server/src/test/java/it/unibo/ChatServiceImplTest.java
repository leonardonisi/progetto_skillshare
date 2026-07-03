package it.unibo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import static org.junit.jupiter.api.Assertions.*;

public class ChatServiceImplTest {

    private ChatServiceImpl chatService;
    private final ConcurrentMap<Integer, Messaggio> dbMessaggi = DatabaseCore.getMappaMessaggi();

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl();
        dbMessaggi.clear();
        DatabaseCore.commit();
    }

    @Test
    void testInviaMessaggioSalvaCorrettamenteSuDatabase() {
        // Esecuzione
        chatService.inviaMessaggio("UserA", "UserB", "Ciao! Ho visto il tuo annuncio.");

        // Verifica
        assertEquals(1, dbMessaggi.size());
        Messaggio salvato = dbMessaggi.get(1);
        assertNotNull(salvato);
        assertEquals("UserA", salvato.getMittente());
        assertEquals("UserB", salvato.getDestinatario());
        assertEquals("Ciao! Ho visto il tuo annuncio.", salvato.getTesto());
    }

    @Test
    void testGetCronologiaFiltraEMantieneOrdineSequenziale() {
        // Popolamento database di test
        chatService.inviaMessaggio("UserA", "UserB", "Messaggio 1");
        chatService.inviaMessaggio("UserB", "UserA", "Messaggio 2");
        chatService.inviaMessaggio("UserA", "UserC", "Messaggio isolato");

        // Recupero cronologia
        List<Messaggio> cronologia = chatService.getCronologia("UserA", "UserB");

        // Verifica ordine sequenziale
        assertEquals(2, cronologia.size());
        assertEquals("Messaggio 1", cronologia.get(0).getTesto());
        assertEquals("Messaggio 2", cronologia.get(1).getTesto());
    }

    @Test
    void testGetConversazioniAttiveSegueOrdinamentoLIFO() {
        // Simulazione flussi di chat sequenziali
        chatService.inviaMessaggio("UserLoggato", "ContattoVecchio", "Primo thread");
        chatService.inviaMessaggio("UserLoggato", "ContattoNuovo", "Secondo thread");
        chatService.inviaMessaggio("UserLoggato", "ContattoVecchio", "Ultimo aggiornamento");

        // Estrazione contatti attivi
        List<String> contatti = chatService.getConversazioniAttive("UserLoggato");

        // Verifica LIFO
        assertEquals(2, contatti.size());
        assertEquals("ContattoVecchio", contatti.get(0));
        assertEquals("ContattoNuovo", contatti.get(1));
    }

    @Test
    void testInviaMessaggioVuotoOInvalidoNonVieneRegistrato() {
        chatService.inviaMessaggio("UserA", "UserB", "   ");
        chatService.inviaMessaggio("UserA", "UserB", null);

        assertEquals(0, dbMessaggi.size());
    }
}