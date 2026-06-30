package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.Cookies;

public class LoginGui {

    private final LoginServiceAsync loginService = GWT.create(LoginService.class);

    public void mostra() {
        // Pulisce tutto il contenuto del body
        RootPanel.get().clear();

        // Inizializzazione Widget
        HTML title = new HTML("<h1 style='color: #007BFF;'>ACCESSO A SKILLSHARE</h1>");
        final TextBox usernameField = new TextBox();
        final PasswordTextBox passwordField = new PasswordTextBox();

        // Aggiunte per mostra password
        final Button showPasswordButton = new Button("👀");
        final Label passwordVisibleLabel = new Label();
        final Label vuoto = new Label("");

        final Button loginButton = new Button("LOGIN");
        final Button registerButton = new Button("REGISTER");

        usernameField.getElement().setAttribute("placeholder", "Username");
        passwordField.getElement().setAttribute("placeholder", "Password");
        passwordVisibleLabel.setVisible(false);

        // assegnazione id per identificazione con Selenium
        title.getElement().setId("titolo-login");
        usernameField.getElement().setId("input-username");
        passwordField.getElement().setId("input-password");
        loginButton.getElement().setId("btn-login");
        registerButton.getElement().setId("btn-register");

        // Grafica per mostra password
        HorizontalPanel passwordPanel = new HorizontalPanel();
        passwordPanel.setSpacing(5);
        passwordPanel.add(vuoto);
        passwordPanel.add(passwordField);
        passwordPanel.add(showPasswordButton);

        // Creazione del layout minimo e aggiunta a schermo
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(title);
        mainPanel.add(new HTML("<h2>Inserisci username e password:</h2>"));
        mainPanel.add(usernameField);
        mainPanel.add(passwordPanel);
        mainPanel.add(passwordVisibleLabel);
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);

        // Modifica widget stile
        usernameField.setWidth("200px");
        passwordField.setWidth("200px");
        loginButton.setWidth("200px");
        registerButton.setWidth("80px");
        showPasswordButton.setWidth("30px");
        vuoto.setWidth("30px");

        RootPanel.get().add(mainPanel);

        // Focus su username
        usernameField.setFocus(true);
        usernameField.selectAll();

        // --- Logica Handler ---
        class LoginHandler implements KeyUpHandler, ClickHandler {
            public void onClick(ClickEvent event) {
                login();
            }

            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    login();
                }
            }

            private void login() {
                String username = usernameField.getText();
                String password = passwordField.getText();
                loginService.authenticate(username, password, new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Errore rete ");
                    }

                    @Override
                    public void onSuccess(String result) {
                        if ("Username non valido".equals(result)) {
                            Window.alert("Username non valido");
                        } else if ("Username inesistente".equals(result)) {
                            Window.alert("Username inesistente");
                        } else if ("Password errata".equals(result)) {
                            Window.alert("Password errata");
                        } else {
                            Cookies.setCookie("username", username);
                            RootPanel.get().clear();

                            SessionManager.setUtenteLoggato(result);
                            new MainLayoutGui().mostra();
                        }
                    }
                });

            }

        }
        LoginHandler loginHandler = new LoginHandler();
        loginButton.addClickHandler(loginHandler);
        usernameField.addKeyUpHandler(loginHandler);
        passwordField.addKeyUpHandler(loginHandler);

        registerButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                new RegisterGui().mostra();
            }
        });

        showPasswordButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (passwordVisibleLabel.isVisible()) {
                    passwordVisibleLabel.setVisible(false);
                    passwordField.setVisible(true);
                } else {
                    passwordVisibleLabel.setText(passwordField.getText());
                    passwordVisibleLabel.setVisible(true);
                }
            }
        });
    }

}
