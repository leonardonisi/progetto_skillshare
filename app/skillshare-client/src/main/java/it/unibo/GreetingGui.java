package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GreetingGui {

    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    // Crea automaticamente il codice JavaScript
    // L'oggetto greetingService invia i dati al backend
    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    public void mostra() {
        // Pulisce tutto il contenuto del body
        RootPanel.get().clear();

        // Inizializzazione Widget
        HTML title = new HTML("<h1>Web Application Starter Project</h1>");
        final Button sendButton = new Button("Send");
        final TextBox nameField = new TextBox();
        final Label errorLabel = new Label();
        final Button ricettaButton = new Button("Vai a ricetta");

        nameField.setText("GWT User");
        sendButton.addStyleName("sendButton");

        // Creazione del Main Panel (VerticalPanel)
        // Server per impilare i widget verticalmente e centrarli
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10); // Opzionale: aggiunge un po' di spazio tra i widget

        // Impostiamo la larghezza al 100% per permettere l'allineamento interno
        mainPanel.setWidth("100%");

        // Allineamento orizzontale al centro per tutti i widget aggiunti dopo questa
        // riga
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        mainPanel.add(title);
        mainPanel.add(new HTML("<b>Please enter your name:</b>"));
        mainPanel.add(nameField);
        mainPanel.add(sendButton);
        mainPanel.add(errorLabel);
        mainPanel.add(ricettaButton);

        // Aggiunta al RootPanel
        // carico il mainPanel nel DOM, per farlo visualizzare, altrimenti è solo un
        // oggetto in memoria
        RootPanel.get().add(mainPanel);

        // Focus
        nameField.setFocus(true);
        nameField.selectAll();

        // --- Logica DialogBox ---
        // è come se fosse un popup, è nascosto finché non viene mostrato (per esempio
        // messaggi del server o errori)
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Remote Procedure Call");
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button("Close");
        closeButton.getElement().setId("closeButton");
        final Label textToServerLabel = new Label();
        final HTML serverResponseLabel = new HTML();

        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        // gestione del click sul bottone close del dialog box
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide(); // Nasconde il dialog box
                sendButton.setEnabled(true); // Riabilita il bottone send
                sendButton.setFocus(true);
            }
        });

        // --- Logica Handler ---
        // Gestione dei click e del tasto invio della tastiera
        class MyHandler implements ClickHandler, KeyUpHandler {
            // gestione del click
            public void onClick(ClickEvent event) {
                sendNameToServer();
            }

            // gestione del tasto invio della tastiera
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sendNameToServer();
                }
            }

            // inivia il testo al server
            private void sendNameToServer() {
                errorLabel.setText("");
                String textToServer = nameField.getText();
                if (!FieldVerifier.isValidName(textToServer)) {
                    errorLabel.setText("Please enter at least four characters");
                    return;
                }

                sendButton.setEnabled(false);
                textToServerLabel.setText(textToServer);
                serverResponseLabel.setText("");

                // invio del testo al server, e gestione della risposta con callback asincrono
                // per evitare di bloccare l'interfaccia utente
                greetingService.greetServer(textToServer, new AsyncCallback<GreetingResponse>() {

                    // se server è irraggiungibile o si verifica un errore, viene chiamato onFailure
                    public void onFailure(Throwable caught) {
                        dialogBox.setText("Remote Procedure Call - Failure");
                        serverResponseLabel.addStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(SERVER_ERROR);
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }

                    // se la chiamata al server ha successo, viene chiamato onSuccess
                    public void onSuccess(GreetingResponse result) {
                        dialogBox.setText("Remote Procedure Call");
                        serverResponseLabel.removeStyleName("serverResponseLabelError");
                        serverResponseLabel.setHTML(new SafeHtmlBuilder()
                                .appendEscaped(result.getGreeting())
                                .appendHtmlConstant("<br><br>I am running ")
                                .appendEscaped(result.getServerInfo())
                                .appendHtmlConstant(".<br><br>It looks like you are using:<br>")
                                .appendEscaped(result.getUserAgent())
                                .toSafeHtml());
                        dialogBox.center();
                        closeButton.setFocus(true);
                    }
                });
            }
        }

        MyHandler handler = new MyHandler();
        sendButton.addClickHandler(handler); // Associa il click del bottone send all'handler
        nameField.addKeyUpHandler(handler);

        // --- Logica Cambio Interfaccia ---
        // serve per cambiare pagina quando si clicca sul bottone "Vai a ricetta"
        ricettaButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Carica la nuova classe
                new RicettaGui().mostra();
            }
        });
    }
}