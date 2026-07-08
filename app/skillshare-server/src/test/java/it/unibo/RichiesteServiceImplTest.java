package it.unibo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;
import java.util.List;

public class RichiesteServiceImplTest {

    private RichiesteServiceImpl richiesteService;
    private ConcurrentMap<Integer, Annuncio> dbAnnunci;

    @BeforeEach
    void setUp() {
        DatabaseCore.enableTestMode();
        DatabaseCore.close(); 

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        dbUtenti.clear();
        dbUtenti.put("admin", new Utente("admin", "password"));

        dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        dbAnnunci.clear();

        Annuncio annuncio = new Annuncio();
        annuncio.setId(100);
        annuncio.setAutore("altroUtente");
        annuncio.setTitolo("Ripetizioni di Java");
        dbAnnunci.put(100, annuncio);

        ConcurrentMap<Integer, RichiestaScambio> dbRichieste = db.hashMap("richieste", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        dbRichieste.clear();

        RichiestaScambio richiesta = new RichiestaScambio();
        richiesta.setId(1);
        richiesta.setIdAnnuncio(100);
        richiesta.setRichiedenteUser("admin");
        richiesta.setStato(RichiestaScambio.StatoRichiesta.IN_ATTESA);
        dbRichieste.put(1, richiesta);

        DatabaseCore.commit();

        richiesteService = new RichiesteServiceImpl();
    }

    @Test
    void testGetMieRichiesteReturnsData() {
        List<RichiestaScambio> richieste = richiesteService.getRichiesteScambio("admin");
        
        assertNotNull(richieste);
        assertFalse(richieste.isEmpty());
    }
}
