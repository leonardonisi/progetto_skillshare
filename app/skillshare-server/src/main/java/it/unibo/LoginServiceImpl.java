package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    private static final DB db = DatabaseCore.getDB();
    private static final ConcurrentMap<String, String> dbUtenti = db
            .hashMap("utenti", Serializer.STRING, Serializer.STRING)
            .createOrOpen();

    static {
        // Aggiunta di un utente admin di default
        dbUtenti.put("admin", "password");
        DatabaseCore.commit();
    }

    public String authenticate(String username, String password) {
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(username)) {
            return ("Username non valido");
        }
        if (!dbUtenti.containsKey(username)) {
            return "Username inesistente";
        }
        String passwordCorretta = dbUtenti.get(username);

        if (!passwordCorretta.equals(password)) {
            return "Password errata";
        }
        return username;
    }
}