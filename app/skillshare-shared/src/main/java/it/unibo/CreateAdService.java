package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("createAd")
public interface CreateAdService extends RemoteService {
    boolean pubblicaAnnuncio(Annuncio annuncio) throws IllegalArgumentException;
}
