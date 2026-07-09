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

    @Test
    void getValutazioniUtente_shouldReturnOnlyTargetUserReviews() {
        // Recupero corretto con la chiave String
        ConcurrentMap<String, Valutazione> dbValutazioni = DatabaseCore.getMappaValutazioni();
        dbValutazioni.clear();

        // Prepariamo 3 recensioni: 2 destinate a "mario", 1 a "admin"
        Valutazione v1 = new Valutazione.Builder()
            .id(101)
            .autore("luigi")
            .destinatario("mario")
            .voto(5)
            .recensione("Bravissimo e preparato!")
            .build();

        Valutazione v2 = new Valutazione.Builder()
            .id(102)
            .autore("toad")
            .destinatario("mario")
            .voto(3)
            .recensione("Ok, ma in ritardo.")
            .build();

        Valutazione v3 = new Valutazione.Builder()
            .id(103)
            .autore("mario")
            .destinatario("admin")
            .voto(4)
            .recensione("Ottima skill.")
            .build();

        // Inseriamo nel DB fittizio usando la chiave univoca del server (ID_Autore)
        dbValutazioni.put(v1.getId() + "_" + v1.getAutore(), v1);
        dbValutazioni.put(v2.getId() + "_" + v2.getAutore(), v2);
        dbValutazioni.put(v3.getId() + "_" + v3.getAutore(), v3);
        DatabaseCore.commit();

        // ACT
        List<Valutazione> recensioniMario = skillService.getValutazioniUtente("mario");

        // ASSERT
        assertNotNull(recensioniMario, "La lista non deve essere null");
        assertEquals(2, recensioniMario.size(), "Dovrebbero esserci esattamente 2 recensioni per mario");
        
        boolean contieneCinque = recensioniMario.stream().anyMatch(v -> v.getVoto() == 5);
        boolean contieneTre = recensioniMario.stream().anyMatch(v -> v.getVoto() == 3);
        assertTrue(contieneCinque && contieneTre, "Le recensioni restituite devono contenere i voti corretti");
        
        boolean contieneAltroDestinatario = recensioniMario.stream().anyMatch(v -> !v.getDestinatario().equals("mario"));
        assertFalse(contieneAltroDestinatario, "Non deve contenere valutazioni destinate ad altri utenti");
    }
}