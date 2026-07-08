package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

public class SkillServiceImpl extends RemoteServiceServlet implements SkillService {

    @Override
    public List<Annuncio> getAnnunciPubblicati(String username) {
        // Recupero della mappa dal database
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
        List<Annuncio> annunciPubblicati = new ArrayList<>();

        // Controllo di sicurezza base
        if (username == null || username.trim().isEmpty()) {
            return annunciPubblicati;
        }

        // Filtraggio degli annunci che appartengono solo all'utente richiesto
        for (java.util.Map.Entry<Integer, Annuncio> entry : dbAnnunci.entrySet()) {
            Annuncio a = entry.getValue();
            if (a != null && username.equals(a.getAutore())) {
                a.setId(entry.getKey());
                annunciPubblicati.add(a);
            }
        }

        return annunciPubblicati;
    }

    @Override
    public List<RichiestaScambio> getRichiesteScambio(String username) {
        try {
            List<RichiestaScambio> listaRichieste = new ArrayList<>();
            ConcurrentMap<Integer, RichiestaScambio> dbRichieste = DatabaseCore.getMappaRichieste();
            
            for (RichiestaScambio r : dbRichieste.values()) {
                if (r != null && username.equals(r.getProprietarioUser())) {
                    listaRichieste.add(r);
                }
            }
            return listaRichieste;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    @Override
    public Annuncio getAnnuncioById(Integer id) {
        try {
            ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
            return dbAnnunci.get(id);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    @Override
    public Utente getUtenteById(String usernameId) {
        try {
            ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();
            return dbUtenti.get(usernameId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteSkill(int idAnnuncio) {
        // Recupero della mappa dal database
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
        
        // Verifica esistenza, rimozione e salvataggio
        if (dbAnnunci.containsKey(idAnnuncio)) {
            dbAnnunci.remove(idAnnuncio);
            DatabaseCore.commit();
            return true;
        }
        
        return false;
    }

    @Override
    public boolean salvaValutazione(Valutazione valutazione) {
        ConcurrentMap<String, Valutazione> dbValutazioni = DatabaseCore.getMappaValutazioni();
        String chiaveUnivoca = valutazione.getId() + "_" + valutazione.getAutore();

        if (dbValutazioni.containsKey(chiaveUnivoca)) {
            return false; 
        }

        dbValutazioni.put(chiaveUnivoca, valutazione);
        DatabaseCore.commit();
        
        return true;
    }
}
