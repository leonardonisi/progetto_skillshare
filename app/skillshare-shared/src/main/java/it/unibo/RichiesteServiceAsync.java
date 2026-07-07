package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RichiesteServiceAsync {
    void getMieRichieste(String username, AsyncCallback<List<Annuncio>> callback);

    void elaboraAzioneScambio(Integer idRichiesta, String username, boolean isConferma, AsyncCallback<RichiestaScambio> callback);
}
