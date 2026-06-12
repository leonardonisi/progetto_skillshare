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

    private LoginServiceImpl service;

    private static final DB db = DatabaseCore.getDB();

    @BeforeEach
    void setUp() throws Exception {
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getServerInfo()).thenReturn("MockServer/1.0");
        when(request.getHeader("User-Agent")).thenReturn("MockBrowser/1.0");
        service = new LoginServiceImpl();
        service.init(servletConfig);
        // Crea un ThreadLocal con la request mock e iniettalo via reflection
        ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();
        threadLocal.set(request);
        Field field = AbstractRemoteServiceServlet.class
                .getDeclaredField("perThreadRequest");
        field.setAccessible(true);
        field.set(service, threadLocal); // sostituiamo il campo con il nostro ThreadLocal

        // Database e aggiunta di utente test
        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, String> dbUtenti = db.hashMap("utenti", Serializer.STRING, Serializer.STRING)
                .createOrOpen();
        dbUtenti.clear();
        dbUtenti.put("admin", "password123");
        DatabaseCore.commit();

    }

    @Test
    void authenticate_validCredentials_shouldReturnUsername() throws IllegalArgumentException {
        String usernameRestituito = service.authenticate("admin", "password123");
        assertEquals("admin", usernameRestituito);
    }

    @Test
    void authenticate_ShouldThrowException_WhenUsernameDoesNotExist() {
        // Verifichiamo che il metodo lanci IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.authenticate("inesistente", "qualcosa");
        });
        assertEquals("Username inesistente", exception.getMessage());
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIsIncorrect() {
        // Verifichiamo che il metodo lanci IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.authenticate("admin", "password_sbagliata");
        });
        assertEquals("Password errata", exception.getMessage());
    }
}