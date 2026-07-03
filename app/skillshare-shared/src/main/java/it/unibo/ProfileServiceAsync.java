package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface ProfileServiceAsync {
    void getCategorie(AsyncCallback<List<String>> callback);

    void getUtente(String username, AsyncCallback<Utente> callback);
    
    void saveUtente(Utente profile, AsyncCallback<Void> callback);
}
