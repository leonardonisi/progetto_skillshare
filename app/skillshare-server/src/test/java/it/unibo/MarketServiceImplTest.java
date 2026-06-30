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
import java.util.List;
import jakarta.servlet.ServletException;

public class MarketServiceImplTest {

    private MarketServiceImpl marketService;

    @BeforeEach
    public void setUp() {
        DatabaseCore.enableTestMode();
        DatabaseCore.close(); 

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        dbUtenti.clear();
        dbUtenti.put("admin", new Utente("admin", "password"));
        DatabaseCore.commit();

        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        Annuncio a = new Annuncio.Builder()
                    .autore("admin")
                    .titolo("Skill #1")
                    .categoria("Sviluppo Software")
                    .skillOfferta("Java GWT")
                    .controprestazioneCercata("Grafica")
                    .disponibilita("Weekend")
                    .build();
        dbAnnunci.put(1, a);
        DatabaseCore.commit();

        marketService = new MarketServiceImpl();
    }

    @Test
    public void testGetAnnunciRitornaListaAnnunci() throws ServletException {
        MarketServiceImpl marketService = new MarketServiceImpl();
        marketService.init();

        List<Annuncio> annunci = marketService.getAnnunci("testUser");

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

        assertNotNull(primoAnnuncio.getAutore(), "L'ID utente non deve essere null");
        assertFalse(primoAnnuncio.getAutore().trim().isEmpty(), "L'ID utente non deve essere vuoto");
    }
}