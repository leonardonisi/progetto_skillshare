package it.unibo;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
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
    void getMieSkills_shouldReturnOnlyAdminSkills() {
        List<Annuncio> skillsRestituite = skillService.getMieSkills("admin");
        
        assertNotNull(skillsRestituite);
        assertEquals(2, skillsRestituite.size(), "Deve restituire solo le 2 skill dell'admin");
        
        boolean contieneMario = skillsRestituite.stream().anyMatch(a -> a.getAutore().equals("mario"));
        assertFalse(contieneMario, "Non deve contenere skill di altri utenti");
    }
}