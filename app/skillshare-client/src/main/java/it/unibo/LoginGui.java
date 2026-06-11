package it.unibo;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginGui {

    public void mostra() {
        // Pulisce tutto il contenuto del body
        RootPanel.get().clear();

        // Inizializzazione Widget
        HTML title = new HTML("<h1 style='color: #87CEEB;'>ACCESSO A SKILLSHARE</h1>");
        final TextBox usernameField = new TextBox();
        final PasswordTextBox passwordField = new PasswordTextBox();
        final Button loginButton = new Button("Login");
        final Button registerButton = new Button("Register");

        usernameField.setText("username");
        passwordField.setText("password");

        // assegnazione id per identificazione con Selenium
        title.getElement().setId("titolo-login");
        usernameField.getElement().setId("input-username");
        passwordField.getElement().setId("input-password");
        loginButton.getElement().setId("btn-login");
        registerButton.getElement().setId("btn-register");

        // Creazione del layout minimo e aggiunta a schermo
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(title);
        mainPanel.add(new HTML("<h2>Inserisci username e password:</h2>"));
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);

        // Modifica widget stile
        // title.getElement().getStyle().setColor("blue");
        usernameField.setWidth("200px");
        passwordField.setWidth("200px");
        loginButton.setWidth("200px");
        registerButton.setWidth("100px");

        RootPanel.get().add(mainPanel);

        // Focus su username
        usernameField.setFocus(true);
        usernameField.selectAll();

        // --- Logica Handler ---
        loginButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                // new HomeGui().mostra();
                // Evento per test Selenium, da aggiungere classe della HomeGUI
                VerticalPanel homePanel = new VerticalPanel();
                HTML homeTitle = new HTML("<h1>HOME</h1>");
                homeTitle.getElement().setId("titolo-home");
                homePanel.add(homeTitle);
                RootPanel.get().add(homePanel);
            }

        });
        registerButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // new RegisterGui().mostra();
                // Evento per test Selenium, da aggiungere classe della RegisterGUI
                VerticalPanel registerPanel = new VerticalPanel();
                HTML registerTitle = new HTML("<h1>REGISTER</h1>");
                registerTitle.getElement().setId("titolo-register");
                registerPanel.add(registerTitle);
                RootPanel.get().add(registerPanel);
            }
        });
    }

}
