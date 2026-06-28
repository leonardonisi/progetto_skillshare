package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

@RemoteServiceRelativePath("createAd")
public interface CreateAdService extends RemoteService {
    boolean pubblicaAnnuncio(Annuncio annuncio) throws IllegalArgumentException;

    List<String> getCategorie();
}
