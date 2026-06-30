package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;
import jakarta.servlet.ServletException;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    public String authenticate(String username, String password) {
        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();

        if (!FieldVerifier.isValidName(username)) {
            return "Username non valido";
        }

        Utente utente = dbUtenti.get(username);

        if (utente == null) {
            return "Username inesistente";
        }

        if (!utente.getPassword().equals(password)) {
            return "Password errata";
        }

        return username;
    }
}