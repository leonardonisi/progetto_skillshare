package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
    void authenticate(String username, String password, AsyncCallback<String> callback);
}