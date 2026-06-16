package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegisterServiceAsync {
    void register(String username, String password, String confirm_password, AsyncCallback<String> callback);
}