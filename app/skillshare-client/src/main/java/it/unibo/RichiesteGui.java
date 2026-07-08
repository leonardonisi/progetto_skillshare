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
import com.google.gwt.user.client.Window;

public class RichiesteGui extends Composite {

    private String utenteCorrente;
    private Image imgProfilo;

    private VerticalPanel mainPanel = new VerticalPanel();
    private SimplePanel contentArea = new SimplePanel();

    // Contenitori interni che si popoleranno con i dati delle skill
    private VerticalPanel listaRichieste = new VerticalPanel();
    private VerticalPanel listaAccettate = new VerticalPanel();
    private VerticalPanel listaRifiutate = new VerticalPanel();
    private VerticalPanel listaConcluse = new VerticalPanel();

    private RichiesteServiceAsync richiesteService = GWT.create(RichiesteService.class);
    private MarketServiceAsync marketService = GWT.create(MarketService.class);
    private SkillServiceAsync skillService = GWT.create(SkillService.class);

    public RichiesteGui() {
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

        DisclosurePanel discMieRichieste = new DisclosurePanel("Mie Richieste");
        discMieRichieste.getElement().setId("sidebar-mie-skills");
        discMieRichieste.setOpen(true);
        listaRichieste.setWidth("100%");
        discMieRichieste.setContent(listaRichieste);

        DisclosurePanel discRichiesteAccettate = new DisclosurePanel("Richieste Accettate");
        discRichiesteAccettate.getElement().setId("sidebar-richieste-attesa");
        discRichiesteAccettate.setOpen(true);
        listaAccettate.setWidth("100%");
        discRichiesteAccettate.setContent(listaAccettate);

        DisclosurePanel discRichiesteRifiutate = new DisclosurePanel("Richieste Rifiutate");
        discRichiesteRifiutate.getElement().setId("sidebar-skills-accettate");
        listaRifiutate.setWidth("100%");
        discRichiesteRifiutate.setContent(listaRifiutate);

        DisclosurePanel discConcluse = new DisclosurePanel("Richieste Concluse");
        discConcluse.getElement().setId("sidebar-skills-concluse");
        listaConcluse.setWidth("100%");
        discConcluse.setContent(listaConcluse);

        sidebar.add(discMieRichieste);
        sidebar.add(discRichiesteAccettate);
        sidebar.add(discRichiesteRifiutate);
        sidebar.add(discConcluse);

        contentArea.setWidth("100%");
        contentArea.setWidget(new Label("Seleziona una richiesta per vedere i dettagli."));

        splitLayout.add(sidebar);
        splitLayout.add(contentArea);

        splitLayout.setCellVerticalAlignment(sidebar, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellVerticalAlignment(contentArea, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellWidth(contentArea, "100%");

        mainPanel.add(splitLayout);

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

    private void getRichiesteScambio() {
        richiesteService.getRichiesteScambio(utenteCorrente, new AsyncCallback<List<RichiestaScambio>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Errore di rete: Impossibile caricare le richieste di skill.");
            }

            @Override
            public void onSuccess(List<RichiestaScambio> richiesteScambio) {
                listaRichieste.clear();
                listaAccettate.clear();
                listaRifiutate.clear();
                listaConcluse.clear();

                for (RichiestaScambio r : richiesteScambio) {
                    Button btnSkill = creaBottoneSidebar("Caricamento...");

                    // Chiamata diretta per ottenere il titolo dell'annuncio
                    richiesteService.getAnnuncioById(r.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
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
                            listaRichieste.add(btnSkill);
                            break;
                        case ACCETTATO:
                            listaAccettate.add(btnSkill);
                            break;
                        case RIFIUTATO:
                            listaRifiutate.add(btnSkill);
                            break;
                        case CONCLUSO:
                            listaConcluse.add(btnSkill);
                            break;
                    }
                }
            }
        });
    }

    private void mostraDettagliRichiestaScambio(RichiestaScambio richiesta) {
        contentArea.setWidget(new Label("Caricamento dettagli in corso..."));

        // Chiamata asincrona inlined per l'annuncio
        richiesteService.getAnnuncioById(richiesta.getIdAnnuncio(), new AsyncCallback<Annuncio>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("ERRORE RPC: " + caught.toString());
                GWT.log("Errore completo:", caught); // Controlla la console del browser (F12)
            }

            @Override
            public void onSuccess(Annuncio annuncio) {
                // Chiamata asincrona inlined per l'utente
                richiesteService.getUtenteById(richiesta.getProprietarioUser(), new AsyncCallback<Utente>() {
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
        caricaImmagineProfilo(imgProfilo, richiesta.getProprietarioUser());

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

<<<<<<< HEAD
        switch(richiesta.getStato()) {
            case IN_ATTESA:
                Button btnAccettaScambio = new Button("Accetta Richiesta");
                buttonGroups.add(btnAccettaScambio);
                buttonGroups.add(btnChat);
                break;

            case ACCETTATO:
                Button btnConfermaScambio = new Button("✓");
                Button btnSegnalaScambioNonAvvenuto = new Button("X");
                buttonGroups.add(btnConfermaScambio);
                buttonGroups.add(btnSegnalaScambioNonAvvenuto);
                buttonGroups.add(btnChat);
                Button btnTick = new Button("✓");
                Button btnX = new Button("X");

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
                            if (result != null && result.getStato() == RichiestaScambio.StatoRichiesta.CONCLUSO) {
                                Window.alert("Scambio concluso con successo! Entrambi avete confermato.");
                                contentArea.clear();
                                caricaRichiesteDalDatabase();
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
                                caricaRichiesteDalDatabase();
                            }
                        });
                    }
                });

                buttonGroups.add(btnTick);
                buttonGroups.add(btnX);
                buttonGroups.add(btnChat);
                break;

            case RIFIUTATO:
                buttonGroups.add(btnChat);
                break;
            
            case CONCLUSO:
                Button btnValuta = new Button("Valuta");
                buttonGroups.add(btnValuta);
                buttonGroups.add(btnChat);
                buttonGroups.add(btnChat);
                Button btnValuta = new Button("Valuta");
                btnValuta.addClickHandler(event -> {
                    SkillsGui.apriPopupValutazione(skill, skillService);
                });
                buttonGroups.add(btnValuta);
                break;

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