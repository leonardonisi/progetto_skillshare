package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
// interfaccia sincrona letta dal server
@RemoteServiceRelativePath("greet") // percorso relativo per raggiungere il servizio
public interface GreetingService extends RemoteService {
	GreetingResponse greetServer(String name) throws IllegalArgumentException;
}
