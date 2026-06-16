package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("serial")
public class RegisterServiceImpl extends RemoteServiceServlet implements RegisterService {

    private static final DB db = DatabaseCore.getDB();
    private static final ConcurrentMap<String, String> dbUtenti = 
        db.hashMap("utenti", Serializer.STRING, Serializer.STRING).createOrOpen();

    static {
        // Aggiunta di un utente admin di default
        dbUtenti.put("usato", "password");
        DatabaseCore.commit();
    }

    public String register(String username, String password, String confirm_password) {
        if (!FieldVerifier.isValidName(username)) {
            return ("Username troppo corto");
        }
        if (!FieldVerifier.isValidName(password)) {
            return ("Password troppo corta");
        }
        if (dbUtenti.containsKey(username)) {
            return "Username già usato";
        }
        if (!password.equals(confirm_password)) {
            return "Password non conforme";
        }
        else{
            return "OK";
        }
    }
}