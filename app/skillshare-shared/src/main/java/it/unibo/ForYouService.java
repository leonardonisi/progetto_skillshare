package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("foryou")
public interface ForYouService extends RemoteService {
    List<Utente> getUtentiConsigliati(String usernameCorrente) throws IllegalArgumentException;

    List<Annuncio> getAnnunciOrdinati(String utenteCorrente, String utenteAnnunci);

    Utente getUtente(String username);
}