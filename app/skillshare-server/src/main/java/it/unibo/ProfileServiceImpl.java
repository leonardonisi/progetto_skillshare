package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {
    private static final DB db = DatabaseCore.getDB();
    
    // Mappa per gli annunci
    private static final ConcurrentMap<Integer, Annuncio> dbAnnunci = db
            .hashMap("annunci", Serializer.INTEGER, Serializer.JAVA)
            .createOrOpen();

    //Mappa per salvare i profili utente su MapDB
    private static final ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }

    @Override
    public Utente getUtente(String username) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username non valido");
        }
        
        // Cerca l'utente nel database MapDB
        Utente profile = dbUtenti.get(username);

        return profile;
    }

    //salva il profilo dell'utente nel database MapDB
    @Override
    public void saveUtente(Utente profile) throws IllegalArgumentException {
        if (profile == null || profile.getUsername() == null) {
            throw new IllegalArgumentException("Profilo non valido o username mancante");
        }
        
        dbUtenti.put(profile.getUsername(), profile);
        
        //commit per salvare i cambiamenti su disco
        DatabaseCore.commit(); 
    }
}