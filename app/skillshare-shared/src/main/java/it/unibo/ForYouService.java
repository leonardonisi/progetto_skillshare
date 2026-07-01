package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("foryou")
public interface ForYouService extends RemoteService {
    List<Utente> getUtentiConsigliati(String usernameCorrente) throws IllegalArgumentException;

    Utente getUtente(String username);
}