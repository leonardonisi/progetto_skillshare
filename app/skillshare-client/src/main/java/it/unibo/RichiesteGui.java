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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Window;

public class RichiesteGui extends Composite {

    private VerticalPanel mainPanel = new VerticalPanel();
    private SimplePanel contentArea = new SimplePanel();

    private VerticalPanel listaRichieste = new VerticalPanel();
    private VerticalPanel listaAccettate = new VerticalPanel();
    private VerticalPanel listaRifiutate = new VerticalPanel();
    private VerticalPanel listaConcluse = new VerticalPanel();

    private RichiesteServiceAsync richiesteService = GWT.create(RichiesteService.class);
    private SkillServiceAsync skillService = GWT.create(SkillService.class);
    private MainLayoutGui mainLayout;

    public RichiesteGui(MainLayoutGui mainLayout) {
        this.mainLayout = mainLayout;
        initWidget(mainPanel);
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);

        HorizontalPanel splitLayout = new HorizontalPanel();
        splitLayout.setWidth("100%");
        splitLayout.setSpacing(20);

        // SIDEBAR
        VerticalPanel sidebar = new VerticalPanel();
        sidebar.setWidth("300px");

        // Tendina "Skills Richieste"
        DisclosurePanel discRichieste = new DisclosurePanel("Skills Richieste");
        discRichieste.getElement().setId("sidebar-skills-richieste");
        discRichieste.setOpen(true);
        listaRichieste.setSpacing(5);
        listaRichieste.setWidth("100%");
        discRichieste.setContent(listaRichieste);

        // Tendina "Skills Accettate"
        DisclosurePanel discAccettate = new DisclosurePanel("Skills Accettate");
        discAccettate.getElement().setId("sidebar-skills-accettate");
        listaAccettate.setSpacing(5);
        listaAccettate.setWidth("100%");
        discAccettate.setContent(listaAccettate);

        // Tendina "Skills Rifiutate"
        DisclosurePanel discRifiutate = new DisclosurePanel("Skills Rifiutate");
        discRifiutate.getElement().setId("sidebar-skills-rifiutate");
        listaRifiutate.setSpacing(5);
        listaRifiutate.setWidth("100%");
        discRifiutate.setContent(listaRifiutate);

        // Tendina "Skills Concluse"
        DisclosurePanel discConcluse = new DisclosurePanel("Skills Concluse");
        discConcluse.getElement().setId("sidebar-skills-concluse");
        listaConcluse.setSpacing(5);
        listaConcluse.setWidth("100%");
        discConcluse.setContent(listaConcluse);

        sidebar.add(discRichieste);
        sidebar.add(discAccettate);
        sidebar.add(discRifiutate);
        sidebar.add(discConcluse);

        // AREA CONTENUTO DELLA TASK
        contentArea.setWidth("100%");

        splitLayout.add(sidebar);
        splitLayout.add(contentArea);

        // Allineamento
        splitLayout.setCellVerticalAlignment(sidebar, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellVerticalAlignment(contentArea, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellWidth(contentArea, "100%");

        mainPanel.add(splitLayout);

        caricaRichiesteDalDatabase();
    }

    private void caricaRichiesteDalDatabase() {
        richiesteService.getMieRichieste("admin", new AsyncCallback<List<Annuncio>>() {
            @Override
            public void onFailure(Throwable caught) {
                contentArea.setWidget(new Label("Errore di rete: Impossibile caricare le richieste."));
            }

            @Override
            public void onSuccess(List<Annuncio> skillsDalDb) {
                listaRichieste.clear();
                listaAccettate.clear();
                listaRifiutate.clear();
                listaConcluse.clear();

                for (Annuncio skill : skillsDalDb) {
                    Button btnSkill = new Button(skill.getTitolo());
                    btnSkill.setWidth("100%");
                    btnSkill.getElement().getStyle().setProperty("textAlign", "left");
                    btnSkill.getElement().getStyle().setProperty("padding", "10px");
                    btnSkill.getElement().getStyle().setProperty("backgroundColor", "#fff");
                    btnSkill.getElement().getStyle().setProperty("border", "1px solid #000");

                    btnSkill.addClickHandler(event -> mostraDettagliCard(skill));

                    // Simulazione degli stati in base ai titoli per testare la grafica delle 4
                    // tendine
                    String statoSimulato = "RICHIESTA";
                    if (skill.getTitolo().equals("Programmazione Java"))
                        statoSimulato = "ACCETTATA";
                    if (skill.getTitolo().equals("Cucina Pollo"))
                        statoSimulato = "RIFIUTATA";
                    if (skill.getTitolo().equals("Allenamento Tennis"))
                        statoSimulato = "CONCLUSA";

                    switch (statoSimulato) {
                        case "RICHIESTA":
                            listaRichieste.add(btnSkill);
                            break;
                        case "ACCETTATA":
                            listaAccettate.add(btnSkill);
                            break;
                        case "RIFIUTATA":
                            listaRifiutate.add(btnSkill);
                            break;
                        case "CONCLUSA":
                            listaConcluse.add(btnSkill);
                            break;
                    }
                }
            }
        });
    }

    private void mostraDettagliCard(Annuncio skill) {
        contentArea.clear();

        VerticalPanel card = new VerticalPanel();
        card.setWidth("100%");
        card.getElement().getStyle().setProperty("border", "2px solid #000");
        card.getElement().getStyle().setProperty("padding", "20px");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

        // --- HEADER DELLA CARD ---
        HorizontalPanel cardHeader = new HorizontalPanel();
        cardHeader.setWidth("100%");
        cardHeader.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblTitolo = new Label(skill.getTitolo().toUpperCase());
        lblTitolo.getElement().getStyle().setProperty("fontWeight", "bold");
        lblTitolo.getElement().getStyle().setProperty("fontSize", "22px");

        Label lblRating = new Label("👤 4.9");
        lblRating.getElement().getStyle().setProperty("fontSize", "18px");
        lblRating.getElement().getStyle().setProperty("fontWeight", "bold");

        cardHeader.add(lblTitolo);
        cardHeader.add(lblRating);
        cardHeader.setCellHorizontalAlignment(lblRating, HasHorizontalAlignment.ALIGN_RIGHT);
        card.add(cardHeader);

        // --- DETTAGLI CARD ---
        Label lblCat = new Label("CATEGORIA: " + skill.getCategoria());
        lblCat.getElement().getStyle().setProperty("marginBottom", "10px");
        card.add(lblCat);

        Label lblOgg = new Label("DETTAGLI OGGETTO: " + skill.getSkillOfferta());
        lblOgg.getElement().getStyle().setProperty("marginBottom", "10px");
        card.add(lblOgg);

        Label lblDisp = new Label("DISPONIBILITÀ: " + skill.getDisponibilita());
        lblDisp.getElement().getStyle().setProperty("marginBottom", "10px");
        card.add(lblDisp);

        Label lblContro = new Label("CONTROPRESTAZIONE OFFERTA: " + skill.getControprestazione());
        lblContro.getElement().getStyle().setProperty("marginBottom", "20px");
        card.add(lblContro);

        // --- BOTTONI DINAMICI ---
        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");

        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        String statoSimulato = "RICHIESTA";
        if (skill.getTitolo().equals("Programmazione Java"))
            statoSimulato = "ACCETTATA";
        if (skill.getTitolo().equals("Cucina Pollo"))
            statoSimulato = "RIFIUTATA";
        if (skill.getTitolo().equals("Allenamento Tennis"))
            statoSimulato = "CONCLUSA";

        Button btnChat = new Button("💬");
        btnChat.getElement().getStyle().setProperty("backgroundColor", "#007bff");
        btnChat.getElement().getStyle().setProperty("color", "#fff");
        btnChat.addClickHandler(event -> eseguiNavigazioneChat(skill.getAutore()));

        if (statoSimulato.equals("RICHIESTA")) {
            buttonGroups.add(btnChat);
        } else if (statoSimulato.equals("ACCETTATA")) {
            Button btnTick = new Button("✓");
            Button btnX = new Button("X");

            btnTick.getElement().setId("btn-tick-conferma");
            btnX.getElement().setId("btn-x-rifiuto");

            btnTick.addClickHandler(event -> {
                btnTick.setEnabled(false);
                String utenteAttuale = SessionManager.getUtenteLoggato();

                richiesteService.elaboraAzioneScambio(skill.getId(), utenteAttuale, true,
                        new AsyncCallback<RichiestaScambio>() {
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
                    richiesteService.elaboraAzioneScambio(skill.getId(), utenteAttuale, false,
                            new AsyncCallback<RichiestaScambio>() {
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
        } else if (statoSimulato.equals("RIFIUTATA")) {
        } else if (statoSimulato.equals("CONCLUSA")) {
            buttonGroups.add(btnChat);
            Button btnValuta = new Button("Valuta");
            btnValuta.addClickHandler(event -> {
                SkillsGui.apriPopupValutazione(skill, skillService);
            });
            buttonGroups.add(btnValuta);
        }

        buttonWrapper.add(buttonGroups);
        buttonWrapper.setCellHorizontalAlignment(buttonGroups, HasHorizontalAlignment.ALIGN_RIGHT);

        card.add(buttonWrapper);
        contentArea.add(card);
    }

    private void eseguiNavigazioneChat(String interlocutore) {
        String utenteLoggato = SessionManager.getUtenteLoggato();

        if (utenteLoggato == null || utenteLoggato.isEmpty()) {
            utenteLoggato = "utente_test";
        }

        // Evitiamo l'auto-chat se l'annuncio è il nostro
        if (utenteLoggato.equals(interlocutore)) {
            interlocutore = "UtenteScambio_1";
        }

        ChatGui vistaChat = new ChatGui();
        mainLayout.cambiaVista(vistaChat);

        vistaChat.apriConversazione(interlocutore);
    }
}
