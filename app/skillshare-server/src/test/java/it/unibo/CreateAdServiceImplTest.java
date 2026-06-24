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

    private CreateAdServiceImpl service;

    private ConcurrentMap<Integer, Annuncio> dbAnnunci;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseCore.enableTestMode();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getServerInfo()).thenReturn("MockServer/1.0");
        when(request.getHeader("User-Agent")).thenReturn("MockBrowser/1.0");
        service = new CreateAdServiceImpl();
        service.init(servletConfig);
        // Crea un ThreadLocal con la request mock e iniettalo via reflection
        ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();
        threadLocal.set(request);
        Field field = AbstractRemoteServiceServlet.class
                .getDeclaredField("perThreadRequest");
        field.setAccessible(true);
        field.set(service, threadLocal); // sostituiamo il campo con il nostro ThreadLocal

        DB db = DatabaseCore.getDB();
        dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA)
                .createOrOpen();
        dbAnnunci.clear();
        DatabaseCore.commit();
    }

    @Test
    void testPubblicaAnnuncioSuccess() {
        Annuncio annuncio = new Annuncio("Ripetizioni Java", "Informatica", "Thread", "Cucina", "Sabato Mattina",
                "utente123");
        boolean result = service.pubblicaAnnuncio(annuncio);
        assertTrue(result, "L'annuncio dovrebbe essere pubblicato con successo.");

        assertEquals(1, dbAnnunci.size(), "Dimensione non corrispode");
        assertTrue(dbAnnunci.containsKey(1), "Annuncio non salvato");

        Annuncio savedAnnuncio = dbAnnunci.get(1);
        assertEquals("Ripetizioni Java", savedAnnuncio.getTitolo(), "Titolo non corrispondente");
        assertEquals("Informatica", savedAnnuncio.getCategoria(), "Categoria non corrispondente");
    }

    @Test
    void pubblicaAnnuncio_CampiObbligatoriVuoti_DeveRifiutareIlSalvataggio() {
        Annuncio annuncioInvalido = new Annuncio("Titolo", "Informatica", "", "", "Sempre", "utente-loggato");
        boolean result = service.pubblicaAnnuncio(annuncioInvalido);

        assertFalse(result, "Errore di salvataggio");
        assertEquals(0, dbAnnunci.size(), "Un annuncio invalido è stato comunque scritto nel database");
    }
}