package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RichiesteServiceAsync {
    void getRichiesteScambio(String username, AsyncCallback<List<RichiestaScambio>> callback);

    void getAnnuncioById(Integer id, AsyncCallback<Annuncio> callback);

    void getUtenteById(String id, AsyncCallback<Utente> callback);

    void elaboraAzioneScambio(Integer idRichiesta, String username, boolean isConferma, AsyncCallback<RichiestaScambio> callback);
}
