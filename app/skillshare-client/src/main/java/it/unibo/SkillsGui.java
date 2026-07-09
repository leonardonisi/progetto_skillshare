package it.unibo;

import java.util.List;
import com.google.gwt.core.client.GWT;
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

    // Contenitori interni
    private VerticalPanel listaMieSkill = new VerticalPanel();
    private VerticalPanel listaRichiesteAttesa = new VerticalPanel();
    private VerticalPanel listaSkillsAccettate = new VerticalPanel();
    private VerticalPanel listaSkillsConcluse = new VerticalPanel();

    private VerticalPanel listaSkillsRifiutate = new VerticalPanel();

    private SkillServiceAsync skillService = GWT.create(SkillService.class);
    private MarketServiceAsync marketService = GWT.create(MarketService.class);
    private RichiesteServiceAsync richiesteService = GWT.create(RichiesteService.class);
    private MainLayoutGui mainLayout;

    public SkillsGui(MainLayoutGui mainLayout) {
        this.utenteCorrente = SessionManager.getUtenteLoggato();
        this.mainLayout = mainLayout;

        initWidget(mainPanel);
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);

        HorizontalPanel splitLayout = new HorizontalPanel();
        splitLayout.setWidth("100%");
        splitLayout.setSpacing(20);

        VerticalPanel sidebar = new VerticalPanel();
        sidebar.setWidth("300px");

        DisclosurePanel discMieSkills = new DisclosurePanel("Mie Skills");
        discMieSkills.setOpen(true);
        listaMieSkill.setWidth("100%");
        discMieSkills.setContent(listaMieSkill);

        DisclosurePanel discRichiesteAttesa = new DisclosurePanel("Richieste in attesa");
        discRichiesteAttesa.setOpen(true);
        listaRichiesteAttesa.setWidth("100%");
        discRichiesteAttesa.setContent(listaRichiesteAttesa);

        DisclosurePanel discAccettate = new DisclosurePanel("Skills Accettate");
        listaSkillsAccettate.setWidth("100%");
        discAccettate.setContent(listaSkillsAccettate);

        DisclosurePanel discConcluse = new DisclosurePanel("Skills Concluse");
        listaSkillsConcluse.setWidth("100%");
        discConcluse.setContent(listaSkillsConcluse);

        DisclosurePanel discRifiutate = new DisclosurePanel("Skills Rifiutate");
        listaSkillsRifiutate.setWidth("100%");
        discRifiutate.setContent(listaSkillsRifiutate);

        sidebar.add(discMieSkills);
        sidebar.add(discRichiesteAttesa);
        sidebar.add(discAccettate);
        sidebar.add(discConcluse);
        sidebar.add(discRifiutate);

        contentArea.setWidth("100%");
        contentArea.setWidget(new Label("Seleziona una skill o una richiesta per vedere i dettagli."));

        splitLayout.add(sidebar);
        splitLayout.add(contentArea);
        splitLayout.setCellVerticalAlignment(sidebar, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellVerticalAlignment(contentArea, HasVerticalAlignment.ALIGN_TOP);

        mainPanel.add(splitLayout);

        // Caricamento dati iniziali
        aggiornaTuttiIDati();
    }

    // Metodo helper per aggiornare tutte le liste
    private void aggiornaTuttiIDati() {
        listaMieSkill.clear();
        listaRichiesteAttesa.clear();
        listaSkillsAccettate.clear();
        listaSkillsConcluse.clear();
        listaSkillsRifiutate.clear();

        richiesteService.getTutteLeRichieste(new AsyncCallback<List<RichiestaScambio>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<RichiestaScambio> tutteLeRichieste) {
                skillService.getAnnunciPubblicati(utenteCorrente, new AsyncCallback<List<Annuncio>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        contentArea.setWidget(new Label("Errore nel caricamento delle skill pubblicate."));
                    }

                    @Override
                    public void onSuccess(List<Annuncio> annunciPubblicati) {
                        for (Annuncio s : annunciPubblicati) {
                            Button btnSkill = creaBottoneSidebar(s.getTitolo());

                            String statoScambioDellaSkill = "ATTIVA";
                            RichiestaScambio richiestaAssociata = null;

                            for (RichiestaScambio r : tutteLeRichieste) {
                                if (r.getIdAnnuncio().equals(s.getId())) {
                                    if (r.getStato() == RichiestaScambio.StatoRichiesta.ACCETTATO) {
                                        statoScambioDellaSkill = "ACCETTATA";
                                        richiestaAssociata = r;
                                    } else if (r.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO) {
                                        statoScambioDellaSkill = "CONCLUSA";
                                        richiestaAssociata = r;
                                    } else if (r.getStato() == RichiestaScambio.StatoRichiesta.RIFIUTATO) {
                                        statoScambioDellaSkill = "RIFIUTATA";
                                        richiestaAssociata = r;
                                    }
                                }
                            }

                            final String statoDaPassare = statoScambioDellaSkill;
                            final RichiestaScambio reqAssociata = richiestaAssociata;

                            if (statoDaPassare.equals("ATTIVA")) {
                                btnSkill.addClickHandler(event -> mostraDettagliAnnuncio(s));
                                listaMieSkill.add(btnSkill);
                            } else if (statoDaPassare.equals("ACCETTATA")) {
                                btnSkill.addClickHandler(event -> mostraDettagliRichiestaScambio(reqAssociata));
                                listaSkillsAccettate.add(btnSkill);
                            } else if (statoDaPassare.equals("CONCLUSA")) {
                                btnSkill.addClickHandler(event -> mostraDettagliRichiestaScambio(reqAssociata));
                                listaSkillsConcluse.add(btnSkill);
                            } else if (statoDaPassare.equals("RIFIUTATA")) {
                                btnSkill.addClickHandler(event -> mostraDettagliRichiestaScambio(reqAssociata));
                                listaSkillsRifiutate.add(btnSkill);
                            }
                        }
                        getRichiesteRicevute();
                    }
                });
            }
        });
    }

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

                    skillService.getAnnuncioById(r.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            btnSkill.setText("Errore caricamento");
                        }

                        @Override
                        public void onSuccess(Annuncio annuncio) {
                            if (annuncio != null)
                                btnSkill.setText(annuncio.getTitolo());
                        }
                    });

                    btnSkill.addClickHandler(event -> mostraDettagliRichiestaScambio(r));

                    // Ordinamento nelle sidebar in base allo stato
                    switch (r.getStato()) {
                        case IN_ATTESA:
                            listaRichiesteAttesa.add(btnSkill);
                            break;
                        case ACCETTATO:
                            listaSkillsAccettate.add(btnSkill);
                            break;
                        case RIFIUTATO:
                            // Ignoriamo i rifiutati o li mettiamo in un'altra lista
                            break;
                        case CONCLUSO:
                            listaSkillsConcluse.add(btnSkill);
                            break;
                    }
                }
            }
        });
    }

    private void getRichiesteRicevute() {
        richiesteService.getRichiesteRicevute(utenteCorrente, new AsyncCallback<List<RichiestaScambio>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<RichiestaScambio> richiesteRicevute) {
                for (RichiestaScambio req : richiesteRicevute) {
                    // Mostriamo solo quelle ancora in attesa di risposta
                    if (req.getStato() == RichiestaScambio.StatoRichiesta.IN_ATTESA) {
                        Button btnRichiesta = creaBottoneSidebar("Proposta da: " + req.getRichiedenteUser());
                        btnRichiesta.getElement().getStyle().setProperty("border", "1px solid #ff9800");

                        btnRichiesta.addClickHandler(ev -> mostraDettagliPropostaRicevuta(req));
                        listaRichiesteAttesa.add(btnRichiesta);
                    }
                }
            }
        });
    }

    private void mostraDettagliPropostaRicevuta(RichiestaScambio req) {
        contentArea.clear();
        VerticalPanel dettagliCard = new VerticalPanel();
        dettagliCard.setSpacing(10);
        dettagliCard.add(new Label("PROPOSTA RICEVUTA DA: " + req.getRichiedenteUser()));
        dettagliCard.add(new Label("DETTAGLI PROPOSTA: " + req.getMessaggioProposta()));

        HorizontalPanel azioniPanel = new HorizontalPanel();
        azioniPanel.setSpacing(10);

        Button btnAccetta = new Button("Accetta");
        Button btnRifiuta = new Button("Rifiuta");

        btnAccetta.addClickHandler(click -> {
            richiesteService.gestisciRispostaRichiesta(req.getIdAnnuncio(), true,
                    new AsyncCallback<RichiestaScambio>() {
                        @Override
                        public void onSuccess(RichiestaScambio result) {
                            Window.alert("Scambio accettato con successo!");
                            contentArea.clear();
                            aggiornaTuttiIDati();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore: " + caught.getMessage());
                        }
                    });
        });

        btnRifiuta.addClickHandler(click -> {
            richiesteService.gestisciRispostaRichiesta(req.getIdAnnuncio(), false,
                    new AsyncCallback<RichiestaScambio>() {
                        @Override
                        public void onSuccess(RichiestaScambio result) {
                            Window.alert("Scambio rifiutato.");
                            contentArea.clear();
                            aggiornaTuttiIDati();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                        }
                    });
        });

        azioniPanel.add(btnAccetta);
        azioniPanel.add(btnRifiuta);
        dettagliCard.add(azioniPanel);
        contentArea.add(dettagliCard);
    }

    private void mostraDettagliRichiestaScambio(RichiestaScambio richiesta) {
        contentArea.setWidget(new Label("Caricamento dettagli in corso..."));

        skillService.getAnnuncioById(richiesta.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERRORE: Impossibile recuperare l'annuncio.");
            }

            @Override
            public void onSuccess(Annuncio annuncio) {
                skillService.getUtenteById(richiesta.getRichiedenteUser(), new AsyncCallback<Utente>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        contentArea.setWidget(new Label("Errore nel recupero dell'utente."));
                    }

                    @Override
                    public void onSuccess(Utente utente) {
                        disegnaCardRichiesta(richiesta, annuncio, utente);
                    }
                });
            }
        });
    }

    private void disegnaCardRichiesta(RichiestaScambio richiesta, Annuncio annuncio, Utente utente) {
        contentArea.clear();

        VerticalPanel card = new VerticalPanel();
        card.setWidth("100%");
        card.getElement().getStyle().setProperty("border", "2px solid #000");
        card.getElement().getStyle().setProperty("padding", "20px");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");
        cardHeader.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblTitolo = new Label(annuncio.getTitolo().toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");

        HorizontalPanel userPanel = new HorizontalPanel();
        Label lblRating = new Label("4.9");
        lblRating.getElement().getStyle().setProperty("fontSize", "18px");
        lblRating.getElement().getStyle().setProperty("fontWeight", "bold");

        imgProfilo = new Image();
        imgProfilo.setPixelSize(40, 40);
        imgProfilo.getElement().getStyle().setProperty("borderRadius", "50%");
        caricaImmagineProfilo(imgProfilo, richiesta.getRichiedenteUser());

        userPanel.add(lblRating);
        userPanel.add(imgProfilo);
        userPanel.setCellVerticalAlignment(lblRating, HasVerticalAlignment.ALIGN_MIDDLE);

        VerticalPanel oggettiUtente = new VerticalPanel();
        Label usernameRichiedente = new Label(utente.getUsername());
        usernameRichiedente.getElement().getStyle().setProperty("fontWeight", "bold");

        oggettiUtente.add(userPanel);
        oggettiUtente.add(usernameRichiedente);
        oggettiUtente.setCellHorizontalAlignment(userPanel, HasHorizontalAlignment.ALIGN_CENTER);

        cardHeader.add(lblTitolo);
        cardHeader.add(oggettiUtente);
        cardHeader.setCellVerticalAlignment(lblTitolo, HasVerticalAlignment.ALIGN_MIDDLE);
        cardHeader.setCellHorizontalAlignment(oggettiUtente, HasHorizontalAlignment.ALIGN_RIGHT);

        card.add(cardHeader);
        card.add(new Label("CATEGORIA: " + annuncio.getCategoria()));
        card.add(new Label("DETTAGLI OGGETTO: " + annuncio.getSkillOfferta()));
        card.add(new Label("DISPONIBILITÀ: " + annuncio.getDisponibilita()));
        card.add(new Label("CONTROPRESTAZIONE: " + annuncio.getControprestazione()));

        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");
        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        Button btnChat = new Button("💬");

        switch (richiesta.getStato()) {
            case IN_ATTESA:
                buttonGroups.add(btnChat);
                break;

            case ACCETTATO:
                Button btnConfermaScambio = new Button("✓");
                btnConfermaScambio.getElement().setId("btn-tick-conferma");
                Button btnSegnalaAnnullamento = new Button("X");
                btnSegnalaAnnullamento.getElement().setId("btn-x-rifiuto");

                btnConfermaScambio.addClickHandler(event -> {
                    btnConfermaScambio.setEnabled(false);
                    richiesteService.elaboraAzioneScambio(annuncio.getId(), utenteCorrente, true, new AsyncCallback<RichiestaScambio>() {
                        @Override
                        public void onFailure(Throwable caught) { btnConfermaScambio.setEnabled(true); }
                        @Override
                        public void onSuccess(RichiestaScambio result) {
                            if (result != null && result.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO) {
                                Window.alert("Scambio concluso con successo! Entrambi avete confermato.");
                                contentArea.clear();
                                aggiornaTuttiIDati();
                            } else {
                                btnConfermaScambio.setText("In attesa della controparte...");
                            }
                        }
                    });
                });
 
                
                btnSegnalaAnnullamento.addClickHandler(event -> {
                    if (Window.confirm("Sei sicuro di voler segnalare il fallimento di questo scambio?")) {
                        richiesteService.elaboraAzioneScambio(annuncio.getId(), utenteCorrente, false, new AsyncCallback<RichiestaScambio>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Window.alert("Errore durante l'annullamento: " + caught.getMessage());
                            }
 
                            @Override
                            public void onSuccess(RichiestaScambio result) {
                                Window.alert("Scambio segnalato come fallito.");
                                contentArea.clear();
                                aggiornaTuttiIDati();
                            }
                        });
                    }
                });
 
                buttonGroups.add(btnConfermaScambio);
                buttonGroups.add(btnSegnalaAnnullamento);
                buttonGroups.add(btnChat);
                break;

            case CONCLUSO:
                Button btnValuta = new Button("Valuta");
                btnValuta.addClickHandler(event -> apriPopupValutazione(annuncio));
                buttonGroups.add(btnValuta);
                buttonGroups.add(btnChat);
                break;
            
            case RIFIUTATO:
                buttonGroups.add(btnChat);
                break;
        }

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

        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");

        Label lblTitolo = new Label(skill.getTitolo().toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");
        cardHeader.add(lblTitolo);

        card.add(cardHeader);
        card.add(new Label("CATEGORIA: " + skill.getCategoria()));
        card.add(new Label("DETTAGLI OGGETTO: " + skill.getSkillOfferta()));
        card.add(new Label("DISPONIBILITÀ: " + skill.getDisponibilita()));
        card.add(new Label("CONTROPRESTAZIONE: " + skill.getControprestazione()));

        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");
        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        Button btnRimuovi = new Button("Rimuovi");
        btnRimuovi.addClickHandler(event -> {
            if (Window.confirm("Eliminare definitivamente questo annuncio?")) {
                skillService.deleteSkill(skill.getId(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(Boolean eliminato) {
                        if (eliminato) {
                            contentArea.clear();
                            aggiornaTuttiIDati();
                        }
                    }
                });
            }
        });

        Button btnModifica = new Button("Modifica");
        btnModifica.addClickHandler(clickEvent -> {
            // Qui inserisci la tua logica di modifica originaria (CreateAdService)
            Window.alert("Implementazione form di modifica qui");
        });

        buttonGroups.add(btnRimuovi);
        buttonGroups.add(btnModifica);

        buttonWrapper.add(buttonGroups);
        buttonWrapper.setCellHorizontalAlignment(buttonGroups, HasHorizontalAlignment.ALIGN_RIGHT);

        card.add(buttonWrapper);
        contentArea.add(card);
    }

    public void apriPopupValutazione(Annuncio skill) {
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
        final int[] votoSelezionato = { 0 };
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
                    .autore(utenteCorrente)
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

    private void eseguiNavigazioneChat(String interlocutore) {
        String utenteLoggato = SessionManager.getUtenteLoggato();

        // Sicurezza se la sessione locale dovesse essere vuota
        if (utenteLoggato == null || utenteLoggato.isEmpty()) {
            utenteLoggato = "admin";
        }

        if (utenteLoggato.equals(interlocutore)) {
            interlocutore = "UtenteScambio_1";
        }

        ChatGui vistaChat = new ChatGui();
        mainLayout.cambiaVista(vistaChat);
        // Apre o crea la chat con l'interlocutore corretto
        vistaChat.apriConversazione(interlocutore);
    }
}