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
class RegisterServiceImplTest {

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;

    private RegisterServiceImpl registerService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseCore.enableTestMode();
        DatabaseCore.close(); 

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        dbUtenti.clear();
        dbUtenti.put("admin", new Utente("admin", "password"));
        DatabaseCore.commit();

        registerService = new RegisterServiceImpl();
    }

    @Test
    void register_ValidData_ShouldSaveUserInDatabase() {
        String usernameTest = "nuovo_utente";
        String passwordTest = "password123";
        
        String result = registerService.register(usernameTest, passwordTest, passwordTest);

        assertEquals("ok", result);

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();

        assertTrue(dbUtenti.containsKey(usernameTest), "L'utente non è stato salvato nel Database");
        assertEquals(passwordTest, dbUtenti.get(usernameTest).getPassword(), "La password salvata non corrisponde");
    }
}