package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChatGui {

    // private final ChatServiceAsync chatService = GWT.create(ChatService.class);

    private final VerticalPanel contactsPanel = new VerticalPanel();
    private final VerticalPanel messagesArea = new VerticalPanel();
    private final TextBox messageInput = new TextBox();
    private final Button btnInvia = new Button("INVIA");

    private final HTML chatHeaderTitle = new HTML(
            "<h3 style='color: #777; font-style: italic;'>Seleziona una conversazione dalla lista per iniziare a scambiare skill</h3>");

    private String utenteLoggato;
    private String interlocutoreAttivo = null;

    public void mostra() {
        RootPanel.get().clear();

        // recupero utente loggato
        utenteLoggato = SessionManager.getUtenteLoggato();
        if (utenteLoggato == null || utenteLoggato.isEmpty()) {
            utenteLoggato = "utente_test";
        }

        HTML title = new HTML("<h1 style='color: #007BFF;'>I MIEI MESSAGGI</h1>");
        title.getElement().setId("titolo-chat");

        // Layout principale a due colonne
        HorizontalPanel mainLayout = new HorizontalPanel();
        mainLayout.setWidth("85%");
        mainLayout.setSpacing(15);

        // Lista contatti a sinistra
        VerticalPanel leftCol = new VerticalPanel();
        leftCol.setWidth("35%");
        leftCol.setSpacing(10);

        // Pulsante per tornare al Marketplace
        Button btnBackToMarket = new Button("⬅ Torna al Marketplace");
        btnBackToMarket.setWidth("100px");
        btnBackToMarket.getElement().setId("btn-torna-market");
        btnBackToMarket.addClickHandler(event -> {
            RootPanel.get().clear();
            new MainLayoutGui().mostra();
        });
        leftCol.add(btnBackToMarket);

        leftCol.add(new HTML("<h3>Conversazioni Attive</h3>"));

        // ScrollPanel per contenere i riquadri dei contatti
        ScrollPanel contactsScroll = new ScrollPanel(contactsPanel);
        contactsScroll.setHeight("400px");
        contactsScroll.getElement().getStyle().setProperty("border", "1px solid #ddd");
        contactsScroll.getElement().getStyle().setProperty("borderRadius", "5px");

        contactsPanel.setWidth("100%");
        contactsPanel.getElement().setId("lista-contatti");
        leftCol.add(contactsScroll);

        // Area chat
        VerticalPanel rightCol = new VerticalPanel();
        rightCol.setWidth("80%");
        rightCol.setSpacing(12);

        // Intestazione dinamica della chat attiva
        rightCol.add(chatHeaderTitle);

        // Contenitore per lo storico dei messaggi
        ScrollPanel messagesScroll = new ScrollPanel(messagesArea);
        messagesScroll.setHeight("350px");
        messagesScroll.getElement().getStyle().setProperty("border", "1px solid #eee");
        messagesScroll.getElement().getStyle().setProperty("borderRadius", "5px");
        messagesScroll.getElement().getStyle().setProperty("padding", "10px");

        messagesArea.setWidth("100%");
        messagesArea.getElement().setId("area-cronologia-messaggi");
        rightCol.add(messagesScroll);

        // Barra di input inferiore
        HorizontalPanel inputPanel = new HorizontalPanel();
        inputPanel.setWidth("100%");
        inputPanel.setSpacing(5);

        messageInput.setWidth("80%");
        messageInput.setHeight("35px");
        messageInput.getElement().setId("input-messaggio");
        messageInput.getElement().setAttribute("placeholder", "Scrivi un messaggio...");

        btnInvia.setWidth("100px");
        btnInvia.getElement().setId("btn-invia-messaggio");
        btnInvia.setEnabled(false);

        inputPanel.add(messageInput);
        inputPanel.add(btnInvia);
        rightCol.add(inputPanel);

        // Assemblaggio pagina
        mainLayout.add(leftCol);
        mainLayout.add(rightCol);

        RootPanel.get().add(title);
        RootPanel.get().add(mainLayout);

        caricaContatti();

        // Click sul pulsante INVIA
        btnInvia.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                inviaMessaggio();
            }
        });

        // MODIFICA: Invio automatico alla pressione del tasto INVIO della tastiera
        messageInput.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    inviaMessaggio();
                }
            }
        });
    }

    private void caricaContatti() {
        contactsPanel.clear();

        for (int i = 1; i <= 3; i++) {
            final String nomeInterlocutore = "UtenteScambio_" + i;

            VerticalPanel contactBox = new VerticalPanel();
            contactBox.setWidth("100%");
            contactBox.getElement().setId("contatto-" + nomeInterlocutore);

            contactBox.getElement().getStyle().setProperty("border", "1px solid #000000");
            contactBox.getElement().getStyle().setProperty("marginBottom", "15px");
            contactBox.getElement().getStyle().setProperty("padding", "25px 20px");
            contactBox.getElement().getStyle().setProperty("cursor", "pointer");
            contactBox.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

            Label nameLabel = new Label(nomeInterlocutore);
            nameLabel.getElement().getStyle().setProperty("fontSize", "18px");
            nameLabel.getElement().getStyle().setProperty("fontWeight", "bold");

            contactBox.add(nameLabel);

            // Handler per gestire il click sulla conversazione
            contactBox.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    apriConversazione(nomeInterlocutore);
                }
            }, ClickEvent.getType());

            contactsPanel.add(contactBox);
        }
    }

    private void apriConversazione(String interlocutore) {
        this.interlocutoreAttivo = interlocutore;
        messagesArea.clear();
        btnInvia.setEnabled(true);
        messageInput.setFocus(true);

        // Cambia l'header mostrando con chi stai parlando
        chatHeaderTitle.setHTML("<h3>Chat con: <b>" + interlocutore + "</b></h3>");

        // Messaggio ricevuto (a sinistra, colore Nero)
        Label msgRicevuto = new Label("Ciao! Ho visto il tuo annuncio su Skillshare.");
        msgRicevuto.getElement().getStyle().setProperty("color", "#000000");
        // MODIFICA: Ingrandito e spaziato
        msgRicevuto.getElement().getStyle().setProperty("fontSize", "16px");
        msgRicevuto.getElement().getStyle().setProperty("marginBottom", "12px");

        messagesArea.add(msgRicevuto);
        messagesArea.setCellHorizontalAlignment(msgRicevuto, VerticalPanel.ALIGN_LEFT);

        // Messaggio inviato (a destra, colore Azzurro)
        Label msgInviato = new Label("Ciao!");
        msgInviato.getElement().getStyle().setProperty("color", "#007BFF");
        msgInviato.getElement().getStyle().setProperty("fontSize", "16px");
        msgInviato.getElement().getStyle().setProperty("marginBottom", "12px");

        messagesArea.add(msgInviato);
        messagesArea.setCellHorizontalAlignment(msgInviato, VerticalPanel.ALIGN_RIGHT);
    }

    private void inviaMessaggio() {
        String testo = messageInput.getText().trim();
        if (testo.isEmpty() || interlocutoreAttivo == null) {
            return;
        }

        // Crea il nuovo messaggio come semplice Label azzurra
        Label nuovoMsg = new Label(testo);
        nuovoMsg.getElement().getStyle().setProperty("color", "#007BFF");
        nuovoMsg.getElement().getStyle().setProperty("fontSize", "16px");
        nuovoMsg.getElement().getStyle().setProperty("marginBottom", "12px");
        messagesArea.add(nuovoMsg);
        messagesArea.setCellHorizontalAlignment(nuovoMsg, VerticalPanel.ALIGN_RIGHT);

        messageInput.setText("");
    }
}