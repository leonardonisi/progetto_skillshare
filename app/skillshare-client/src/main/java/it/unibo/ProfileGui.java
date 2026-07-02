package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.FileUpload;
import java.util.ArrayList;
import java.util.List;

public class ProfileGui {

    private final ProfileServiceAsync profileService = GWT.create(ProfileService.class);

    // Lista per tenere traccia delle categorie scelte ed evitare i duplicati
    private List<String> categorieSelezionate = new ArrayList<>();

    private String fotoBase = ""; 
    private Image photoImg;
    private Button editButton;

    public void mostra() {
        RootPanel.get().clear();

        // Bottone salva modifiche
        final Button editButton = new Button("SALVA MODIFICHE");
        editButton.getElement().setId("btn-modifica");
        editButton.getElement().getStyle().setProperty("marginTop", "20px");
        editButton.getElement().getStyle().setProperty("padding", "10px 20px");
        editButton.setVisible(false);//finchè non ci sono modifiche non è visibile

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

        // pannello verticale
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

        // Foto
        VerticalPanel photoPanel = new VerticalPanel();
        photoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        photoPanel.setSpacing(10);

        // Immagine Tonda
        photoImg = new Image("images/utente.jpg");
        photoImg.getElement().setId("img-photo");
        photoImg.setPixelSize(120, 120);
        photoImg.getElement().getStyle().setProperty("borderRadius", "50%");
        photoImg.getElement().getStyle().setProperty("objectFit", "cover");
        photoImg.getElement().getStyle().setProperty("border", "2px solid #dbdbdb");

        // Bottone standard "Scegli File"
        FileUpload uploadFoto = new FileUpload();
        uploadFoto.getElement().setAttribute("accept", "image/*");
        uploadFoto.setVisible(false);
        uploadFoto.addChangeHandler(event -> {
            editButton.setVisible(true);
            leggiImmagineBase(uploadFoto.getElement(), this);
        });

        Button btnScegliFoto = new Button("Cambia Foto Profilo");
        btnScegliFoto.getElement().getStyle().setProperty("padding", "5px 10px");
        btnScegliFoto.getElement().getStyle().setProperty("cursor", "pointer");

        btnScegliFoto.addClickHandler(event -> {
            uploadFoto.getElement().<com.google.gwt.dom.client.InputElement>cast().click();
        });

        photoPanel.add(photoImg);
        photoPanel.add(uploadFoto);
        photoPanel.add(btnScegliFoto);

        // Username
        Label usernameTitle = new Label("Nome Utente:");
        usernameTitle.getElement().getStyle().setProperty("fontWeight", "bold");

        final TextBox usernameBox = new TextBox();
        usernameBox.getElement().setId("txt-username");
        
        final String utenteLoggato = SessionManager.getUtenteLoggato();
        usernameBox.setText(utenteLoggato);
        usernameBox.setReadOnly(true); // Evita che l'utente si cambi il nome da solo
        
        usernameBox.setWidth("200px");
        usernameBox.getElement().getStyle().setProperty("textAlign", "center");

        usernameBox.addKeyUpHandler(event -> editButton.setVisible(true));

        HorizontalPanel bodyPanel = new HorizontalPanel();
        bodyPanel.setWidth("100%");
        bodyPanel.setSpacing(15);
        bodyPanel.getElement().getStyle().setProperty("marginTop", "0px");

        // Bio e Locazione
        VerticalPanel leftPanel = new VerticalPanel();
        leftPanel.setWidth("280px");
        leftPanel.setSpacing(10);

        Label bioTitle = new Label("Biografia:");
        bioTitle.getElement().getStyle().setProperty("fontWeight", "bold");
        
        final TextArea bioArea = new TextArea();
        bioArea.getElement().setId("txt-bio");
        bioArea.setWidth("100%");
        bioArea.setVisibleLines(5);

        bioArea.addKeyUpHandler(event -> editButton.setVisible(true));

        Label locazioneTitle = new Label("Località:");
        locazioneTitle.getElement().getStyle().setProperty("fontWeight", "bold");
        
        final TextBox locazioneBox = new TextBox();
        locazioneBox.getElement().setId("txt-locazione");
        locazioneBox.setWidth("100%");

        locazioneBox.addKeyUpHandler(event -> editButton.setVisible(true));

        leftPanel.add(bioTitle);
        leftPanel.add(bioArea);
        leftPanel.add(locazioneTitle);
        leftPanel.add(locazioneBox);

        // Chiamata al server per caricare i dati reali
        profileService.getProfile(utenteLoggato, new AsyncCallback<UserProfile>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Errore nel caricamento dei dati del profilo: " + caught.getMessage());
            }

            @Override
            public void onSuccess(UserProfile profiloSalvato) {
                if (profiloSalvato != null) {
                    bioArea.setText(profiloSalvato.getBio() != null ? profiloSalvato.getBio() : "");
                    locazioneBox.setText(profiloSalvato.getLocation() != null ? profiloSalvato.getLocation() : "");
                    
                    if (profiloSalvato.getPhoto() != null && !profiloSalvato.getPhoto().isEmpty()) {
                        fotoBase = profiloSalvato.getPhoto();
                        photoImg.setUrl(fotoBase);
                    }

                    // Se vuoi ricaricare anche le categorie precedentemente salvate
                    if (profiloSalvato.getCategories() != null) {
                        categorieSelezionate.addAll(profiloSalvato.getCategories());
                    }
                }
            }
        });

        // Categorie a lato
        VerticalPanel rightPanel = new VerticalPanel();
        rightPanel.setWidth("280px");
        rightPanel.setSpacing(5);

        Label categorieTitolo = new Label("I miei interessi (Max 10):");
        categorieTitolo.getElement().getStyle().setProperty("fontWeight", "bold");

        final ListBox categorieDropdown = new ListBox();
        categorieDropdown.getElement().setId("select-categorie");
        categorieDropdown.setWidth("100%");

        // aggiunta delle categorie
        profileService.getCategorie(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                categorieDropdown.clear();
                categorieDropdown.addItem("Errore caricamento");
                Window.alert("Impossibile caricare le categorie: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                categorieDropdown.clear();
                categorieDropdown.addItem("Scegli categoria");

                for (String categoria : result) {
                    categorieDropdown.addItem(categoria);
                }
            }
        });

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

                        editButton.setVisible(true);

                        // Creiamo un pannello orizzontale per tenere insieme il testo e la 'X'
                        final HorizontalPanel tagContainer = new HorizontalPanel();
                        tagContainer.setWidth("100%");
                        tagContainer.getElement().getStyle().setProperty("backgroundColor", "#e8f5e9");
                        tagContainer.getElement().getStyle().setProperty("borderRadius", "4px");
                        tagContainer.getElement().getStyle().setProperty("marginBottom", "4px");

                        // Crea l'etichetta visiva per la categoria
                        Label tagTesto = new Label("• " + scelta);
                        tagTesto.getElement().getStyle().setColor("#2E8B57");
                        tagTesto.getElement().getStyle().setProperty("fontWeight", "500");
                        tagTesto.getElement().getStyle().setProperty("padding", "4px 8px");
                        tagTesto.setWidth("100%");

                        // Bottone di rimozione (la 'X')
                        Button removeBtn = new Button("X");
                        removeBtn.getElement().getStyle().setColor("#d32f2f");
                        removeBtn.getElement().getStyle().setProperty("backgroundColor", "transparent");
                        removeBtn.getElement().getStyle().setProperty("border", "none");
                        removeBtn.getElement().getStyle().setProperty("cursor", "pointer");
                        removeBtn.getElement().getStyle().setProperty("fontWeight", "bold");

                        // Rimozione
                        removeBtn.addClickHandler(e -> {categorieSelezionate.remove(scelta); 
                            tagPanel.remove(tagContainer);
                            editButton.setVisible(true);
                        });

                        // Assembliamo il tag visivo
                        tagContainer.add(tagTesto);
                        tagContainer.add(removeBtn);
                        tagContainer.setCellHorizontalAlignment(removeBtn, HasHorizontalAlignment.ALIGN_RIGHT);

                        tagPanel.add(tagContainer);
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

        editButton.addClickHandler(event -> {
            UserProfile profiloDaSalvare = new UserProfile(
                usernameBox.getText(),       // Legge l'username modificato
                bioArea.getText(),           // Legge la nuova biografia
                locazioneBox.getText(),      // Legge la nuova località
                fotoBase,                          // Foto 
                categorieSelezionate         // Lista aggiornata dei tag scelti
            );

            // Salvataggio sul database tramite il server
            profileService.saveProfile(profiloDaSalvare, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Errore nel salvataggio: " + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    Window.alert("Categorie salvate con successo nel database!");

                    editButton.setVisible(false);
                }
            });
        });

        cardPanel.add(title);
        cardPanel.add(photoPanel);
        cardPanel.add(usernameTitle);
        cardPanel.add(usernameBox);
        cardPanel.add(bodyPanel);
        cardPanel.add(editButton);

        pageBackground.add(cardPanel);
        RootPanel.get().add(pageBackground);

    }

    // Legge il file e lo trasforma in testo Base64
    private native void leggiImmagineBase(com.google.gwt.dom.client.Element input, ProfileGui gui) /*-{
        var file = input.files[0];
        if (!file) return;
        var reader = new FileReader();
        reader.onload = function(e) {
            gui.@it.unibo.ProfileGui::aggiornaFotoCaricata(Ljava/lang/String;)(e.target.result);
        };
        reader.readAsDataURL(file);
    }-*/;

    public void aggiornaFotoCaricata(String base) {
        this.fotoBase = base;          // Salva la stringa per il database
        this.photoImg.setUrl(base);     // Aggiorna l'immagine sullo schermo istantaneamente
        this.editButton.setVisible(true);  // Mostra il bottone "SALVA"
    }
}