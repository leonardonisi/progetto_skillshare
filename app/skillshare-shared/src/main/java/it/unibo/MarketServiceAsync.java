package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MarketServiceAsync {
    void getAnnunci(String usernameDaEscludere, AsyncCallback<List<Annuncio>> callback);

    void getCategorie(AsyncCallback<List<String>> callback);

    void getUtente(String username, AsyncCallback<Utente> callback);
}