package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

@RemoteServiceRelativePath("profile")
public interface ProfileService extends RemoteService {
    List<String> getCategorie();

    UserProfile getProfile(String username) throws IllegalArgumentException;

    void saveProfile(UserProfile profile) throws IllegalArgumentException;
}