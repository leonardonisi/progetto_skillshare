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
import com.google.gwt.i18n.client.NumberFormat;

public class SkillsGui extends Composite {

    private String utenteCorrente;

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
        discMieSkills.getElement().setId("sidebar-mie-skills");

        DisclosurePanel discRichiesteAttesa = new DisclosurePanel("Richieste in attesa");
        discRichiesteAttesa.setOpen(true);
        listaRichiesteAttesa.setWidth("100%");
        discRichiesteAttesa.setContent(listaRichiesteAttesa);

        DisclosurePanel discAccettate = new DisclosurePanel("Skills Accettate");
        listaSkillsAccettate.setWidth("100%");
        discAccettate.setContent(listaSkillsAccettate);
        discAccettate.getElement().setId("sidebar-skills-accettate");

        DisclosurePanel discConcluse = new DisclosurePanel("Skills Concluse");
        listaSkillsConcluse.setWidth("100%");
        discConcluse.setContent(listaSkillsConcluse);
        discConcluse.getElement().setId("sidebar-skills-concluse");

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
        splitLayout.setCellWidth(contentArea, "100%");

        mainPanel.add(splitLayout);

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
                Window.alert("ERRORE getTutteLeRichieste: " + caught.getMessage());
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
                        int indiceSkill = 0; // Contatore incrementale per gli ID attesi dai test Selenium

                        for (Annuncio s : annunciPubblicati) {
                            Button btnSkill = creaBottoneSidebar(s.getTitolo());
                            btnSkill.getElement().setId("btn-skill-" + indiceSkill); // Assegnazione dinamica dell'ID
                            indiceSkill++;

                            String statoScambioDellaSkill = "ATTIVA";
                            RichiestaScambio richiestaAssociata = null;

                            for (RichiestaScambio r : tutteLeRichieste) {
                                if (r.getIdAnnuncio() != null && r.getIdAnnuncio().equals(s.getId())) {

                                    RichiestaScambio.StatoRichiesta statoReq = r.getStato();

                                    if (statoReq == RichiestaScambio.StatoRichiesta.CONCLUSO) {
                                        statoScambioDellaSkill = "CONCLUSA";
                                        richiestaAssociata = r;
                                        break;
                                    } else if (statoReq == RichiestaScambio.StatoRichiesta.ACCETTATO) {
                                        statoScambioDellaSkill = "ACCETTATA";
                                        richiestaAssociata = r;
                                        break;
                                    } else if (statoReq == RichiestaScambio.StatoRichiesta.RIFIUTATO) {
                                        if (statoScambioDellaSkill.equals("ATTIVA")) {
                                            statoScambioDellaSkill = "RIFIUTATA";
                                            richiestaAssociata = r;
                                        }
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
        btn.getElement().getStyle().setProperty("width", "250px");
        btn.getElement().getStyle().setProperty("boxSizing", "border-box");
        btn.getElement().getStyle().setProperty("textAlign", "left");
        btn.getElement().getStyle().setProperty("padding", "10px");
        btn.getElement().getStyle().setProperty("marginBottom", "5px");
        btn.getElement().getStyle().setProperty("backgroundColor", "#fff");
        btn.getElement().getStyle().setProperty("border", "1px solid #000");
        return btn;
    }

    private void getRichiesteRicevute() {
        richiesteService.getRichiesteRicevute(utenteCorrente, new AsyncCallback<List<RichiestaScambio>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERRORE getRichiesteRicevute: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<RichiestaScambio> richiesteRicevute) {
                for (RichiestaScambio req : richiesteRicevute) {
                    if (req.getStato() == RichiestaScambio.StatoRichiesta.IN_ATTESA) {
                        Button btnRichiesta = creaBottoneSidebar("Proposta da: " + req.getRichiedenteUser());
                        btnRichiesta.getElement().getStyle().setProperty("border", "1px solid #ff9800");
                        btnRichiesta.addClickHandler(ev -> mostraDettagliPropostaRicevuta(req));
                        listaRichiesteAttesa.add(btnRichiesta);

                        // Recupera il titolo dell'annuncio e aggiorna il testo del bottone
                        skillService.getAnnuncioById(req.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                // il testo resta quello base, nessun problema
                            }

                            @Override
                            public void onSuccess(Annuncio annuncio) {
                                if (annuncio != null) {
                                    btnRichiesta.setText(annuncio.getTitolo() + " — da: " + req.getRichiedenteUser());
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void mostraDettagliPropostaRicevuta(RichiestaScambio req) {
        contentArea.setWidget(new Label("Caricamento dettagli in corso..."));

        skillService.getAnnuncioById(req.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERRORE: Impossibile recuperare l'annuncio.");
            }

            @Override
            public void onSuccess(Annuncio annuncio) {
                skillService.getUtenteById(req.getRichiedenteUser(), new AsyncCallback<Utente>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        contentArea.setWidget(new Label("Errore nel recupero dell'utente."));
                    }

                    @Override
                    public void onSuccess(Utente utente) {
                        disegnaCardPropostaRicevuta(req, annuncio, utente);
                    }
                });
            }
        });
    }

    private void disegnaCardPropostaRicevuta(RichiestaScambio req, Annuncio annuncio, Utente utente) {
        contentArea.clear();

        VerticalPanel card = creaCardBase();
        card.add(creaHeaderCard(annuncio.getTitolo(), utente.getUsername()));
        aggiungiDettagliAnnuncio(card, annuncio);

        Label lblProposta = new Label("CONTROPRESTAZIONE PROPOSTA: " + req.getMessaggioProposta());
        lblProposta.getElement().getStyle().setProperty("marginTop", "10px");
        lblProposta.getElement().getStyle().setProperty("fontWeight", "bold");
        lblProposta.getElement().getStyle().setProperty("color", "#000000");
        card.add(lblProposta);

        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        Button btnAccetta = new Button("Accetta");
        btnAccetta.getElement().getStyle().setProperty("backgroundColor", "#28a745");
        btnAccetta.getElement().getStyle().setProperty("backgroundImage", "none");
        btnAccetta.getElement().getStyle().setProperty("color", "#fff");
        btnAccetta.getElement().getStyle().setProperty("border", "none");

        Button btnRifiuta = new Button("Rifiuta");
        btnRifiuta.getElement().getStyle().setProperty("backgroundColor", "#dc3545");
        btnRifiuta.getElement().getStyle().setProperty("backgroundImage", "none");
        btnRifiuta.getElement().getStyle().setProperty("color", "#fff");
        btnRifiuta.getElement().getStyle().setProperty("border", "none");

        btnAccetta.addClickHandler(click -> {
            richiesteService.gestisciRispostaRichiesta(req.getId(), true,
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
            richiesteService.gestisciRispostaRichiesta(req.getId(), false,
                    new AsyncCallback<RichiestaScambio>() {
                        @Override
                        public void onSuccess(RichiestaScambio result) {
                            Window.alert("Scambio rifiutato.");
                            contentArea.clear();
                            aggiornaTuttiIDati();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore: " + caught.getMessage());
                        }
                    });
        });

        buttonGroups.add(btnAccetta);
        buttonGroups.add(btnRifiuta);

        card.add(creaButtonWrapper(buttonGroups));
        contentArea.add(card);
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

        VerticalPanel card = creaCardBase();
        card.add(creaHeaderCard(annuncio.getTitolo(), utente.getUsername()));
        aggiungiDettagliAnnuncio(card, annuncio);

        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        Button btnChat = new Button("💬");
        btnChat.getElement().setId("btn-chat-sidebar");

        final String interlocutoreChat = annuncio.getAutore().equals(utenteCorrente)
                ? richiesta.getRichiedenteUser()
                : annuncio.getAutore();

        btnChat.addClickHandler(event -> eseguiNavigazioneChat(interlocutoreChat));

        switch (richiesta.getStato()) {
            case IN_ATTESA:
                buttonGroups.add(btnChat);
                break;

            case ACCETTATO:
                Button btnConfermaScambio = new Button("✓");
                btnConfermaScambio.getElement().setId("btn-tick-conferma");
                Button btnSegnalaAnnullamento = new Button("X");
                btnSegnalaAnnullamento.getElement().setId("btn-x-rifiuto");

                boolean giaConfermatoDaMe = false;

                if (utenteCorrente.equals(richiesta.getProprietarioUser())) {
                    giaConfermatoDaMe = richiesta.isConfermatoDaProprietario();
                } else if (utenteCorrente.equals(richiesta.getRichiedenteUser())) {
                    giaConfermatoDaMe = richiesta.isConfermatoDaRichiedente();
                }

                if (giaConfermatoDaMe) {
                    btnConfermaScambio.setText("In attesa della controparte...");
                    btnConfermaScambio.setEnabled(false);
                }

                btnConfermaScambio.addClickHandler(event -> {
                    btnConfermaScambio.setEnabled(false);
                    richiesteService.elaboraAzioneScambio(richiesta.getId(), utenteCorrente, true,
                            new AsyncCallback<RichiestaScambio>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    btnConfermaScambio.setEnabled(true);
                                }

                                @Override
                                public void onSuccess(RichiestaScambio result) {
                                    if (result != null
                                            && result.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO) {
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
                        richiesteService.elaboraAzioneScambio(richiesta.getId(), utenteCorrente, false,
                                new AsyncCallback<RichiestaScambio>() {
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
                if (richiesta.isValutatoDaProprietario()) {
                    Button btnGiaValutato = new Button("Già Valutato");
                    btnGiaValutato.setEnabled(false);
                    buttonGroups.add(btnGiaValutato);
                } else {
                    Button btnValuta = new Button("Valuta");
                    btnValuta.addClickHandler(event -> apriPopupValutazione(annuncio, richiesta));
                    buttonGroups.add(btnValuta);
                }

                buttonGroups.add(btnChat);
                break;

            case RIFIUTATO:
                buttonGroups.add(btnChat);
                break;
        }

        card.add(creaButtonWrapper(buttonGroups));
        contentArea.add(card);
    }

    // --- HELPER DI COSTRUZIONE CARD (estratti per evitare duplicazione) ---

    private VerticalPanel creaCardBase() {
        VerticalPanel card = new VerticalPanel();
        card.setWidth("100%");
        card.getElement().getStyle().setProperty("border", "2px solid #000");
        card.getElement().getStyle().setProperty("padding", "20px");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");
        return card;
    }

    private void aggiungiDettagliAnnuncio(VerticalPanel card, Annuncio annuncio) {
        card.add(new Label("CATEGORIA: " + annuncio.getCategoria()));
        card.add(new Label("DETTAGLI OGGETTO: " + annuncio.getSkillOfferta()));
        card.add(new Label("DISPONIBILITÀ: " + annuncio.getDisponibilita()));
        card.add(new Label("CONTROPRESTAZIONE: " + annuncio.getControprestazione()));
    }

    private HorizontalPanel creaHeaderCard(String titolo, String username) {
        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");
        cardHeader.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblTitolo = new Label(titolo.toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");

        HorizontalPanel userPanel = new HorizontalPanel();

        Label lblRating = new Label("...");
        lblRating.getElement().getStyle().setProperty("fontSize", "18px");
        lblRating.getElement().getStyle().setProperty("fontWeight", "bold");
        lblRating.getElement().getStyle().setProperty("marginRight", "10px");

        skillService.getValutazioniUtente(username, new AsyncCallback<List<Valutazione>>() {
            @Override
            public void onFailure(Throwable caught) {
                lblRating.setText("N/A");
            }

            @Override
            public void onSuccess(List<Valutazione> valutazioni) {
                if (valutazioni == null || valutazioni.isEmpty()) {
                    lblRating.setText("N/A");
                } else {
                    double somma = 0;
                    for (Valutazione v : valutazioni) {
                        somma += v.getVoto();
                    }
                    double media = somma / valutazioni.size();
                    lblRating.setText(NumberFormat.getFormat("0.0").format(media));
                }
            }
        });

        Image imgProfilo = new Image();
        imgProfilo.setPixelSize(40, 40);
        imgProfilo.getElement().getStyle().setProperty("borderRadius", "50%");
        caricaImmagineProfilo(imgProfilo, username);

        userPanel.add(lblRating);
        userPanel.add(imgProfilo);
        userPanel.setCellVerticalAlignment(lblRating, HasVerticalAlignment.ALIGN_MIDDLE);

        VerticalPanel oggettiUtente = new VerticalPanel();
        Label usernameLabel = new Label(username);
        usernameLabel.getElement().getStyle().setProperty("fontWeight", "bold");

        oggettiUtente.add(userPanel);
        oggettiUtente.add(usernameLabel);
        oggettiUtente.setCellHorizontalAlignment(userPanel, HasHorizontalAlignment.ALIGN_CENTER);

        cardHeader.add(lblTitolo);
        cardHeader.add(oggettiUtente);
        cardHeader.setCellVerticalAlignment(lblTitolo, HasVerticalAlignment.ALIGN_MIDDLE);
        cardHeader.setCellHorizontalAlignment(oggettiUtente, HasHorizontalAlignment.ALIGN_RIGHT);

        return cardHeader;
    }

    private HorizontalPanel creaButtonWrapper(HorizontalPanel buttonGroups) {
        HorizontalPanel wrapper = new HorizontalPanel();
        wrapper.setWidth("100%");
        wrapper.add(buttonGroups);
        wrapper.setCellHorizontalAlignment(buttonGroups, HasHorizontalAlignment.ALIGN_RIGHT);
        return wrapper;
    }

    private void mostraDettagliAnnuncio(Annuncio skill) {
        contentArea.clear();

        VerticalPanel card = creaCardBase();

        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");

        Label lblTitolo = new Label(skill.getTitolo().toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");
        cardHeader.add(lblTitolo);

        card.add(cardHeader);
        aggiungiDettagliAnnuncio(card, skill);

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
                if (txtTitolo.getText().trim().isEmpty() || txtDesc.getText().trim().isEmpty()
                        || txtDisp.getText().trim().isEmpty()) {
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
                        Window.alert("Errore di rete durante la modifica: " + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            Window.alert("Annuncio aggiornato con successo!");
                            aggiornaTuttiIDati();
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

        card.add(creaButtonWrapper(buttonGroups));
        contentArea.add(card);
    }

    public void apriPopupValutazione(Annuncio skill, RichiestaScambio richiesta) {
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
                    .destinatario(richiesta.getRichiedenteUser())
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

                        skillService.impostaRichiestaValutata(richiesta.getId(), true, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(Void result) {
                                contentArea.clear();
                                aggiornaTuttiIDati();
                            }
                        });

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

        if (utenteLoggato == null || utenteLoggato.isEmpty()) {
            utenteLoggato = "admin";
        }

        if (utenteLoggato.equals(interlocutore)) {
            interlocutore = "UtenteScambio_1";
        }

        ChatGui vistaChat = new ChatGui();
        mainLayout.cambiaVista(vistaChat);
        vistaChat.apriConversazione(interlocutore);
    }
}