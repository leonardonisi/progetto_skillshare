package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("richieste")
public interface RichiesteService extends RemoteService {
    List<RichiestaScambio> getRichiesteScambio(String username);

    Annuncio getAnnuncioById(Integer id);
    
    Utente getUtenteById(String usernameId);
}