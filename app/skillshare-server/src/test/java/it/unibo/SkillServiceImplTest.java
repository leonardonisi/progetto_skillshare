package it.unibo;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapdb.DB;
import org.mapdb.Serializer;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SkillServiceImplTest {

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;

    private SkillServiceImpl skillService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseCore.enableTestMode();
        DatabaseCore.close();

        DB db = DatabaseCore.getDB();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA)
                .createOrOpen();
        dbAnnunci.clear();

        // Inserimento delle skill per l'utente "admin"
        Annuncio skill1 = new Annuncio.Builder()
                .autore("admin")
                .titolo("Cucina Pollo")
                .categoria("Cucina")
                .build();

        Annuncio skill2 = new Annuncio.Builder()
                .autore("admin")
                .titolo("Programmazione Java")
                .categoria("Sviluppo Software")
                .build();

        // Inseriamo una skill di "mario" per testare che il filtro funzioni
        Annuncio skillMario = new Annuncio.Builder()
                .autore("mario")
                .titolo("Falsa Skill")
                .categoria("Altro")
                .build();

        dbAnnunci.put(1, skill1);
        dbAnnunci.put(2, skill2);
        dbAnnunci.put(3, skillMario);
        DatabaseCore.commit();

        skillService = new SkillServiceImpl();
    }

    @Test
    void getAnnunciPubblicati_shouldReturnOnlyAdminSkills() {
        List<Annuncio> skillsRestituite = skillService.getAnnunciPubblicati("admin");

        assertNotNull(skillsRestituite);
        assertEquals(2, skillsRestituite.size());

        boolean contieneMario = skillsRestituite.stream().anyMatch(a -> a.getAutore().equals("mario"));
        assertFalse(contieneMario, "Non deve contenere skill di altri utenti");
    }

    @Test
    void getMieSkills_shouldInjectCorrectMapDBId() {
        List<Annuncio> skillsRestituite = skillService.getAnnunciPubblicati("admin");

        assertNotNull(skillsRestituite);
        assertFalse(skillsRestituite.isEmpty(), "La lista delle skill restituite non deve essere vuota");

        // Verifico dinamicamente che ogni skill dell'utente abbia un ID valido
        // assegnato dal DB
        for (Annuncio a : skillsRestituite) {
            assertTrue(a.getId() > 0,
                    "L'ID associato all'annuncio '" + a.getTitolo() + "' deve essere valido e maggiore di 0");
        }
    }

    @Test
    void salvaValutazione_deveImpedireValutazioniMultiplePerLoStessoScambio() {
        // Preparazione dati: Assicuriamoci che la mappa delle valutazioni sia vuota per il test
        ConcurrentMap<String, Valutazione> dbValutazioni = DatabaseCore.getMappaValutazioni();
        dbValutazioni.clear();

        Valutazione valutazione = new Valutazione.Builder()
            .id(999)
            .autore("admin")
            .voto(5)
            .recensione("Ottimo scambio!")
            .build();

        // Prima chiamata: deve avere successo
        boolean primoTentativo = skillService.salvaValutazione(valutazione);
        org.junit.jupiter.api.Assertions.assertTrue(primoTentativo, "La prima valutazione deve essere salvata con successo");
        org.junit.jupiter.api.Assertions.assertEquals(1, dbValutazioni.size(), "Deve esserci 1 valutazione nel db");

        // Seconda chiamata (stesso scambio): deve fallire
        Valutazione valutazioneDuplicata = new Valutazione.Builder()
            .id(999)
            .autore("admin")
            .voto(3)
            .recensione("Provo a cambiare il voto")
            .build();
            
        boolean secondoTentativo = skillService.salvaValutazione(valutazioneDuplicata);
        assertFalse(secondoTentativo, "Non deve permettere il salvataggio di una seconda valutazione per lo stesso scambio");
        
        // Verifica che il database non sia stato modificato dal secondo tentativo
        assertEquals(1, dbValutazioni.size());
        assertEquals(5, dbValutazioni.get("999_admin").getVoto(), "Il voto deve rimanere quello della prima valutazione");
    }
}