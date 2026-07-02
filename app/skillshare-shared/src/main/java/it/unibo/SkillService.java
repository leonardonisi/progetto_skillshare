package it.unibo;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

// Importante: questo definisce l'URL a cui il client farà la richiesta
@RemoteServiceRelativePath("skillService")
public interface SkillService extends RemoteService {
    List<Annuncio> getMieSkills(String username);
}
