package it.unibo;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.dom.client.Document;
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

public class ChatGui extends Composite {

    private final ChatServiceAsync chatService = GWT.create(ChatService.class);

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
        RootPanel.get().add(this);
    }

    public ChatGui() {

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
        VerticalPanel vistaCompleta = new VerticalPanel();
        vistaCompleta.setWidth("100%");

        mainLayout.add(leftCol);
        mainLayout.add(rightCol);

        vistaCompleta.add(title);
        vistaCompleta.add(mainLayout);

        initWidget(vistaCompleta);

        caricaContatti();

        // Click sul pulsante INVIA
        btnInvia.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                inviaMessaggio();
            }
        });

        // Invio automatico alla pressione del tasto INVIO della tastiera
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

        chatService.getConversazioniAttive(utenteLoggato, new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                contactsPanel.add(new Label("Errore nel caricamento delle conversazioni."));
            }

            @Override
            public void onSuccess(List<String> contatti) {
                if (contatti == null || contatti.isEmpty()) {
                    contactsPanel.add(new Label("Nessuna conversazione attiva."));
                    return;
                }

                for (final String nomeInterlocutore : contatti) {
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

                    // Gestione del click sulla conversazione reale
                    contactBox.addDomHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            apriConversazione(nomeInterlocutore);
                        }
                    }, ClickEvent.getType());

                    contactsPanel.add(contactBox);
                }
            }
        });
    }

    public void apriConversazione(String interlocutore) {
        this.interlocutoreAttivo = interlocutore;
        messagesArea.clear();
        btnInvia.setEnabled(true);
        messageInput.setFocus(true);

        chatHeaderTitle.setHTML("<h3>Chat con: <b>" + interlocutore + "</b></h3>");

        chatService.getCronologia(utenteLoggato, interlocutore, new AsyncCallback<List<Messaggio>>() {
            @Override
            public void onFailure(Throwable caught) {
                messagesArea.add(new Label("Errore nel caricamento della cronologia."));
            }

            @Override
            public void onSuccess(List<Messaggio> cronologia) {
                // Se non c'è una cronologia creiamo dinamicamente il contatto
                if (cronologia == null || cronologia.isEmpty()) {
                    String idContatto = "contatto-" + interlocutore;
                    if (Document.get().getElementById(idContatto) == null) {
                        VerticalPanel contactBox = new VerticalPanel();
                        contactBox.setWidth("100%");
                        contactBox.getElement().setId(idContatto);

                        contactBox.getElement().getStyle().setProperty("border", "1px solid #000000");
                        contactBox.getElement().getStyle().setProperty("marginBottom", "15px");
                        contactBox.getElement().getStyle().setProperty("padding", "25px 20px");
                        contactBox.getElement().getStyle().setProperty("cursor", "pointer");
                        contactBox.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

                        Label nameLabel = new Label(interlocutore);
                        nameLabel.getElement().getStyle().setProperty("fontSize", "18px");
                        nameLabel.getElement().getStyle().setProperty("fontWeight", "bold");
                        contactBox.add(nameLabel);

                        contactBox.addDomHandler(event -> apriConversazione(interlocutore), ClickEvent.getType());
                        contactsPanel.insert(contactBox, 0);
                    }

                    Label infoLabel = new Label(
                            "Nessun messaggio precedente. Scrivi qualcosa per iniziare lo scambio!");
                    infoLabel.getElement().getStyle().setProperty("fontStyle", "italic");
                    infoLabel.getElement().getStyle().setProperty("color", "#999");
                    messagesArea.add(infoLabel);
                    return;
                }

                for (Messaggio m : cronologia) {
                    Label lblMsg = new Label(m.getTesto());
                    lblMsg.getElement().getStyle().setProperty("fontSize", "16px");
                    lblMsg.getElement().getStyle().setProperty("marginBottom", "12px");

                    // Se il mittente sono io -> Messaggio inviato (Azzurro a Destra)
                    if (m.getMittente().equals(utenteLoggato)) {
                        lblMsg.getElement().getStyle().setProperty("color", "#007BFF");
                        messagesArea.add(lblMsg);
                        messagesArea.setCellHorizontalAlignment(lblMsg, VerticalPanel.ALIGN_RIGHT);
                    } else {
                        // Se il mittente è l'altro -> Messaggio ricevuto (Nero a Sinistra)
                        lblMsg.getElement().getStyle().setProperty("color", "#000000");
                        messagesArea.add(lblMsg);
                        messagesArea.setCellHorizontalAlignment(lblMsg, VerticalPanel.ALIGN_LEFT);
                    }
                }
            }
        });
    }

    private void inviaMessaggio() {
        final String testo = messageInput.getText().trim();
        if (testo.isEmpty() || interlocutoreAttivo == null) {
            return;
        }
        messageInput.setText("");

        chatService.inviaMessaggio(utenteLoggato, interlocutoreAttivo, testo, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Impossibile inviare il messaggio: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                apriConversazione(interlocutoreAttivo);
                caricaContatti();
            }
        });
    }
}