package it.unibo;

import java.util.L1ist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.DialogBox;

public class SkillsGui extends Composite {

    private String utenteCorrente;
    private Image imgProfilo;

    private VerticalPanel mainPanel = new VerticalPanel();
    private SimplePanel contentArea = new SimplePanel();

    // Contenitori interni che si popoleranno con i dati delle skill
    private VerticalPanel listaMieSkill = new VerticalPanel();
    private VerticalPanel listaRichiesteAttesa = new VerticalPanel();
    private VerticalPanel listaSkillsAccettate = new VerticalPanel();
    private VerticalPanel listaSkillsConcluse = new VerticalPanel();

    private SkillServiceAsync skillService = GWT.create(SkillService.class);
    private MarketServiceAsync marketService = GWT.create(MarketService.class);
    private RichiesteServiceAsync richiesteService = GWT.create(RichiesteService.class);

    public SkillsGui() {
        this.utenteCorrente = SessionManager.getUtenteLoggato();

        initWidget(mainPanel);
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);

        HorizontalPanel splitLayout = new HorizontalPanel();
        splitLayout.setWidth("100%");
        splitLayout.setSpacing(20);

        // Sidebar con menù a tendina
        VerticalPanel sidebar = new VerticalPanel();
        sidebar.setWidth("300px");

        // Tendina "Mie Skills" (Attive)
        DisclosurePanel discMieSkills = new DisclosurePanel("Mie Skills");
        discMieSkills.getElement().setId("sidebar-mie-skills");
        discMieSkills.setOpen(true);
        listaMieSkill.setWidth("100%");
        discMieSkills.setContent(listaMieSkill);

        // Tendina "Richieste in attesa"
        DisclosurePanel discRichiesteAttesa = new DisclosurePanel("Richieste in attesa");
        discRichiesteAttesa.getElement().setId("sidebar-richieste-attesa");
        discRichiesteAttesa.setOpen(true);
        listaRichiesteAttesa.setWidth("100%");
        discRichiesteAttesa.setContent(listaRichiesteAttesa);

        // Tendina "Skills Accettate"
        DisclosurePanel discAccettate = new DisclosurePanel("Skills Accettate");
        discAccettate.getElement().setId("sidebar-skills-accettate");
        listaSkillsAccettate.setWidth("100%");
        discAccettate.setContent(listaSkillsAccettate);

        // Tendina "Skills Concluse"
        DisclosurePanel discConcluse = new DisclosurePanel("Skills Concluse");
        discConcluse.getElement().setId("sidebar-skills-concluse");
        listaSkillsConcluse.setWidth("100%");
        discConcluse.setContent(listaSkillsConcluse);

        sidebar.add(discMieSkills);
        sidebar.add(discRichiesteAttesa);
        sidebar.add(discAccettate);
        sidebar.add(discConcluse);

        contentArea.setWidth("100%");
        contentArea.setWidget(new Label("Seleziona una skill o una richiesta per vedere i dettagli."));

        splitLayout.add(sidebar);
        splitLayout.add(contentArea);

        splitLayout.setCellVerticalAlignment(sidebar, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellVerticalAlignment(contentArea, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellWidth(contentArea, "100%");

        mainPanel.add(splitLayout);

        getAnnunciPubblicati();
        getRichiesteScambio();
    }

    // Metodo di supporto per evitare di duplicare lo stile dei bottoni
    private Button creaBottoneSidebar(String testo) {
        Button btn = new Button(testo);
        btn.getElement().getStyle().setProperty("width", "95%"); 
        btn.getElement().getStyle().setProperty("boxSizing", "border-box");
        btn.getElement().getStyle().setProperty("textAlign", "left");
        btn.getElement().getStyle().setProperty("padding", "10px");
        btn.getElement().getStyle().setProperty("marginBottom", "5px"); 
        btn.getElement().getStyle().setProperty("backgroundColor", "#fff");
        btn.getElement().getStyle().setProperty("border", "1px solid #000");
        return btn;
    }

    private void getAnnunciPubblicati() {
        skillService.getAnnunciPubblicati(utenteCorrente, new AsyncCallback<List<Annuncio>>() {
            @Override
            public void onFailure(Throwable caught) {
                contentArea.setWidget(new Label("Errore di rete: Impossibile caricare le skill pubblicate."));
            }

            @Override
            public void onSuccess(List<Annuncio> annunciPubblicati) {
                listaMieSkill.clear();

                for (Annuncio s : annunciPubblicati) {
                    Button btnSkill = creaBottoneSidebar(s.getTitolo());
                    btnSkill.addClickHandler(event -> mostraDettagliAnnuncio(s));
                    listaMieSkill.add(btnSkill);
                }
            }
        });
    }

    private void getRichiesteScambio() {
        skillService.getRichiesteScambio(utenteCorrente, new AsyncCallback<List<RichiestaScambio>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Errore di rete: Impossibile caricare le richieste di skill.");
            }

            @Override
            public void onSuccess(List<RichiestaScambio> richiesteScambio) {
                listaRichiesteAttesa.clear();
                listaSkillsAccettate.clear();
                listaSkillsConcluse.clear();

                for (RichiestaScambio r : richiesteScambio) {
                    Button btnSkill = creaBottoneSidebar("Caricamento...");

                    // Chiamata diretta per ottenere il titolo dell'annuncio
                    skillService.getAnnuncioById(r.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            btnSkill.setText("Errore caricamento titolo");
                        }
                        @Override
                        public void onSuccess(Annuncio annuncio) {
                            btnSkill.setText(annuncio.getTitolo());
                        }
                    });

                    btnSkill.addClickHandler(event -> mostraDettagliRichiestaScambio(r));

                    switch(r.getStato()) {
                        case IN_ATTESA:
                            listaRichiesteAttesa.add(btnSkill);
                            break;
                        case ACCETTATO:
                            listaSkillsAccettate.add(btnSkill);
                            break;
                        case RIFIUTATO:
                            continue;
                        case CONCLUSO:
                            listaSkillsConcluse.add(btnSkill);
                            break;
                    }
                }
            }
        });
    }

    private void mostraDettagliRichiestaScambio(RichiestaScambio richiesta) {
        contentArea.setWidget(new Label("Caricamento dettagli in corso..."));

        // Chiamata asincrona inlined per l'annuncio
        skillService.getAnnuncioById(richiesta.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERRORE RPC: " + caught.toString());
                GWT.log("Errore completo:", caught); // Controlla la console del browser (F12)
            }

            @Override
            public void onSuccess(Annuncio annuncio) {
                // Chiamata asincrona inlined per l'utente
                skillService.getUtenteById(richiesta.getRichiedenteUser(), new AsyncCallback<Utente>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        contentArea.setWidget(new Label("Errore nel recupero dell'utente richiedente."));
                    }

                    @Override
                    public void onSuccess(Utente utente) {
                        disegnaCardRichiesta(richiesta, annuncio, utente);
                    }
                });
            }
        });
    }

    public static void apriPopupValutazione(Annuncio skill, SkillServiceAsync skillService) {
        DialogBox popup = new DialogBox();
        popup.setText("Valuta lo scambio");
        popup.setAnimationEnabled(true);
        popup.setGlassEnabled(true);

        VerticalPanel panel = new VerticalPanel();
        panel.setSpacing(10);
        panel.setWidth("300px");

        panel.add(new Label("Titolo Scambio: " + skill.getTitolo()));

        TextArea txtRecensione = new TextArea();
        txtRecensione.getElement().setId("input-recensione");
        txtRecensione.setWidth("100%");
        txtRecensione.setVisibleLines(4);
        panel.add(new Label("Scrivi una recensione:"));
        panel.add(txtRecensione);

        HorizontalPanel starPanel = new HorizontalPanel();
        starPanel.setSpacing(5);
        final int[] votoSelezionato = {0}; 
        Label[] stelle = new Label[5];
        
        for (int i = 0; i < 5; i++) {
            final int starValue = i + 1;
            stelle[i] = new Label("☆");
            stelle[i].getElement().setId("star-" + starValue);
            stelle[i].getElement().getStyle().setProperty("fontSize", "24px");
            stelle[i].getElement().getStyle().setProperty("cursor", "pointer");
            
            stelle[i].addClickHandler(e -> {
                votoSelezionato[0] = starValue;
                for (int j = 0; j < 5; j++) {
                    stelle[j].setText(j < starValue ? "★" : "☆");
                    stelle[j].getElement().getStyle().setProperty("color", j < starValue ? "#FFD700" : "#000000"); 
                }
            });
            starPanel.add(stelle[i]);
        }
        panel.add(new Label("Voto:"));
        panel.add(starPanel);

        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.setSpacing(10);
        
        Button btnAnnulla = new Button("Annulla");
        btnAnnulla.getElement().setId("btn-annulla-valutazione");
        btnAnnulla.addClickHandler(e -> popup.hide());
        
        Button btnInvia = new Button("Invia");
        btnInvia.getElement().setId("btn-invia-valutazione");
        btnInvia.addClickHandler(e -> {
            if (votoSelezionato[0] == 0) {
                Window.alert("Per favore, seleziona un voto con le stelle.");
                return;
            }
            
            Valutazione nuovaValutazione = new Valutazione.Builder()
                .id(skill.getId())
                .autore("admin")
                .voto(votoSelezionato[0])
                .recensione(txtRecensione.getText())
                .build();
                
            skillService.salvaValutazione(nuovaValutazione, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("Errore di connessione.");
                }
                @Override
                public void onSuccess(Boolean salvata) {
                    if (salvata) {
                        Window.alert("Valutazione salvata con successo!");
                        popup.hide();
                    } else {
                        Window.alert("Errore: Hai già valutato questo scambio.");
                    }
                }
            });
        });

        btnPanel.add(btnInvia);
        btnPanel.add(btnAnnulla);
        panel.add(btnPanel);
        
        popup.setWidget(panel);
        popup.center();
    }

    private void disegnaCardRichiesta(RichiestaScambio richiesta, Annuncio annuncio, Utente utente) {
        contentArea.clear();

        VerticalPanel card = new VerticalPanel();
        card.setWidth("100%");
        card.getElement().getStyle().setProperty("border", "2px solid #000");
        card.getElement().getStyle().setProperty("padding", "20px");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

        // Header della card con titolo e rating
        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");
        cardHeader.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblTitolo = new Label(annuncio.getTitolo().toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");
        lblTitolo.getElement().setId("lbl-titolo");

        HorizontalPanel userPanel = new HorizontalPanel();

        Label lblRating = new Label("4.9");
        lblRating.getElement().getStyle().setProperty("fontSize", "18px");
        lblRating.getElement().getStyle().setProperty("fontWeight", "bold");

        imgProfilo = new Image();
        imgProfilo.setPixelSize(40, 40);
        imgProfilo.getElement().getStyle().setProperty("borderRadius", "50%");
        imgProfilo.getElement().getStyle().setProperty("objectFit", "cover");
        imgProfilo.getElement().getStyle().setProperty("border", "2px solid #007BFF");
        imgProfilo.getElement().getStyle().setProperty("marginLeft", "15px");
        imgProfilo.getElement().setId("nav-profilo");
        caricaImmagineProfilo(imgProfilo, richiesta.getRichiedenteUser());

        userPanel.add(lblRating);
        userPanel.add(imgProfilo);
        userPanel.setCellVerticalAlignment(lblRating, HasVerticalAlignment.ALIGN_MIDDLE);

        VerticalPanel oggettiUtente = new VerticalPanel();

        Label usernameRichiedente = new Label(utente.getUsername());
        usernameRichiedente.getElement().getStyle().setProperty("fontSize", "18px");
        usernameRichiedente.getElement().getStyle().setProperty("fontWeight", "bold");
        usernameRichiedente.getElement().getStyle().setProperty("marginTop", "10px");

        oggettiUtente.add(userPanel);
        oggettiUtente.add(usernameRichiedente);
        oggettiUtente.setCellHorizontalAlignment(userPanel, HasHorizontalAlignment.ALIGN_CENTER);

        cardHeader.add(lblTitolo);
        cardHeader.add(oggettiUtente);
        cardHeader.setCellVerticalAlignment(lblTitolo, HasVerticalAlignment.ALIGN_MIDDLE);
        cardHeader.setCellHorizontalAlignment(oggettiUtente, HasHorizontalAlignment.ALIGN_RIGHT);

        Label lblCat = new Label("CATEGORIA: " + annuncio.getCategoria());
        lblCat.getElement().getStyle().setProperty("marginBottom", "10px");      

        Label lblOgg = new Label("DETTAGLI OGGETTO: " + annuncio.getSkillOfferta());
        lblOgg.getElement().getStyle().setProperty("marginBottom", "10px");
        lblOgg.getElement().setId("lbl-descrizione");
        
        Label lblDisp = new Label("DISPONIBILITÀ: " + annuncio.getDisponibilita());
        lblDisp.getElement().getStyle().setProperty("marginBottom", "10px");
        
        Label lblContro = new Label("CONTROPRESTAZIONE OFFERTA: " + annuncio.getControprestazione());
        lblContro.getElement().getStyle().setProperty("marginBottom", "20px");

        card.add(cardHeader);
        card.add(lblCat);
        card.add(lblOgg);
        card.add(lblDisp);
        card.add(lblContro);

        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");

        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        Button btnChat = new Button("💬");
        btnChat.getElement().getStyle().setProperty("backgroundColor", "#007bff");
        btnChat.getElement().getStyle().setProperty("color", "#fff");

        switch(richiesta.getStato()) {
            case IN_ATTESA:
                Button btnAccettaScambio = new Button("Accetta Richiesta");
                buttonGroups.add(btnAccettaScambio);
                buttonGroups.add(btnChat);
                break;

<<<<<<< HEAD
            case ACCETTATO:
                Button btnConfermaScambio = new Button("✓");
                Button btnSegnalaScambioNonAvvenuto = new Button("X");
                buttonGroups.add(btnConfermaScambio);
                buttonGroups.add(btnSegnalaScambioNonAvvenuto);
                buttonGroups.add(btnChat);

                Button btnTick = new Button("✓");
            Button btnX = new Button("X");
            Button btnChat = new Button("💬");

            btnTick.getElement().setId("btn-tick-conferma");
            btnX.getElement().setId("btn-x-rifiuto");

            btnTick.addClickHandler(event -> {
                btnTick.setEnabled(false);
                String utenteAttuale = SessionManager.getUtenteLoggato();
                
                richiesteService.elaboraAzioneScambio(skill.getId(), utenteAttuale, true, new AsyncCallback<RichiestaScambio>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Errore durante la conferma: " + caught.getMessage());
                        btnTick.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(RichiestaScambio result) {
                        if (result != null && result.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO || result.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO) {
                            Window.alert("Scambio concluso con successo! Entrambi avete confermato.");
                            contentArea.clear();
                            caricaSkillsDalDatabase();
                        } else {
                            btnTick.setText("In attesa della controparte...");
                        }
                    }
                });
            });

            btnX.addClickHandler(event -> {
                if (Window.confirm("Sei sicuro di voler rifiutare o annullare questo scambio?")) {
                    String utenteAttuale = SessionManager.getUtenteLoggato();
                    richiesteService.elaboraAzioneScambio(skill.getId(), utenteAttuale, false, new AsyncCallback<RichiestaScambio>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore durante l'annullamento: " + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(RichiestaScambio result) {
                            Window.alert("Scambio annullato.");
                            contentArea.clear();
                            caricaSkillsDalDatabase();
                        }
                    });
                }
            });

            buttonGroups.add(btnTick);
            buttonGroups.add(btnX);
            buttonGroups.add(btnChat);
                break;
            
            case CONCLUSO:
                Button btnValuta = new Button("Valuta");
                buttonGroups.add(btnValuta);
                buttonGroups.add(btnChat);

                buttonGroups.add(new Button("💬"));
            
            Button btnValuta = new Button("Valuta");
            btnValuta.getElement().setId("btn-valuta-scambio");
            btnValuta.addClickHandler(event -> {
                apriPopupValutazione(skill, skillService);
            });
            buttonGroups.add(btnValuta);
                break;
        
        buttonWrapper.add(buttonGroups);
        buttonWrapper.setCellHorizontalAlignment(buttonGroups, HasHorizontalAlignment.ALIGN_RIGHT);

        card.add(buttonWrapper);
        contentArea.add(card);
    }

    private void mostraDettagliAnnuncio(Annuncio skill) {
        contentArea.clear();

        VerticalPanel card = new VerticalPanel();
        card.setWidth("100%");
        card.getElement().getStyle().setProperty("border", "2px solid #000");
        card.getElement().getStyle().setProperty("padding", "20px");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");
        cardHeader.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblTitolo = new Label(skill.getTitolo().toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");
        lblTitolo.getElement().setId("lbl-titolo");
        cardHeader.add(lblTitolo);

        Label lblCat = new Label("CATEGORIA: " + skill.getCategoria());
        lblCat.getElement().getStyle().setProperty("marginBottom", "10px");      

        Label lblOgg = new Label("DETTAGLI OGGETTO: " + skill.getSkillOfferta());
        lblOgg.getElement().getStyle().setProperty("marginBottom", "10px");
        lblOgg.getElement().setId("lbl-descrizione");
        
        Label lblDisp = new Label("DISPONIBILITÀ: " + skill.getDisponibilita());
        lblDisp.getElement().getStyle().setProperty("marginBottom", "10px");
        
        Label lblContro = new Label("CONTROPRESTAZIONE OFFERTA: " + skill.getControprestazione());
        lblContro.getElement().getStyle().setProperty("marginBottom", "20px");

        card.add(cardHeader);
        card.add(lblCat);
        card.add(lblOgg);
        card.add(lblDisp);
        card.add(lblContro);

        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");

        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        // Realizzazione del bottone Rimuovi con la tua logica RPC
            Button btnRimuovi = new Button("Rimuovi");
            
            btnRimuovi.addClickHandler(event -> {
                boolean confermato = Window.confirm("Sei sicuro di voler eliminare definitivamente questo annuncio dal Marketplace?");
                
                if (confermato) {
                    skillService.deleteSkill(skill.getId(), new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore di comunicazione con il server.");
                        }

                        @Override
                        public void onSuccess(Boolean eliminato) {
                            if (eliminato) {
                                // Svuota l'area centrale e ricarica la tendina aggiornata senza la skill eliminata
                                contentArea.clear();
                                caricaSkillsDalDatabase();
                            } else {
                                Window.alert("Errore: Impossibile trovare la skill da eliminare.");
                            }
                        }
                    });
                }
            });
        /*
            // Creiamo il bottone Modifica mantenendo l'ID inserito dal tuo collega (da main)
            Button btnModifica = new Button("Modifica");
            btnModifica.getElement().setId("btn-modifica-annuncio");
            
            // Aggiungiamo i bottoni modificati al gruppo
            buttonGroups.add(btnRimuovi);
            buttonGroups.add(btnModifica);

            Button btnChat = new Button("💬");
            btnChat.getElement().getStyle().setProperty("backgroundColor", "#007bff");
            btnChat.getElement().getStyle().setProperty("color", "#fff");

            // Gestione click Modifica
            btnModifica.addClickHandler(clickEvent -> {
                card.clear();
                card.add(cardHeader);

                TextBox txtTitolo = new TextBox();
                txtTitolo.getElement().setId("input-modifica-titolo");
                txtTitolo.setText(skill.getTitolo());
                txtTitolo.setWidth("100%");
                card.add(new Label("TITOLO:"));
                card.add(txtTitolo);

                // ListBox per le categorie
                ListBox listCat = new com.google.gwt.user.client.ui.ListBox();
                card.add(new Label("CATEGORIA:"));
                card.add(listCat);

                // Recupero categorie
                CreateAdServiceAsync adService = GWT.create(CreateAdService.class);
                adService.getCategorie(new AsyncCallback<List<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        listCat.addItem(skill.getCategoria());
                    }

                    @Override
                    public void onSuccess(List<String> categorieDalDb) {
                        for (String cat : categorieDalDb) {
                            listCat.addItem(cat);
                        }

                        // Seleziona la categoria attuale dell'annuncio
                        for (int i = 0; i < listCat.getItemCount(); i++) {
                            if (listCat.getItemText(i).equalsIgnoreCase(skill.getCategoria())) {
                                listCat.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                });

                // Skill Offerta
                TextArea txtDesc = new TextArea();
                txtDesc.getElement().setId("input-modifica-descrizione");
                txtDesc.setText(skill.getSkillOfferta());
                txtDesc.setWidth("100%");
                card.add(new Label("DETTAGLI OGGETTO:"));
                card.add(txtDesc);

                // Disponibilità
                TextArea txtDisp = new TextArea();
                txtDisp.getElement().setId("input-modifica-disponibilita");
                txtDisp.setText(skill.getDisponibilita());
                txtDisp.setWidth("100%");
                card.add(new Label("DISPONIBILITÀ:"));
                card.add(txtDisp);

                // Bottone Conferma
                Button btnConferma = new Button("Conferma");
                btnConferma.getElement().setId("btn-modifica-conferma");

                btnConferma.addClickHandler(confermaEvent -> {
                    if (txtTitolo.getText().trim().isEmpty() || txtDesc.getText().trim().isEmpty()
                            || txtDisp.getText().trim().isEmpty()) {
                        Window.alert("Tutti i campi sono obbligatori!");
                        return;
                    }

                    // Aggiorna localmente l'oggetto con i valori inseriti dall'utente nel form
                    skill.setTitolo(txtTitolo.getText().trim());
                    skill.setCategoria(listCat.getSelectedItemText());
                    skill.setOffro(txtDesc.getText().trim());
                    skill.setDisponibilita(txtDisp.getText().trim());

                    adService.aggiornaAnnuncio(skill.getId(), skill, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore di rete: " + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result) {
                                caricaSkillsDalDatabase();
                                mostraDettagliCard(skill);
                            } else {
                                Window.alert("Errore: Impossibile aggiornare l'annuncio nel database.");
                            }
                        }
                    });
                });

                HorizontalPanel confWrapper = new HorizontalPanel();
                confWrapper.setWidth("100%");
                confWrapper.add(btnConferma);
                confWrapper.setCellHorizontalAlignment(btnConferma, HasHorizontalAlignment.ALIGN_RIGHT);
                card.add(confWrapper);
            });

            buttonGroups.add(btnRimuovi);
            buttonGroups.add(btnModifica);
            buttonGroups.add(btnChat);
        */

        Button btnRimuovi = new Button("Rimuovi");
        btnRimuovi.addClickHandler(event -> {
            boolean confermato = Window.confirm("Sei sicuro di voler eliminare definitivamente questo annuncio dal Marketplace?");
            if (confermato) {
                skillService.deleteSkill(skill.getId(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Errore di comunicazione con il server.");
                    }

                    @Override
                    public void onSuccess(Boolean eliminato) {
                        if (eliminato) {
                            contentArea.clear();
                            getAnnunciPubblicati();
                            getRichiesteScambio();
                        } else {
                            Window.alert("Errore: Impossibile trovare la skill da eliminare.");
                        }
                    }
                });
            }
        });
        
        Button btnModifica = new Button("Modifica");
        btnModifica.getElement().setId("btn-modifica-annuncio");

        btnModifica.addClickHandler(clickEvent -> {
            card.clear();
            card.add(cardHeader);

            TextBox txtTitolo = new TextBox();
            txtTitolo.getElement().setId("input-modifica-titolo");
            txtTitolo.setText(skill.getTitolo());
            txtTitolo.setWidth("100%");
            card.add(new Label("TITOLO:"));
            card.add(txtTitolo);

            ListBox listCat = new ListBox();
            card.add(new Label("CATEGORIA:"));
            card.add(listCat);

            CreateAdServiceAsync adService = GWT.create(CreateAdService.class);
            adService.getCategorie(new AsyncCallback<List<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    listCat.addItem(skill.getCategoria());
                }

                @Override
                public void onSuccess(List<String> categorieDalDb) {
                    for (String cat : categorieDalDb) {
                        listCat.addItem(cat);
                    }
                    for (int i = 0; i < listCat.getItemCount(); i++) {
                        if (listCat.getItemText(i).equalsIgnoreCase(skill.getCategoria())) {
                            listCat.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            });

            TextArea txtDesc = new TextArea();
            txtDesc.getElement().setId("input-modifica-descrizione");
            txtDesc.setText(skill.getSkillOfferta());
            txtDesc.setWidth("100%");
            card.add(new Label("DETTAGLI OGGETTO:"));
            card.add(txtDesc);

            TextArea txtDisp = new TextArea();
            txtDisp.getElement().setId("input-modifica-disponibilita");
            txtDisp.setText(skill.getDisponibilita());
            txtDisp.setWidth("100%");
            card.add(new Label("DISPONIBILITÀ:"));
            card.add(txtDisp);

            Button btnConferma = new Button("Conferma");
            btnConferma.getElement().setId("btn-modifica-conferma");

            btnConferma.addClickHandler(confermaEvent -> {
                if (txtTitolo.getText().trim().isEmpty() || txtDesc.getText().trim().isEmpty() || txtDisp.getText().trim().isEmpty()) {
                    Window.alert("Tutti i campi sono obbligatori!");
                    return;
                }

                skill.setTitolo(txtTitolo.getText().trim());
                skill.setCategoria(listCat.getSelectedItemText());
                skill.setSkillOfferta(txtDesc.getText().trim());
                skill.setDisponibilita(txtDisp.getText().trim());

                adService.aggiornaAnnuncio(skill.getId(), skill, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Errore di rete: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            getAnnunciPubblicati();
                            mostraDettagliAnnuncio(skill);
                        } else {
                            Window.alert("Errore: Impossibile aggiornare l'annuncio nel database.");
                        }
                    }
                });
            });

            HorizontalPanel confWrapper = new HorizontalPanel();
            confWrapper.setWidth("100%");
            confWrapper.add(btnConferma);
            confWrapper.setCellHorizontalAlignment(btnConferma, HasHorizontalAlignment.ALIGN_RIGHT);
            card.add(confWrapper);
        });

        buttonGroups.add(btnRimuovi);
        buttonGroups.add(btnModifica);
        buttonWrapper.add(buttonGroups);
        buttonWrapper.setCellHorizontalAlignment(buttonGroups, HasHorizontalAlignment.ALIGN_RIGHT);
        
        card.add(buttonWrapper);
        contentArea.add(card);
    }

    private void caricaImmagineProfilo(Image imgProfilo, String username) {
        marketService.getUtente(username, new AsyncCallback<Utente>() {
            @Override
            public void onFailure(Throwable caught) {
                imgProfilo.setUrl("images/utente.jpg");
            }

            @Override
            public void onSuccess(Utente utenteCompleto) {
                if (utenteCompleto != null && utenteCompleto.getFotoProfiloBase64() != null) {
                    imgProfilo.setUrl(utenteCompleto.getFotoProfiloBase64());
                } else {
                    imgProfilo.setUrl("images/utente.jpg");
                }
            }
        });
    }
}