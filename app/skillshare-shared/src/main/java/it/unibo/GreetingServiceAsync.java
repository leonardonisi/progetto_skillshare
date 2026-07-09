package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
// interfaccia asincrona letta dal client
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<GreetingResponse> callback);
}
