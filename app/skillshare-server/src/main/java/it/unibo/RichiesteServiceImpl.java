package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RichiesteServiceImpl extends RemoteServiceServlet implements RichiesteService {

    // Salvataggio del riferimento alla mappa
    private static final ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();

    @Override
    public List<Annuncio> getMieRichieste(String username) {
        List<Annuncio> mieRichieste = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            return mieRichieste;
        }

        // Scorrimento della mappa statica
        for (Annuncio annuncio : dbAnnunci.values()) {
            if (username.equals(annuncio.getAutore())) {
                mieRichieste.add(annuncio);
            }
        }

        return mieRichieste;
    }
}