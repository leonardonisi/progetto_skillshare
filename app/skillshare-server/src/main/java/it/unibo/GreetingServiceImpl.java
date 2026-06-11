package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public GreetingResponse greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		// crea la risposta da inviare al client vuota
		GreetingResponse response = new GreetingResponse();

		// inserissce dati di rete
		response.setServerInfo(getServletContext().getServerInfo());
		response.setUserAgent(getThreadLocalRequest().getHeader("User-Agent"));

		// richiama l'oggetto Java che svolge la logica e lo inserisce nella risposta
		response.setGreeting(new Greeting().greet(input));

		// ritorna la risposta al client
		return response;
	}
}
