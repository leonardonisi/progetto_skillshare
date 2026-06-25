package it.unibo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileGui {

    public void mostra() {
        // Pulisce la schermata precedente
        RootPanel.get().clear();

        HTML title = new HTML("<h1 style='color: #2E8B57;'>IL MIO PROFILO</h1>");
        final Button editButton = new Button("MODIFICA PROFILO");

        // Assegnazione degli ID
        title.getElement().setId("titolo-profilo");
        editButton.getElement().setId("btn-modifica");

        //Layout
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(15);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        mainPanel.add(title);
        mainPanel.add(editButton);

        RootPanel.get().add(mainPanel);
        
    }
}