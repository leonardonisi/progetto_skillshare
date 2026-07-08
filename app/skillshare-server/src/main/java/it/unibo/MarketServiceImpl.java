package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.Serializer;
import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import jakarta.servlet.ServletException;

public class MarketServiceImpl extends RemoteServiceServlet implements MarketService {

    @Override
    public List<Annuncio> getAnnunci(String usernameDaEscludere) {
        DB db = DatabaseCore.getDB();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();

        List<Annuncio> annunciFiltrati = new ArrayList<>();

        for (Annuncio a : dbAnnunci.values()) {
            if(a.isAttivo()){
                if (!a.getAutore().equals(usernameDaEscludere)) {
                    annunciFiltrati.add(a);
                }
            }
        }

        return annunciFiltrati;
    }

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }

    @Override
    public Utente getUtente(String username) {
        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();

        return dbUtenti.get(username);
    }

    @Override
    public void logout(String username) {
        System.out.println("Utente scollegato dal server: " + username);
    }
}