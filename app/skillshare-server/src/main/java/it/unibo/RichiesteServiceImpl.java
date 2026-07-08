package it.unibo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

import it.unibo.RichiestaScambio.StatoRichiesta;

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
    public java.util.List<Annuncio> getMieRichieste(String username) {
        java.util.List<Annuncio> richiesteInviateDaMe = new java.util.ArrayList<>();
        for (RichiestaScambio r : DatabaseCore.getMappaRichieste().values()) {
            // Filtro stringente: l'utente deve essere il RICHIEDENTE, non il proprietario!
            if (r.getRichiedenteUser().equals(username)) {
                Annuncio a = DatabaseCore.getMappaAnnunci().get(r.getIdAnnuncio());
                if (a != null) {
                    richiesteInviateDaMe.add(a);
                }
            }
        }
        return richiesteInviateDaMe;
    }
    
    
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

        // Se l'utente ha cliccato la "X" (Rifiuto)
        if (!isConferma) {
            richiesta.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
        } else {
            // Se l'utente ha cliccato il "Tick" (Conferma), controllo chi è
            if (username.equals(richiesta.getProprietarioUser())) {
                richiesta.setConfermatoDaProprietario(true);
            } else if (username.equals(richiesta.getRichiedenteUser())) {
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

    @Override
    public RichiestaScambio inviaRichiesta(Integer idAnnuncio, String richiedente, String proprietario,
            String messaggio) {
        // Usiamo l'ID dell'annuncio sia come ID richiesta che come chiave della mappa
        // per consistenza totale
        RichiestaScambio nuova = new RichiestaScambio(idAnnuncio, idAnnuncio, richiedente, proprietario);
        nuova.setMessaggioProposta(messaggio);
        nuova.setStato(RichiestaScambio.StatoRichiesta.IN_ATTESA);

        // Salva usando l'ID annuncio come chiave
        DatabaseCore.getMappaRichieste().put(idAnnuncio, nuova);
        DatabaseCore.commit();
        return nuova;
    }

    @Override
    public RichiestaScambio gestisciRispostaRichiesta(Integer idRichiesta, boolean accetta) {
        // idRichiesta corrisponde all'ID dell'annuncio salvato come chiave
        RichiestaScambio r = DatabaseCore.getMappaRichieste().get(idRichiesta);
        if (r != null) {
            r.setStato(accetta ? RichiestaScambio.StatoRichiesta.ACCETTATO : RichiestaScambio.StatoRichiesta.RIFIUTATO);
            DatabaseCore.getMappaRichieste().put(idRichiesta, r);
            DatabaseCore.commit();
        }
        return r;
    }

    @Override
    public java.util.List<RichiestaScambio> getRichiesteRicevute(String username) {
        java.util.List<RichiestaScambio> ricevute = new java.util.ArrayList<>();
        for (RichiestaScambio r : DatabaseCore.getMappaRichieste().values()) {
            if (r.getProprietarioUser().equals(username) && r.getStato() == StatoRichiesta.IN_ATTESA) {
                ricevute.add(r);
            }
        }
        return ricevute;
    }

    @Override
    public java.util.HashMap<String, String> getMappaStatiRichieste() {
        java.util.HashMap<String, String> mappa = new java.util.HashMap<>();
        for (RichiestaScambio r : DatabaseCore.getMappaRichieste().values()) {
            // Convertiamo l'ID in stringa prima di inserirlo nella mappa
            if (r.getStato() == RichiestaScambio.StatoRichiesta.ACCETTATO) {
                mappa.put(String.valueOf(r.getIdAnnuncio()), "ACCETTATA");
            } else if (r.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO) {
                mappa.put(String.valueOf(r.getIdAnnuncio()), "CONCLUSA");
            }
        }
        return mappa;
    }

    @Override
    public java.util.List<RichiestaScambio> getTutteLeRichieste() {
        return new java.util.ArrayList<>(DatabaseCore.getMappaRichieste().values());
    }
}