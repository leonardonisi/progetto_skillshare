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

    @Override
    public RichiestaScambio elaboraAzioneScambio(Integer idRichiesta, String username, boolean isConferma) {
        
        ConcurrentMap<Integer, RichiestaScambio> dbRichieste = DatabaseCore.getMappaRichieste();
        
        if (dbRichieste == null || !dbRichieste.containsKey(idRichiesta)) {
            return null;
        }

        RichiestaScambio richiesta = dbRichieste.get(idRichiesta);

        //Se l'utente ha cliccato la "X" (Rifiuto)
        if (!isConferma) {
            richiesta.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
        } else {
            // Se l'utente ha cliccato il "Tick" (Conferma), controllo chi è
            if (username.equals(richiesta.getProprietarioId())) {
                richiesta.setConfermatoDaProprietario(true);
            } else if (username.equals(richiesta.getRichiedenteId())) {
                richiesta.setConfermatoDaRichiedente(true);
            }

            // Controllo se hanno confermato ENTRAMBI
            if (richiesta.isConfermatoDaProprietario() && richiesta.isConfermatoDaRichiedente()) {
                richiesta.setStato(RichiestaScambio.StatoRichiesta.CONCLUSO);
            }
        }

        // Aggiorno la mappa e faccio il commit su MapDB
        dbRichieste.put(idRichiesta, richiesta);
        DatabaseCore.commit();

        return richiesta;
    }
}