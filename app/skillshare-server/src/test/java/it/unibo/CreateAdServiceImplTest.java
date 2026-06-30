package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.AbstractRemoteServiceServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ConcurrentMap;
import org.mapdb.Serializer;
import org.mapdb.DB;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreateAdServiceImplTest {

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;

    private CreateAdServiceImpl createAdService;

    private ConcurrentMap<Integer, Annuncio> dbAnnunci;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseCore.enableTestMode();
        DatabaseCore.close(); 

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        dbUtenti.clear();
        dbUtenti.put("admin", new Utente("admin", "password"));

        dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        dbAnnunci.clear();

        DatabaseCore.commit();

        createAdService = new CreateAdServiceImpl();
    }

    @Test
    void testPubblicaAnnuncioSuccess() {
        Annuncio annuncio = new Annuncio.Builder()
                .autore("admin")
                .titolo("Ripetizioni Java")
                .categoria("Informatica")
                .skillOfferta("Thread")
                .controprestazioneCercata("Cucina")
                .disponibilita("Sabato Mattina")
                .build();
        boolean result = createAdService.pubblicaAnnuncio(annuncio);
        assertTrue(result, "L'annuncio dovrebbe essere pubblicato con successo.");

        assertEquals(1, dbAnnunci.size(), "Dimensione non corrispode");
        assertTrue(dbAnnunci.containsKey(1), "Annuncio non salvato");

        Annuncio savedAnnuncio = dbAnnunci.get(1);
        assertEquals("Ripetizioni Java", savedAnnuncio.getTitolo(), "Titolo non corrispondente");
        assertEquals("Informatica", savedAnnuncio.getCategoria(), "Categoria non corrispondente");
    }

    @Test
    void pubblicaAnnuncio_CampiObbligatoriVuoti_DeveRifiutareIlSalvataggio() {
        Annuncio annuncioInvalido = new Annuncio.Builder()
                .autore("admin")
                .titolo("Ripetizioni Java")
                .categoria("Informatica")
                .skillOfferta("")
                .controprestazioneCercata("")
                .disponibilita("Sabato Mattina")
                .build();
        boolean result = createAdService.pubblicaAnnuncio(annuncioInvalido);

        assertFalse(result, "Errore di salvataggio");
        assertEquals(0, dbAnnunci.size(), "Un annuncio invalido è stato comunque scritto nel database");
    }
}