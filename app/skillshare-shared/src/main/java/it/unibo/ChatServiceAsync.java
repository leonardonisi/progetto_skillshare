package it.unibo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface ChatServiceAsync {
    void inviaMessaggio(String mittente, String destinatario, String testo, AsyncCallback<Void> callback);

    void getCronologia(String utente1, String utente2, AsyncCallback<List<Messaggio>> callback);

    void getConversazioniAttive(String utenteLoggato, AsyncCallback<List<String>> callback);
}