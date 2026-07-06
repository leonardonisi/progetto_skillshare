package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class CreateAdServiceImpl extends RemoteServiceServlet implements CreateAdService {
    private static final DB db = DatabaseCore.getDB();

    private static final ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();

    private static final ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();

    @Override
    public boolean pubblicaAnnuncio(Annuncio annuncio) {
        if (annuncio == null || annuncio.getTitolo() == null || annuncio.getSkillOfferta() == null
                || annuncio.getControprestazione() == null || annuncio.getDisponibilita() == null
                || annuncio.getAutore() == null) {
            return false;
        }
        try {
            int id = dbAnnunci.size() + 1;
            dbAnnunci.put(id, annuncio);
            Utente autore = dbUtenti.get(annuncio.getAutore());

            autore.getAnnunciPubblicati().add(id);
            dbUtenti.put(autore.getUsername(), autore);

            DatabaseCore.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }

    @Override
    public boolean aggiornaAnnuncio(int id, Annuncio annuncioAggiornato) {
        if (annuncioAggiornato == null ||
                annuncioAggiornato.getTitolo() == null || annuncioAggiornato.getTitolo().trim().isEmpty() ||
                annuncioAggiornato.getCategoria() == null || annuncioAggiornato.getCategoria().trim().isEmpty() ||
                annuncioAggiornato.getSkillOfferta() == null || annuncioAggiornato.getSkillOfferta().trim().isEmpty() ||
                annuncioAggiornato.getDisponibilita() == null || annuncioAggiornato.getDisponibilita().trim().isEmpty()
                ||
                annuncioAggiornato.getAutore() == null) {
            return false;
        }

        try {
            if (!dbAnnunci.containsKey(id)) {
                return false;
            }
            // Sovrascrivo l'annuncio mantenendo lo stesso identico ID chiave
            dbAnnunci.put(id, annuncioAggiornato);
            DatabaseCore.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
