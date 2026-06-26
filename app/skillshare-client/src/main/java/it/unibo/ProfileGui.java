package it.unibo;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import java.util.ArrayList;
import java.util.List;

public class ProfileGui {

    // Lista per tenere traccia delle categorie scelte ed evitare i duplicati
    private List<String> categorieSelezionate = new ArrayList<>();

    public void mostra() {
        RootPanel.get().clear();

        // Sfondo generale della pagina
        VerticalPanel pageBackground = new VerticalPanel();
        pageBackground.setWidth("100%");
        pageBackground.setHeight("100vh");
        pageBackground.getElement().getStyle().setBackgroundColor("#f3f2ef"); 
        pageBackground.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        Button btnHome = new Button("← Torna alla Home");
        btnHome.getElement().setId("btn-torna-home");
        btnHome.getElement().getStyle().setProperty("margin", "5px 20px"); 
        btnHome.getElement().getStyle().setProperty("padding", "6px 12px");
        btnHome.getElement().getStyle().setProperty("cursor", "pointer");
    
        btnHome.addClickHandler(event -> {new MainLayoutGui().mostra();});
        pageBackground.add(btnHome);
        pageBackground.setCellHorizontalAlignment(btnHome, HasHorizontalAlignment.ALIGN_LEFT);

        //pannello verticale 
        VerticalPanel cardPanel = new VerticalPanel();
        cardPanel.setWidth("600px");
        cardPanel.setSpacing(20);
        cardPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        cardPanel.getElement().getStyle().setBackgroundColor("white");
        cardPanel.getElement().getStyle().setProperty("borderRadius", "10px");
        cardPanel.getElement().getStyle().setProperty("boxShadow", "0 4px 8px rgba(0,0,0,0.1)");
        cardPanel.getElement().getStyle().setProperty("marginTop", "40px");

        // Titolo
        HTML title = new HTML("<h2 style='color: #8b2e71; margin-bottom: 0;'>IL MIO PROFILO</h2>");
        title.getElement().setId("titolo-profilo");

        // Foto/avatar arrotondata
        Label avatar = new Label("👤");
        avatar.getElement().setId("img-avatar"); 

        avatar.getElement().getStyle().setProperty("width", "120px");
        avatar.getElement().getStyle().setProperty("height", "120px");
        avatar.getElement().getStyle().setProperty("backgroundColor", "#ffffff"); 
        avatar.getElement().getStyle().setProperty("border", "2px solid #dbdbdb"); 
        avatar.getElement().getStyle().setProperty("borderRadius", "50%"); 

        avatar.getElement().getStyle().setProperty("fontSize", "80px"); 
        avatar.getElement().getStyle().setProperty("lineHeight", "120px"); 
        avatar.getElement().getStyle().setProperty("textAlign", "center"); 
        avatar.getElement().getStyle().setProperty("margin", "0 auto");

        // Username 
        Label usernameLabel = new Label("Username: Filker67");
        usernameLabel.getElement().setId("lbl-username");
        usernameLabel.getElement().getStyle().setProperty("fontWeight", "bold");
        usernameLabel.getElement().getStyle().setProperty("fontSize", "18px");

        HorizontalPanel bodyPanel = new HorizontalPanel();
        bodyPanel.setWidth("100%");
        bodyPanel.setSpacing(15);
        bodyPanel.getElement().getStyle().setProperty("marginTop", "20px");

        // Bio e Locazione
        VerticalPanel leftPanel = new VerticalPanel();
        leftPanel.setWidth("280px");
        leftPanel.setSpacing(10);

        Label bioLabel = new Label("Biografia: Ciao, sono un nuovo utente di SkillShare e voglio imparare a programmare in Python e cucinare il pollo!");
        bioLabel.getElement().setId("lbl-bio");
        bioLabel.getElement().getStyle().setProperty("fontStyle", "italic");
        bioLabel.getElement().getStyle().setColor("#555");

        Label locazioneLabel = new Label("Locazione: Cesena, FC (Italia)");
        locazioneLabel.getElement().setId("lbl-locazione");
        locazioneLabel.getElement().getStyle().setColor("#777");

        leftPanel.add(bioLabel);
        leftPanel.add(locazioneLabel);

        // Categorie a lato
        VerticalPanel rightPanel = new VerticalPanel();
        rightPanel.setWidth("280px");
        rightPanel.setSpacing(5);

        Label categorieTitolo = new Label("I miei interessi (Max 10):");
        categorieTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        
        final ListBox categorieDropdown = new ListBox();
        categorieDropdown.getElement().setId("select-categorie");
        categorieDropdown.setWidth("100%");
        categorieDropdown.addItem("Seleziona una categoria...");

        String[] tutteLeCategorie = {
            "Sviluppo Software", "Lingue Straniere", "Cucina", "Musica", 
            "Sport", "Fotografia", "Design", "Marketing", "Fai da te", 
            "Matematica", "Yoga", "Scrittura"
        };
        for (String categoria : tutteLeCategorie) {
            categorieDropdown.addItem(categoria);
        }

        // Pannello tag verticale 
        final VerticalPanel tagPanel = new VerticalPanel();
        tagPanel.setSpacing(5);
        tagPanel.getElement().setId("panel-tag-categorie");

        categorieDropdown.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                int selectedIndex = categorieDropdown.getSelectedIndex();
                
                if (selectedIndex > 0) {
                    String scelta = categorieDropdown.getItemText(selectedIndex);
                    
                    // Controllo categorie duplicate e limite massimo
                    if (categorieSelezionate.contains(scelta)) {
                        Window.alert("Hai già selezionato questa categoria!");
                    } else if (categorieSelezionate.size() >= 10) {
                        Window.alert("Hai raggiunto il limite massimo di 10 categorie!");
                    } else {
                        // Aggiunge alla lista di controllo
                        categorieSelezionate.add(scelta);
                        
                        // Crea l'etichetta visiva per la categoria
                        Label tag = new Label("• " + scelta);
                        tag.getElement().getStyle().setColor("#2E8B57");
                        tag.getElement().getStyle().setProperty("fontWeight", "500");
                        tag.getElement().getStyle().setProperty("backgroundColor", "#e8f5e9");
                        tag.getElement().getStyle().setProperty("padding", "4px 8px");
                        tag.getElement().getStyle().setProperty("borderRadius", "4px");
                        tag.setWidth("100%");
                        
                        tagPanel.add(tag);
                    }
                    categorieDropdown.setSelectedIndex(0);
                }
            }
        });

        rightPanel.add(categorieTitolo);
        rightPanel.add(categorieDropdown);
        rightPanel.add(tagPanel);

        // Assembla il corpo centrale
        bodyPanel.add(leftPanel);
        bodyPanel.add(rightPanel);

        // Bottone Modifica 
        final Button editButton = new Button("MODIFICA PROFILO");
        editButton.getElement().setId("btn-modifica");
        editButton.getElement().getStyle().setProperty("marginTop", "20px");
        editButton.getElement().getStyle().setProperty("padding", "10px 20px");

       
        cardPanel.add(title);
        cardPanel.add(avatar);
        cardPanel.add(usernameLabel);
        cardPanel.add(bodyPanel);
        cardPanel.add(editButton);

        
        pageBackground.add(cardPanel);
        RootPanel.get().add(pageBackground);
        
    }
}