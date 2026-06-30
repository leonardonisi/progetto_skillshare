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
    private static final ConcurrentMap<String, UserProfile> dbProfili = db
            .hashMap("profili_utenti", Serializer.STRING, Serializer.JAVA)
            .createOrOpen();

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }

    @Override
    public UserProfile getProfile(String username) throws IllegalArgumentException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username non valido");
        }
        
        // Cerca l'utente nel database MapDB
        UserProfile profile = dbProfili.get(username);
        
        // Se è la prima volta che l'utente accede e non ha un profilo salvato, 
        // gliene restituiamo uno vuoto per evitare crash
        if (profile == null) {
            profile = new UserProfile(username, "Scrivi qui la tua bio...", "Località sconosciuta", "", new ArrayList<>());
        }
        
        return profile;
    }

    //salva il profilo dell'utente nel database MapDB
    @Override
    public void saveProfile(UserProfile profile) throws IllegalArgumentException {
        if (profile == null || profile.getUsername() == null) {
            throw new IllegalArgumentException("Profilo non valido o username mancante");
        }
        
        dbProfili.put(profile.getUsername(), profile);
        
        //commit per salvare i cambiamenti su disco
        DatabaseCore.commit(); 
    }
}