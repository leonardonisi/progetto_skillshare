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

        if (!isConferma) {
            richiesta.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
        } else {
            if (username.equals(richiesta.getProprietarioUser())) {
                richiesta.setConfermatoDaProprietario(true);
            } else if (username.equals(richiesta.getRichiedenteUser())) {
                richiesta.setConfermatoDaRichiedente(true);
            }

            if (richiesta.isConfermatoDaProprietario() && richiesta.isConfermatoDaRichiedente()) {
                richiesta.setStato(RichiestaScambio.StatoRichiesta.CONCLUSO);

                Annuncio annuncio = DatabaseCore.getMappaAnnunci().get(richiesta.getIdAnnuncio());
                if (annuncio != null && annuncio.getCategoria() != null) {
                    String tag = annuncio.getCategoria();

                    Utente proprietario = DatabaseCore.getMappaUtenti().get(richiesta.getProprietarioUser());
                    if (proprietario != null) {
                        proprietario.incrementaScambiCategoria(tag);
                        if (proprietario.getScambiConclusiPerCategoria().getOrDefault(tag, 0) >= 10) {
                            proprietario.aggiungiBadge("Esperto in " + tag);
                        }
                        DatabaseCore.getMappaUtenti().put(proprietario.getUsername(), proprietario);
                    }

                    Utente richiedente = DatabaseCore.getMappaUtenti().get(richiesta.getRichiedenteUser());
                    if (richiedente != null) {
                        richiedente.incrementaScambiCategoria(tag);
                        if (richiedente.getScambiConclusiPerCategoria().getOrDefault(tag, 0) >= 10) {
                            richiedente.aggiungiBadge("Esperto in " + tag);
                        }
                        DatabaseCore.getMappaUtenti().put(richiedente.getUsername(), richiedente);
                    }
                }
            }
        }

        dbRichieste.put(idRichiesta, richiesta);
        DatabaseCore.commit();
        return richiesta;
    }

    @Override
    public RichiestaScambio inviaRichiesta(Integer idAnnuncio, String richiedente, String proprietario, String messaggio) {

        ConcurrentMap<Integer, RichiestaScambio> dbRichieste = DatabaseCore.getMappaRichieste();

        for(RichiestaScambio r : dbRichieste.values()){
            if(r.getIdAnnuncio().equals(idAnnuncio) && r.getRichiedenteUser().equals(richiedente)){
                return null;
            }
        }

        int nuovoIdRichiesta = dbRichieste.keySet().stream()
                                        .max(Integer::compareTo)
                                        .orElse(0) + 1;

        RichiestaScambio nuova = new RichiestaScambio(nuovoIdRichiesta, idAnnuncio, richiedente, proprietario);
        nuova.setMessaggioProposta(messaggio);
        nuova.setStato(RichiestaScambio.StatoRichiesta.IN_ATTESA);

        Utente utenteRichiedente = DatabaseCore.getMappaUtenti().get(richiedente);
        if (utenteRichiedente != null) {
            utenteRichiedente.incrementaRichiesteInviate();
            if (utenteRichiedente.getContatoreRichiesteInviate() >= 10) {
                utenteRichiedente.aggiungiBadge("Richiedente Attivo");
            }
            DatabaseCore.getMappaUtenti().put(utenteRichiedente.getUsername(), utenteRichiedente);
        }

        DatabaseCore.getMappaRichieste().put(nuovoIdRichiesta, nuova);
        DatabaseCore.commit();
        
        return nuova;
    }

    @Override
    public RichiestaScambio gestisciRispostaRichiesta(Integer idRichiesta, boolean accetta) {
        System.out.println("Sto accettando la richiesta: " + idRichiesta);
        ConcurrentMap<Integer, RichiestaScambio> dbRichieste = DatabaseCore.getMappaRichieste();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
        RichiestaScambio r = dbRichieste.get(idRichiesta);

        if (r != null) {
            if (accetta) {
                r.setStato(RichiestaScambio.StatoRichiesta.ACCETTATO);
                Annuncio annuncio = dbAnnunci.get(r.getIdAnnuncio());
                annuncio.setAttivo(false);
                dbAnnunci.put(r.getIdAnnuncio(), annuncio);

                for (RichiestaScambio req : dbRichieste.values()) {
                    if (!req.getId().equals(idRichiesta)
                            && req.getIdAnnuncio().equals(r.getIdAnnuncio())
                            && req.getStato() == RichiestaScambio.StatoRichiesta.IN_ATTESA) {
                        req.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
                        dbRichieste.put(req.getId(), req);
                    }
                }
            } else {
                r.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
            }
            dbRichieste.put(idRichiesta, r);
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