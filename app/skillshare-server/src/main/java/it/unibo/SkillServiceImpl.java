package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

public class SkillServiceImpl extends RemoteServiceServlet implements SkillService {

    @Override
    public List<Annuncio> getMieSkills(String username) {
        // Recupero della mappa dal database
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
        List<Annuncio> mieSkills = new ArrayList<>();

        // Controllo di sicurezza base
        if (username == null || username.trim().isEmpty()) {
            return mieSkills;
        }

        // Filtraggio degli annunci che appartengono solo all'utente richiesto
        for (Annuncio a : dbAnnunci.values()) {
            if (username.equals(a.getAutore())) {
                mieSkills.add(a);
            }
        }

        return mieSkills;
    }
}
