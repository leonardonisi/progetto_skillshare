package it.unibo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateAdGui {

    // private final CreateAdServiceAsync createAdService =
    // GWT.create(CreateAdService.class);

    public void mostra() {
        // Pulisce tutto il contenuto del body
        RootPanel.get().clear();

        // Inizializzazione Widget
        HTML title = new HTML("<h1 style='color: #87CEEB;'>PUBBLICA ANNUNCIO</h1>");
        HTML subtitle = new HTML("<h2>Inserisci i dettagli del tuo annuncio:</h2>");
        final TextBox titleField = new TextBox();
        final ListBox categoryList = new ListBox();
        final TextArea offertSkill = new TextArea();
        final TextArea disponibility = new TextArea();
        final TextArea searchedSkill = new TextArea();
        final Button btnPubblica = new Button("PUBBLICA");
        final Button btnAnnulla = new Button("ANNULLA");

        // Aggiunta delle categorie
        categoryList.addItem("Informatica");
        categoryList.addItem("Cucina");
        categoryList.addItem("Lingue");
        categoryList.addItem("Economia");
        categoryList.addItem("Altro");

        // assegnazione id per identificazione con Selenium
        title.getElement().setId("titolo-create-ad");
        titleField.getElement().setId("titolo-annuncio");
        categoryList.getElement().setId("categoria-annuncio");
        offertSkill.getElement().setId("offerta-skill");
        disponibility.getElement().setId("disponibilita");
        searchedSkill.getElement().setId("ricerca-skill");
        btnPubblica.getElement().setId("btn-pubblica");

        // Creazione del layout aggiunta a schermo
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        // Diminuzione spazio tra titolo e sottotitolo
        subtitle.getElement().getStyle().setProperty("marginTop", "-50px");

        mainPanel.add(title);
        mainPanel.add(subtitle);
        mainPanel.add(new Label("Titolo Annuncio:"));
        mainPanel.add(titleField);
        mainPanel.add(new Label("Categoria:"));
        mainPanel.add(categoryList);
        mainPanel.add(new Label("Descrizione Skill Offerta:"));
        mainPanel.add(offertSkill);
        mainPanel.add(new Label("Disponibilità:"));
        mainPanel.add(disponibility);
        mainPanel.add(new Label("Skill Ricercata:"));
        mainPanel.add(searchedSkill);
        mainPanel.add(btnPubblica);
        mainPanel.add(btnAnnulla);

        // Modifica widget stile per allineamento
        titleField.setWidth("200px");
        categoryList.setWidth("206px");
        btnPubblica.setWidth("200px");
        btnAnnulla.setWidth("80px");

        offertSkill.setCharacterWidth(50); // Larghezza del box
        offertSkill.setVisibleLines(6);
        disponibility.setCharacterWidth(50); // Larghezza del box
        disponibility.setVisibleLines(2);
        searchedSkill.setCharacterWidth(50); // Larghezza del box
        searchedSkill.setVisibleLines(6);

        RootPanel.get().add(mainPanel);
        titleField.setFocus(true);
        // --- Logica Handler ---
        class CreateAdHandler implements ClickHandler {

            @Override
            public void onClick(ClickEvent event) {
                pubblica();
            }

            private void pubblica() {
                String offro = offertSkill.getText().trim();
                String cerco = searchedSkill.getText().trim();
                String disponibilita = disponibility.getText().trim();

                // Validazione locale richiesta dai test di accettazione
                if (offro.isEmpty() || cerco.isEmpty() || disponibilita.isEmpty()) {
                    Window.alert("Devi specificare sia cosa offri sia cosa cerchi sia la disponibilità");
                    /*
                     * @Override
                     * public void onFailure(Throwable caught) {
                     * Window.alert("Errore rete ");
                     * }
                     * 
                     * @Override
                     * public void onSuccess(String result) {
                     */
                } else {
                    Window.alert("Annuncio pubblicato con successo");
                    RootPanel.get().clear();
                    new MainLayoutGui().mostra();
                }
            }
        }

        // Istanziazione dell'handler e aggancio ai widget coinvolti
        CreateAdHandler createAdHandler = new CreateAdHandler();
        btnPubblica.addClickHandler(createAdHandler);
        btnAnnulla.addClickHandler(event -> {
            RootPanel.get().clear();
            new MainLayoutGui().mostra();
        });
    }
}
