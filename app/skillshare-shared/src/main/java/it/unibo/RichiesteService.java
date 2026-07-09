package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("richieste")
public interface RichiesteService extends RemoteService {
    List<RichiestaScambio> getRichiesteScambio(String username);

    Annuncio getAnnuncioById(Integer id);
    
    Utente getUtenteById(String usernameId);

    RichiestaScambio elaboraAzioneScambio(Integer idRichiesta, String username, boolean isConferma);

    RichiestaScambio inviaRichiesta(Integer idAnnuncio, String richiedente, String proprietario, String messaggio);
    
    RichiestaScambio gestisciRispostaRichiesta(Integer idRichiesta, boolean accetta);

    java.util.List<RichiestaScambio> getRichiesteRicevute(String username);

    java.util.HashMap<String, String> getMappaStatiRichieste();

    java.util.List<RichiestaScambio> getTutteLeRichieste();

    java.util.List<Annuncio> getMieRichieste(String username);
}