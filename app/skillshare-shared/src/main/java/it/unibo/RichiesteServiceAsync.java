package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RichiesteServiceAsync {
    void getRichiesteScambio(String username, AsyncCallback<List<RichiestaScambio>> callback);

    void getAnnuncioById(Integer id, AsyncCallback<Annuncio> callback);

    void getUtenteById(String id, AsyncCallback<Utente> callback);

    void elaboraAzioneScambio(Integer idRichiesta, String username, boolean isConferma, AsyncCallback<RichiestaScambio> callback);

    void inviaRichiesta(Integer idAnnuncio, String richiedente, String proprietario, String messaggio, AsyncCallback<RichiestaScambio> callback);
    
    void gestisciRispostaRichiesta(Integer idRichiesta, boolean accetta, AsyncCallback<RichiestaScambio> callback);

    void getRichiesteRicevute(String username,AsyncCallback<java.util.List<RichiestaScambio>> callback);

    void getMappaStatiRichieste(AsyncCallback<java.util.HashMap<String, String>> callback);

    void getTutteLeRichieste(AsyncCallback<java.util.List<RichiestaScambio>> callback);

    void getMieRichieste(String username, AsyncCallback<java.util.List<Annuncio>> callback);
}
