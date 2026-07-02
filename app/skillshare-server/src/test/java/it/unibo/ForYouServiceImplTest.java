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
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import org.mapdb.Serializer;
import org.mapdb.DB;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ForYouServiceImplTest {

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;

    private ForYouServiceImpl forYouService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseCore.enableTestMode();
        DatabaseCore.close(); 

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();

        dbUtenti.clear();

        // utente corrente
        Utente admin = new Utente("admin", "password");
        admin.setCompetenzePreferite(Arrays.asList("Informatica", "Lingue")); 
        dbUtenti.put("admin", admin);

        // altri utenti
        Utente utenteTop = new Utente("utenteTop", "pass");
        dbUtenti.put("utenteTop", utenteTop);
        
        dbAnnunci.put(1, new Annuncio.Builder()
                .autore("utenteTop")
                .titolo("Sito Web")
                .categoria("Informatica")
                .build());
                
        dbAnnunci.put(2, new Annuncio.Builder()
                .autore("utenteTop")
                .titolo("Lezioni Python")
                .categoria("Informatica")
                .build());
                
        dbAnnunci.put(3, new Annuncio.Builder()
                .autore("utenteTop")
                .titolo("Traduzione Inglese")
                .categoria("Lingue")
                .build());

        Utente utenteMedio = new Utente("utenteMedio", "pass");
        dbUtenti.put("utenteMedio", utenteMedio);
        
        dbAnnunci.put(4, new Annuncio.Builder()
                .autore("utenteMedio")
                .titolo("Lezioni Francese")
                .categoria("Lingue")
                .build());
                
        dbAnnunci.put(5, new Annuncio.Builder()
                .autore("utenteMedio")
                .titolo("Taglio prato")
                .categoria("Giardinaggio")
                .build());

        Utente utenteBasso = new Utente("utenteBasso", "pass");
        dbUtenti.put("utenteBasso", utenteBasso);
        
        dbAnnunci.put(6, new Annuncio.Builder()
                .autore("utenteBasso")
                .titolo("Riparazione Tubo")
                .categoria("Idraulica")
                .build());

        DatabaseCore.commit();

        forYouService = new ForYouServiceImpl();
    }

    @Test
    void testUtentiRitornatiSonoOrdinatiPerRilevanza() { 
        String usernameCorrente = "admin";

        Map<String, Integer> match = new HashMap<>();

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();

        List<Annuncio> listaAnnunci = List.copyOf(dbAnnunci.values());
        Utente utenteCorrente = dbUtenti.get(usernameCorrente);
        List<String> listaCategoriePreferite = utenteCorrente.getCompetenzePreferite();

        List<Utente> result = forYouService.getUtentiConsigliati(usernameCorrente);

        for(Annuncio a : listaAnnunci){
            if(listaCategoriePreferite.contains(a.getCategoria())){
                match.merge(a.getAutore(), 1, Integer::sum);
            }
        }

        int punteggioPrecedente = Integer.MAX_VALUE;

        for(Utente u : result){
            int punteggioUtenteAttuale = match.getOrDefault(u.getUsername(), 0);
            
            assertTrue(punteggioUtenteAttuale <= punteggioPrecedente);
            
            punteggioPrecedente = punteggioUtenteAttuale;
        }
    }

    @Test
    void testAnnunciUtenteSonoOrdinatiPerRilevanza() {
        String usernameCorrente = "admin";

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        
        Utente utenteCorrente = dbUtenti.get(usernameCorrente);
        List<String> listaCategoriePreferite = utenteCorrente.getCompetenzePreferite();
        String utenteAnnunci = "utenteMedio"; 

        List<Annuncio> listaAnnunciOrdinata = forYouService.getAnnunciOrdinati(usernameCorrente, utenteAnnunci);

        assertTrue(listaAnnunciOrdinata.size() >= 2);

        boolean match1 = listaCategoriePreferite.contains(listaAnnunciOrdinata.get(0).getCategoria());
        boolean match2 = listaCategoriePreferite.contains(listaAnnunciOrdinata.get(1).getCategoria());

        assertTrue(!match2 || match1);
    }
}