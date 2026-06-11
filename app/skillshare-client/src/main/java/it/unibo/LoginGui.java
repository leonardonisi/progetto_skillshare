package it.unibo;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginGui {

    public void mostra() {
        // Pulisce tutto il contenuto del body
        RootPanel.get().clear();

        // Inizializzazione Widget
        HTML title = new HTML("<h1>ACCESSO A SKILLSHARE</h1>");
        final TextBox usernameField = new TextBox();
        final TextBox passwordField = new TextBox();

        usernameField.setText("username");
        passwordField.setText("password");

        // assegnazione id per identificazione con Selenium
        title.getElement().setId("titolo-login");
        usernameField.getElement().setId("input-username");
        passwordField.getElement().setId("input-password");

        // Creazione del layout minimo e aggiunta a schermo
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(title);
        mainPanel.add(new HTML("<b>Inserisci username e password:</b>"));
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);

        RootPanel.get().add(mainPanel);

        // Focus su username
        usernameField.setFocus(true);
        usernameField.selectAll();

    }

}
