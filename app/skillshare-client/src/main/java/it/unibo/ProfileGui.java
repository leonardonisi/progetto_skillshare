package it.unibo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfileGui {

    private final ProfileServiceAsync profileService = GWT.create(ProfileService.class);
    private final SkillServiceAsync skillService = GWT.create(SkillService.class);

    // Lista per tenere traccia delle categorie scelte ed evitare i duplicati
    private List<String> categorieSelezionate = new ArrayList<>();

    private String fotoBase = ""; 
    private Image photoImg;
    private Button editButton;
    private Utente utenteAttuale;

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

        final Label usernameLabel = new Label();
        usernameLabel.getElement().setId("txt-username");
        
        final String utenteLoggato = SessionManager.getUtenteLoggato();
        usernameLabel.setText(utenteLoggato);
        
        usernameLabel.setWidth("200px");
        usernameLabel.getElement().getStyle().setProperty("textAlign", "center");
        usernameLabel.getElement().getStyle().setProperty("padding", "5px");

        // container per il raiting medio
        HorizontalPanel ratingPanel = new HorizontalPanel();
        ratingPanel.setSpacing(5);
        ratingPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        Label lblRating = new Label("Caricamento rating..."); 
        lblRating.getElement().setId("lbl-rating-medio");
        lblRating.getElement().getStyle().setProperty("fontSize", "18px");
        lblRating.getElement().getStyle().setProperty("fontWeight", "bold");
        ratingPanel.add(lblRating);

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

        // Bottone salva modifiche
        editButton = new Button("SALVA MODIFICHE");
        editButton.getElement().setId("btn-modifica");
        editButton.getElement().getStyle().setProperty("marginTop", "20px");
        editButton.getElement().getStyle().setProperty("padding", "10px 20px");
        editButton.setVisible(false);//finchè non ci sono modifiche non è visibile

        // Chiamata al server per caricare i dati reali
        profileService.getUtente(utenteLoggato, new AsyncCallback<Utente>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Errore nel caricamento dei dati del profilo: " + caught.getMessage());
            }

            @Override
            public void onSuccess(Utente profiloSalvato) {
                if (profiloSalvato != null) {
                    utenteAttuale = profiloSalvato;

                    bioArea.setText(profiloSalvato.getBio() != null ? profiloSalvato.getBio() : "");
                    locazioneBox.setText(profiloSalvato.getLocazione() != null ? profiloSalvato.getLocazione() : "");
                    
                    if (profiloSalvato.getFotoProfiloBase64() != null && !profiloSalvato.getFotoProfiloBase64().isEmpty()) {
                        fotoBase = profiloSalvato.getFotoProfiloBase64();
                        photoImg.setUrl(fotoBase);
                    }

                    // Se vuoi ricaricare anche le categorie precedentemente salvate
                    if (profiloSalvato.getCompetenzePreferite() != null) {
                        categorieSelezionate.addAll(profiloSalvato.getCompetenzePreferite());
                    }
                    VerticalPanel panelBadgeContenitore = new VerticalPanel();
                    panelBadgeContenitore.setWidth("100%");
                    panelBadgeContenitore.setSpacing(10);        
                    panelBadgeContenitore.getElement().getStyle().setProperty("marginTop", "20px");
                    panelBadgeContenitore.getElement().getStyle().setProperty("padding", "15px");
                    panelBadgeContenitore.getElement().getStyle().setProperty("border", "1px dashed #007BFF");
                    panelBadgeContenitore.getElement().getStyle().setProperty("backgroundColor", "#f8f9fa");

                    Label lblSezioneBadge = new Label("MEDAGLIE E TRAGUARDI GUADAGNATI:");
                    lblSezioneBadge.getElement().getStyle().setProperty("fontWeight", "bold");
                    lblSezioneBadge.getElement().getStyle().setProperty("color", "#007BFF");
                    panelBadgeContenitore.add(lblSezioneBadge);

                    List<String> distintiviSbloccati = profiloSalvato.getBadgeOttenuti();

                    if (distintiviSbloccati == null || distintiviSbloccati.isEmpty()) {
                        Label lblNessunBadge = new Label("Nessun badge sbloccato finora. Completa 10 azioni per ricevere la tua prima medaglia!");
                        lblNessunBadge.getElement().getStyle().setProperty("fontStyle", "italic");
                        panelBadgeContenitore.add(lblNessunBadge);
                    } else {
                        FlowPanel grigliaMedaglie = new FlowPanel();
                        grigliaMedaglie.setWidth("100%");

                        for (String nomeMedaglia : distintiviSbloccati) {
                            HorizontalPanel boxSingoloBadge = new HorizontalPanel();
                            boxSingoloBadge.setSpacing(5);
                            boxSingoloBadge.getElement().getStyle().setProperty("display", "inline-flex");
                            boxSingoloBadge.getElement().getStyle().setProperty("margin", "5px 10px");
                            boxSingoloBadge.getElement().getStyle().setProperty("padding", "5px 10px");
                            boxSingoloBadge.getElement().getStyle().setProperty("backgroundColor", "#fff");
                            boxSingoloBadge.getElement().getStyle().setProperty("border", "1px solid #28a745");
                            boxSingoloBadge.getElement().getStyle().setProperty("borderRadius", "4px");

                            Label emojiMedaglia = new Label("🏅");
                            Label testoMedaglia = new Label(nomeMedaglia);
                            testoMedaglia.getElement().getStyle().setProperty("fontWeight", "bold");

                            boxSingoloBadge.add(emojiMedaglia);
                            boxSingoloBadge.add(testoMedaglia);
                            grigliaMedaglie.add(boxSingoloBadge);
                        }
                        panelBadgeContenitore.add(grigliaMedaglie);
                    }
                    cardPanel.add(panelBadgeContenitore);
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
           if (utenteAttuale != null) {
                utenteAttuale.setBio(bioArea.getText());
                utenteAttuale.setLocazione(locazioneBox.getText());
                utenteAttuale.setFotoProfiloBase64(fotoBase);
                
                utenteAttuale.setCompetenzePreferite(new ArrayList<>(categorieSelezionate));
           }

            // Salvataggio sul database tramite il server
            profileService.saveUtente(utenteAttuale, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Errore nel salvataggio: " + caught.getMessage());
                }

                @Override
                public void onSuccess(Void result) {
                    Window.alert("Modifiche salvate con successo");

                    editButton.setVisible(false);
                }
            });
        });

        cardPanel.add(title);
        cardPanel.add(photoPanel);
        cardPanel.add(usernameTitle);
        cardPanel.add(usernameLabel);
        cardPanel.add(ratingPanel);
        cardPanel.add(bodyPanel);
        cardPanel.add(editButton);

        // container per lo storico delle recensioni
        VerticalPanel storicoContainer = new VerticalPanel();
        storicoContainer.getElement().setId("container-storico-recensioni");
        storicoContainer.setWidth("600px");
        storicoContainer.setSpacing(10);
        storicoContainer.getElement().getStyle().setProperty("marginTop", "20px");
        storicoContainer.getElement().getStyle().setProperty("marginBottom", "40px");
        
        Label storicoTitle = new Label("Storico Recensioni");
        storicoTitle.getElement().getStyle().setProperty("fontWeight", "bold");
        storicoTitle.getElement().getStyle().setProperty("fontSize", "20px");
        storicoTitle.getElement().getStyle().setProperty("color", "#8b2e71");
        storicoContainer.add(storicoTitle);

        pageBackground.add(cardPanel);
        pageBackground.add(storicoContainer);
        RootPanel.get().add(pageBackground);

        // chiamata RCP per ottenere le recensioni dell'utente
        caricaEmostraRecensioni(utenteLoggato, lblRating, storicoContainer);


    }

    // metodo per caricare e mostrare le recensioni dell'utente
    private void caricaEmostraRecensioni(String username, Label lblRating, VerticalPanel storicoContainer) {
        skillService.getValutazioniUtente(username, new AsyncCallback<List<Valutazione>>() {
            @Override
            public void onFailure(Throwable caught) {
                lblRating.setText("Errore caricamento rating");
            }

            @Override
            public void onSuccess(List<Valutazione> recensioni) {
                if (recensioni == null || recensioni.isEmpty()) {
                    lblRating.setText("Nessuna recensione");
                    Label noRev = new Label("L'utente non ha ancora ricevuto recensioni.");
                    noRev.getElement().getStyle().setProperty("color", "#666");
                    storicoContainer.add(noRev);
                    return;
                }

                // Calcolo Rating Medio
                double somma = 0;
                for (Valutazione v : recensioni) {
                    somma += v.getVoto();
                }
                double media = somma / recensioni.size();
                
                String mediaFormat = String.valueOf(Math.round(media * 10.0) / 10.0);
                
                StringBuilder stelle = new StringBuilder();
                int stellePiene = (int) Math.round(media);
                for (int i = 0; i < 5; i++) {
                    if (i < stellePiene) stelle.append("★");
                    else stelle.append("☆");
                }
                
                lblRating.setText(mediaFormat + " " + stelle.toString());
                lblRating.getElement().getStyle().setProperty("color", "#FFD700"); 

                // Rendering Storico Recensioni
                for (int i = recensioni.size() - 1; i >= 0; i--) {
                    Valutazione v = recensioni.get(i);
                    
                    VerticalPanel cardRecensione = new VerticalPanel();
                    cardRecensione.setWidth("100%");
                    cardRecensione.getElement().setId("item-recensione-" + i);
                    cardRecensione.getElement().getStyle().setBackgroundColor("white");
                    cardRecensione.getElement().getStyle().setProperty("padding", "15px");
                    cardRecensione.getElement().getStyle().setProperty("borderRadius", "8px");
                    cardRecensione.getElement().getStyle().setProperty("boxShadow", "0 2px 4px rgba(0,0,0,0.05)");

                    HorizontalPanel headerRec = new HorizontalPanel();
                    headerRec.setWidth("100%");
                    
                    Label autore = new Label("Da: " + v.getAutore());
                    autore.getElement().getStyle().setProperty("fontWeight", "bold");
                    
                    StringBuilder votoStella = new StringBuilder();
                    for(int s=0; s<5; s++) { votoStella.append(s < v.getVoto() ? "★" : "☆"); }
                    Label lblVoto = new Label(votoStella.toString());
                    lblVoto.getElement().getStyle().setProperty("color", "#FFD700");

                    headerRec.add(autore);
                    headerRec.add(lblVoto);
                    headerRec.setCellHorizontalAlignment(lblVoto, HasHorizontalAlignment.ALIGN_RIGHT);

                    Label testoRec = new Label(v.getRecensione());
                    testoRec.getElement().getStyle().setProperty("marginTop", "10px");
                    testoRec.getElement().getStyle().setProperty("fontStyle", "italic");

                    cardRecensione.add(headerRec);
                    cardRecensione.add(testoRec);

                    storicoContainer.add(cardRecensione);
                }
            }
        });
    }

    // Legge il file e lo trasforma in testo Base64
    private native void leggiImmagineBase(Element input, ProfileGui gui) /*-{
        var file = input.files[0];
        if (!file) return;
        var reader = new FileReader();
        reader.onload = function(e) {
            gui.@it.unibo.ProfileGui::aggiornaFotoCaricata(Ljava/lang/String;)(e.target.result);
        };
        reader.readAsDataURL(file);
    }-*/;

    public void aggiornaFotoCaricata(String base) {
        this.fotoBase = base;          
        this.photoImg.setUrl(base);    
        this.editButton.setVisible(true);  
    }

}