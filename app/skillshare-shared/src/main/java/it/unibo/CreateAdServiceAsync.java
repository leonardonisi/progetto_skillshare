package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CreateAdServiceAsync {
    void pubblicaAnnuncio(Annuncio annuncio, AsyncCallback<Boolean> callback);
}
