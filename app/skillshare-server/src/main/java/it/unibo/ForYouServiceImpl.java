package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.HashMap;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.Serializer;
import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import jakarta.servlet.ServletException;

public class ForYouServiceImpl extends RemoteServiceServlet implements ForYouService  {

    @Override
    public List<Utente> getUtentiConsigliati(String usernameCorrente) {
        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();

        Utente richiedente = dbUtenti.get(usernameCorrente);
        List<String> categoriePreferite = richiedente.getCompetenzePreferite();

        Map<String, Integer> mappaMatch = new HashMap<>();
        Map<String, Integer> mappaTotaleAnnunci = new HashMap<>();
        
        List<Utente> utentiFiltratiOrdinati = new ArrayList<>();
        for (Utente u : dbUtenti.values()) {
            if (!u.getUsername().equals(usernameCorrente)) {
                mappaMatch.put(u.getUsername(), 0);
                mappaTotaleAnnunci.put(u.getUsername(), 0);
                utentiFiltratiOrdinati.add(u);
            }
        }
        
        for (Annuncio annuncio : dbAnnunci.values()) {
            if(annuncio.isAttivo()){
                String autore = annuncio.getAutore();
                if (autore == null) continue;

                // Incrementiamo totale annunci pubblicati dall'autore
                mappaTotaleAnnunci.put(autore, mappaTotaleAnnunci.getOrDefault(autore, 0) + 1);

                // Incrementiamo match
                if (categoriePreferite != null && categoriePreferite.contains(annuncio.getCategoria())) {
                    mappaMatch.put(autore, mappaMatch.getOrDefault(autore, 0) + 1);
                }
            }
        }

        utentiFiltratiOrdinati.sort((u1, u2) -> {
            int match1 = mappaMatch.getOrDefault(u1.getUsername(), 0);
            int match2 = mappaMatch.getOrDefault(u2.getUsername(), 0);

            // Regola 1: Ordina per numero di Match (Decrescente)
            if (match1 != match2) {
                return Integer.compare(match2, match1); 
            }

            // Regola 2: Se i match sono uguali, ordina per annunci totali (Decrescente)
            int totale1 = mappaTotaleAnnunci.getOrDefault(u1.getUsername(), 0);
            int totale2 = mappaTotaleAnnunci.getOrDefault(u2.getUsername(), 0);
            
            return Integer.compare(totale2, totale1);
        });

        return utentiFiltratiOrdinati;
    }

    @Override
    public List<Annuncio> getAnnunciOrdinati(String utenteCorrente, String utenteAnnunci){
        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();

        List<Annuncio> annunciOrdinati = new ArrayList<>();

        Utente richiedente = dbUtenti.get(utenteCorrente);
        List<String> categoriePreferite = richiedente.getCompetenzePreferite();

        List<Annuncio> annunciUtenteScelto = new ArrayList<>();
        List<Annuncio> cacheSupporto = new ArrayList<>();

        for (Annuncio a : dbAnnunci.values()){
            if(a.isAttivo()){
                String autore = a.getAutore();
                if (autore == null) continue;
                else if (autore.equals(utenteAnnunci))
                    annunciUtenteScelto.add(a);
            }
        }

        for (Annuncio a : annunciUtenteScelto){
            String categoria = a.getCategoria();
            if (categoria == null) continue;
            else if (categoriePreferite.contains(categoria))
                annunciOrdinati.add(a);
            else
                cacheSupporto.add(a);
        }

        annunciOrdinati.addAll(cacheSupporto);

        return annunciOrdinati;
    }

    @Override
    public Utente getUtente(String username){
        DB db = DatabaseCore.getDB();
        ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();

        return dbUtenti.get(username);
    }
}