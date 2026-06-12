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

    public String authenticate(String username, String password) {
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(username)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long");
        }
        if (!dbUtenti.containsKey(username)) {
            throw new IllegalArgumentException("Username inesistente");
        }
        String passwordCorretta = dbUtenti.get(username);

        if (!passwordCorretta.equals(password)) {
            throw new IllegalArgumentException("Password errata");
        }
        return username;
    }
}