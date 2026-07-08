package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RichiesteServiceImpl extends RemoteServiceServlet implements RichiesteService {

    @Override
    public List<RichiestaScambio> getRichiesteScambio(String username) {
        try {
            List<RichiestaScambio> listaRichieste = new ArrayList<>();
            ConcurrentMap<Integer, RichiestaScambio> dbRichieste = DatabaseCore.getMappaRichieste();
            
            for (RichiestaScambio r : dbRichieste.values()) {
                if (r != null && username.equals(r.getRichiedenteUser())) {
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
}