package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface ProfileServiceAsync {
    void getCategorie(AsyncCallback<List<String>> callback);

    void getProfile(String username, AsyncCallback<UserProfile> callback);
    
    void saveProfile(UserProfile profile, AsyncCallback<Void> callback);
}
