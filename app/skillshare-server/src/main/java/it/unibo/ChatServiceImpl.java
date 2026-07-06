package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {

    // Riferimento statico alla mappa condivisa recuperata da DatabaseCore
    private static final ConcurrentMap<Integer, Messaggio> dbMessaggi = DatabaseCore.getMappaMessaggi();

    @Override
    public synchronized void inviaMessaggio(String mittente, String destinatario, String testo) {
        if (testo == null || testo.trim().isEmpty() || mittente == null || destinatario == null) {
            return;
        }

        try {
            // Calcolo dell'ID incrementale come per gli annunci
            int id = dbMessaggi.size() + 1;
            Messaggio nuovoMsg = new Messaggio(mittente, destinatario, testo.trim());

            dbMessaggi.put(id, nuovoMsg);
            DatabaseCore.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Messaggio> getCronologia(String utente1, String utente2) {
        List<Messaggio> cronologiaFiltrata = new ArrayList<>();

        // Lettura sequenziale
        int totaleMessaggi = dbMessaggi.size();
        for (int i = 1; i <= totaleMessaggi; i++) {
            Messaggio m = dbMessaggi.get(i);
            if (m != null) {
                if ((m.getMittente().equals(utente1) && m.getDestinatario().equals(utente2)) ||
                        (m.getMittente().equals(utente2) && m.getDestinatario().equals(utente1))) {
                    cronologiaFiltrata.add(m);
                }
            }
        }
        return cronologiaFiltrata;
    }

    @Override
    public List<String> getConversazioniAttive(String utenteLoggato) {
        List<String> contattiAttivi = new ArrayList<>();

        // LIFO
        int totaleMessaggi = dbMessaggi.size();
        for (int i = totaleMessaggi; i >= 1; i--) {
            Messaggio m = dbMessaggi.get(i);
            if (m != null) {
                String interlocutore = null;
                if (m.getMittente().equals(utenteLoggato)) {
                    interlocutore = m.getDestinatario();
                } else if (m.getDestinatario().equals(utenteLoggato)) {
                    interlocutore = m.getMittente();
                }

                // Inserisce in cima alla lista l'interlocutore dell'ultimo messaggio
                if (interlocutore != null && !contattiAttivi.contains(interlocutore)) {
                    contattiAttivi.add(interlocutore);
                }
            }
        }
        return contattiAttivi;
    }
}