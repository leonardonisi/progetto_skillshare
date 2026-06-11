package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet { // implements
    // LoginService {
    public String authenticate(String username, String password) throws IllegalArgumentException {
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(username)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long");
        }
        if ("admin".equals(username) && "password123".equals(password)) {
            return username;
        }
        return null;
    }
}