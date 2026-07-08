package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("skillService")
public interface SkillService extends RemoteService {
    List<Annuncio> getAnnunciPubblicati(String username);

    List<RichiestaScambio> getRichiesteScambio(String username);

    Annuncio getAnnuncioById(Integer id);
    
    Utente getUtenteById(String usernameId);

    boolean deleteSkill(int idAnnuncio);
}
