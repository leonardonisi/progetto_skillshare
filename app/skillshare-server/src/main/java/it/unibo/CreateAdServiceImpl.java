package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class CreateAdServiceImpl extends RemoteServiceServlet implements CreateAdService {
    private static final DB db = DatabaseCore.getDB();
    private static final ConcurrentMap<Integer, Annuncio> dbAnnunci = db
            .hashMap("annunci", Serializer.INTEGER, Serializer.JAVA)
            .createOrOpen();

    @Override
    public boolean pubblicaAnnuncio(Annuncio annuncio) {
        if (annuncio == null || annuncio.getTitolo() == null || annuncio.getSkillOfferta() == null
                || annuncio.getControprestazione() == null || annuncio.getDisponibilita() == null
                || annuncio.getUtente() == null) {
            return false;
        }
        try {
            int id = dbAnnunci.size() + 1; // Genera un ID incrementale
            dbAnnunci.put(id, annuncio); // Imposta l'ID nell'annuncio
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

}
