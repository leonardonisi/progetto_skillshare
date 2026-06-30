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
class LoginServiceImplTest {

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;

    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseCore.enableTestMode();
        DatabaseCore.close(); 

        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        dbUtenti.clear();
        dbUtenti.put("admin", new Utente("admin", "password"));
        DatabaseCore.commit();

        loginService = new LoginServiceImpl();
    }

    @Test
    void authenticate_validCredentials_shouldReturnUsername() {
        String usernameRestituito = loginService.authenticate("admin", "password");
        assertEquals("admin", usernameRestituito);
    }

    @Test
    void authenticate_ShouldReturnError_WhenUsernameDoesNotExist() {
        String result = loginService.authenticate("inesistente", "password");
        assertEquals("Username inesistente", result);
    }

    @Test
    void authenticate_ShouldReturnError_WhenPasswordIsIncorrect() {
        String result = loginService.authenticate("admin", "password_sbagliata");
        assertEquals("Password errata", result);
    }
}