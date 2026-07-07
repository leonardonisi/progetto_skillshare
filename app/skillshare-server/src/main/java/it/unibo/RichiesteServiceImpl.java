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