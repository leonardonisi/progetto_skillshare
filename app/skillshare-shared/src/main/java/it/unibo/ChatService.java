package it.unibo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

@RemoteServiceRelativePath("chat")
public interface ChatService extends RemoteService {
    // Salva un messaggio sul database
    void inviaMessaggio(String mittente, String destinatario, String testo);

    // Recupera la cronologia tra due utenti ordinata per tempo crescente
    List<Messaggio> getCronologia(String utente1, String utente2);

    // Recupera gli username degli utenti con cui c'è una chat attiva
    List<String> getConversazioniAttive(String utenteLoggato);
}