package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ForYouServiceAsync {
    void getUtentiConsigliati(String usernameCorrente, AsyncCallback<List<Utente>> callback);

    void getUtente(String username, AsyncCallback<Utente> callback);
}