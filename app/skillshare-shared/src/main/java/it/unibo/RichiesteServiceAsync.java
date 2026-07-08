package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RichiesteServiceAsync {
    void getMieRichieste(String username, AsyncCallback<List<Annuncio>> callback);

    void elaboraAzioneScambio(Integer idRichiesta, String username, boolean isConferma, AsyncCallback<RichiestaScambio> callback);

    void inviaRichiesta(Integer idAnnuncio, String richiedente, String proprietario, String messaggio, com.google.gwt.user.client.rpc.AsyncCallback<RichiestaScambio> callback);
    
    void gestisciRispostaRichiesta(Integer idRichiesta, boolean accetta, com.google.gwt.user.client.rpc.AsyncCallback<RichiestaScambio> callback);

    void getRichiesteRicevute(String username, com.google.gwt.user.client.rpc.AsyncCallback<java.util.List<RichiestaScambio>> callback);

    void getMappaStatiRichieste(com.google.gwt.user.client.rpc.AsyncCallback<java.util.HashMap<String, String>> callback);

    void getTutteLeRichieste(com.google.gwt.user.client.rpc.AsyncCallback<java.util.List<RichiestaScambio>> callback);
}
