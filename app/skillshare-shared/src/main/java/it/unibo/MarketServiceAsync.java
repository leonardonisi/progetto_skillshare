package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MarketServiceAsync {
    void getAnnunci(AsyncCallback<List<Annuncio>> callback);
}