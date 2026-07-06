package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface CreateAdServiceAsync {
    void pubblicaAnnuncio(Annuncio annuncio, AsyncCallback<Boolean> callback);

    void getCategorie(AsyncCallback<List<String>> callback);

    void aggiornaAnnuncio(int id, Annuncio annuncioAggiornato, AsyncCallback<Boolean> callback);
}
