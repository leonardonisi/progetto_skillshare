package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import org.mapdb.DB;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;

public class CreateAdServiceImpl extends RemoteServiceServlet implements CreateAdService {
    private static final DB db = DatabaseCore.getDB();
    private static final ConcurrentMap<Integer, Annuncio> dbAnnunci = db
            .hashMap("annunci", Serializer.INTEGER, Serializer.JAVA)
            .createOrOpen();

    @Override
    public boolean pubblicaAnnuncio(Annuncio annuncio) {
        if (annuncio == null || annuncio.getTitolo() == null || annuncio.getSkillOfferta() == null
                || annuncio.getControprestazioneCercata() == null || annuncio.getDisponibilita() == null
                || annuncio.getUtenteId() == null) {
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

}
