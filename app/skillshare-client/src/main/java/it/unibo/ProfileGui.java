package it.unibo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class ProfileGui {

    public void mostra() {
        RootPanel.get().clear();

        // Titolo
        HTML title = new HTML("<h1 style='color: #2E8B57;'>IL MIO PROFILO</h1>");
        title.getElement().setId("titolo-profilo");

        // Avatar 
        Image avatar = new Image("https://via.placeholder.com/150");
        avatar.getElement().setId("img-avatar");

        // Informazioni Utente
        Label usernameLabel = new Label("Username: UtenteProva");
        usernameLabel.getElement().setId("lbl-username");

        Label bioLabel = new Label("Biografia: Ciao, sono un nuovo utente di SkillShare e voglio imparare a programmare!");
        bioLabel.getElement().setId("lbl-bio");

        //Bottone Modifica
        final Button editButton = new Button("MODIFICA PROFILO");
        editButton.getElement().setId("btn-modifica");

        // Layout 
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(15);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        mainPanel.add(title);
        mainPanel.add(avatar);
        mainPanel.add(usernameLabel);
        mainPanel.add(bioLabel);
        mainPanel.add(editButton);

        // Aggiunta a schermo
        RootPanel.get().add(mainPanel);
    }
}