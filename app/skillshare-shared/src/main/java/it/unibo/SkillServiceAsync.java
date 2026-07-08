package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SkillServiceAsync {
    void getAnnunciPubblicati(String username, AsyncCallback<List<Annuncio>> callback);

    void getRichiesteScambio(String username, AsyncCallback<List<RichiestaScambio>> callback);

    void getAnnuncioById(Integer id, AsyncCallback<Annuncio> callback);

    void getUtenteById(String id, AsyncCallback<Utente> callback);

    void deleteSkill(int idAnnuncio, AsyncCallback<Boolean> callback);
    void salvaValutazione(Valutazione valutazione, AsyncCallback<Boolean> callback);
    void getValutazioniUtente(String username, AsyncCallback<List<Valutazione>> callback);
}