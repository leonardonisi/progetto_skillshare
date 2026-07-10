package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.i18n.client.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MarketGui extends Composite {

    private final MarketServiceAsync servizio = GWT.create(MarketService.class);
    private SkillServiceAsync skillService = GWT.create(SkillService.class);
    private final RichiesteServiceAsync richiesteService = GWT.create(RichiesteService.class);
    private String utenteCorrente;

    // Componenti della vista Marketplace
    private VerticalPanel colonnaSinistra;
    private VerticalPanel colonnaDestra;

    // Elementi del dettaglio annuncio
    private Label titoloDettaglio;
    private Button btnRichiedi;
    private Button btnChat;
    private Label votoDettaglio;
    private Label lblCategoria;
    private Label lblDescrizione;
    private Label lblDispo;
    private Label lblContro;
    private Image imgAnnuncio;
    private List<Annuncio> tuttiGliAnnunci;
    private Annuncio annuncioSelezionato;
    private MainLayoutGui mainLayout;

    public MarketGui(MainLayoutGui mainLayout) {
        this.utenteCorrente = SessionManager.getUtenteLoggato();
        this.mainLayout = mainLayout;

        VerticalPanel vistaMarket = new VerticalPanel();
        vistaMarket.setWidth("80%");
        vistaMarket.getElement().getStyle().setProperty("margin", "0 auto");

        // Contenitore Filtraggio, Ricerca e Pubblica
        HorizontalPanel searchBar = new HorizontalPanel();
        searchBar.setWidth("100%");
        searchBar.setSpacing(10);
        searchBar.getElement().getStyle().setProperty("marginBottom", "40px");
        searchBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        // Elenco per filtrare Categorie
        ListBox tendinaCategorie = new ListBox();
        tendinaCategorie.setHeight("47px");
        tendinaCategorie.getElement().setId("tendina-categorie");
        tendinaCategorie.getElement().getStyle().setProperty("fontSize", "14px");
        tendinaCategorie.getElement().getStyle().setProperty("padding", "5px");
        tendinaCategorie.getElement().getStyle().setProperty("cursor", "pointer");

        // aggiunta delle categorie
        servizio.getCategorie(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                tendinaCategorie.clear();
                tendinaCategorie.addItem("Errore caricamento");
                Window.alert("Impossibile caricare le categorie: " + caught.getMessage());
            }

            @Override
            public void onSuccess(List<String> result) {
                tendinaCategorie.clear();
                tendinaCategorie.addItem("Tutte le Categorie");
                for (String categoria : result) {
                    tendinaCategorie.addItem(categoria);
                }
            }
        });

        // logica di filtraggio
        tendinaCategorie.addChangeHandler(event -> {
            String categoriaScelta = tendinaCategorie.getSelectedItemText();
            List<Annuncio> annunciFiltrati = new ArrayList<>();

            if (tuttiGliAnnunci != null) {
                if (categoriaScelta.equals("Tutte le Categorie") || categoriaScelta.equals("Errore caricamento")) {
                    annunciFiltrati.addAll(tuttiGliAnnunci);
                } else {
                    for (Annuncio a : tuttiGliAnnunci) {
                        if (a.getCategoria().equals(categoriaScelta)) {
                            annunciFiltrati.add(a);
                        }
                    }
                }
                aggiornaVistaAnnunci(annunciFiltrati);
            }
        });

        SimplePanel divisorio1 = creaDivisorio();

        // Barra di Ricerca
        TextBox searchBox = new TextBox();
        searchBox.getElement().setPropertyString("placeholder", "Cerca...");
        searchBox.setWidth("500px");
        searchBox.setHeight("35px");
        searchBox.getElement().setId("search-bar");
        searchBox.getElement().getStyle().setProperty("fontSize", "16px");
        searchBox.getElement().getStyle().setProperty("padding", "5px 15px");

        // logica di ricerca in tempo reale
        searchBox.addKeyUpHandler(event -> {
            String ricercaEffettuata = searchBox.getText().trim().toLowerCase();
            List<Annuncio> annunciFiltrati = new ArrayList<>();

            if (tuttiGliAnnunci != null) {
                if (ricercaEffettuata.isEmpty()) {
                    annunciFiltrati.addAll(tuttiGliAnnunci);
                } else {
                    for (Annuncio a : tuttiGliAnnunci) {
                        String titolo = a.getTitolo().toLowerCase();
                        String descrizione = a.getSkillOfferta().toLowerCase();
                        if (titolo.contains(ricercaEffettuata) || descrizione.contains(ricercaEffettuata)) {
                            annunciFiltrati.add(a);
                        }
                    }
                }
                aggiornaVistaAnnunci(annunciFiltrati);
            }
        });

        SimplePanel divisorio2 = creaDivisorio();

        // Bottone Pubblica
        Button btnPubblica = new Button("PUBBLICA");
        btnPubblica.setHeight("47px");
        btnPubblica.setWidth("180px");
        btnPubblica.getElement().setId("btn-pubblica");
        btnPubblica.getElement().getStyle().setProperty("fontSize", "14px");
        btnPubblica.getElement().getStyle().setProperty("cursor", "pointer");
        btnPubblica.getElement().getStyle().setProperty("fontWeight", "bold");
        btnPubblica.getElement().getStyle().setProperty("border", "none");
        btnPubblica.getElement().getStyle().setProperty("background", "#007BFF");
        btnPubblica.getElement().getStyle().setProperty("color", "#FFFFFF");

        btnPubblica.addClickHandler(event -> new CreateAdGui().mostra());

        searchBar.add(tendinaCategorie);
        searchBar.add(divisorio1);
        searchBar.add(searchBox);
        searchBar.add(divisorio2);
        searchBar.add(btnPubblica);

        // Area Contenuto Dinamico
        HorizontalPanel contentArea = new HorizontalPanel();
        contentArea.setWidth("100%");
        contentArea.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        colonnaSinistra = new VerticalPanel();
        colonnaSinistra.setWidth("100%");

        SimplePanel spacer = new SimplePanel();
        spacer.setWidth("100%");

        colonnaDestra = new VerticalPanel();
        colonnaDestra.setWidth("100%");
        colonnaDestra.getElement().getStyle().setProperty("border", "1px solid #666");
        colonnaDestra.getElement().getStyle().setProperty("padding", "30px");
        colonnaDestra.getElement().getStyle().setProperty("backgroundColor", "#ffffff");
        colonnaDestra.getElement().getStyle().setProperty("minHeight", "380px");

        // Header del dettaglio (Titolo + Voto)
        HorizontalPanel headerDettaglio = new HorizontalPanel();
        headerDettaglio.setWidth("100%");
        headerDettaglio.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        headerDettaglio.getElement().getStyle().setProperty("marginBottom", "30px");

        titoloDettaglio = new Label("Seleziona un annuncio");
        titoloDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");
        titoloDettaglio.getElement().getStyle().setProperty("fontSize", "28px");

        imgAnnuncio = new Image();
        imgAnnuncio.setPixelSize(40, 40);
        imgAnnuncio.getElement().getStyle().setProperty("borderRadius", "50%");
        imgAnnuncio.getElement().getStyle().setProperty("objectFit", "cover");
        imgAnnuncio.getElement().getStyle().setProperty("border", "2px solid #007BFF");
        imgAnnuncio.getElement().getStyle().setProperty("marginLeft", "10px");
        imgAnnuncio.setVisible(false);

        votoDettaglio = new Label("");
        votoDettaglio.getElement().getStyle().setProperty("fontSize", "22px");
        votoDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");

        headerDettaglio.add(titoloDettaglio);
        headerDettaglio.add(votoDettaglio);
        headerDettaglio.add(imgAnnuncio);
        headerDettaglio.setCellHorizontalAlignment(votoDettaglio, HasHorizontalAlignment.ALIGN_RIGHT);
        headerDettaglio.setCellHorizontalAlignment(imgAnnuncio, HasHorizontalAlignment.ALIGN_RIGHT);
        headerDettaglio.setCellWidth(titoloDettaglio, "100%");

        // Label Dettaglio dei dettagli strutturali
        lblCategoria = creaDettaglioLabel("16px", "20px");
        lblCategoria.getElement().setId("lbl-categoria");

        lblDescrizione = creaDettaglioLabel("16px", "20px");
        lblDispo = creaDettaglioLabel("16px", "20px");
        lblContro = creaDettaglioLabel("16px", "40px");

        // Contenitore Bottoni Richiedi e Chat
        FlowPanel btnContainer = new FlowPanel();
        btnContainer.setWidth("100%");
        btnContainer.getElement().getStyle().setProperty("display", "flex");
        btnContainer.getElement().getStyle().setProperty("justifyContent", "flex-end");

        // Bottone Dettaglio Richiedi
        btnRichiedi = new Button("RICHIEDI");
        btnRichiedi.setHeight("40px");
        btnRichiedi.setWidth("130px");
        btnRichiedi.getElement().getStyle().setProperty("fontSize", "14px");
        btnRichiedi.getElement().getStyle().setProperty("cursor", "pointer");
        btnRichiedi.getElement().getStyle().setProperty("fontWeight", "bold");
        btnRichiedi.getElement().getStyle().setProperty("background", "#333333");
        btnRichiedi.getElement().getStyle().setProperty("color", "white");
        btnRichiedi.getElement().getStyle().setProperty("border", "none");
        btnRichiedi.getElement().getStyle().setProperty("marginRight", "10px");
        btnRichiedi.setVisible(false);

        btnRichiedi.addClickHandler(event -> {
            if (annuncioSelezionato != null) {
                gestisciClickRichiesta();
            }
        });

        // Bottone Dettaglio Chat
        btnChat = new Button("💬");
        btnChat.setHeight("40px");
        btnChat.setWidth("40px");
        btnChat.getElement().getStyle().setProperty("background", "#007BFF");
        btnChat.getElement().getStyle().setProperty("color", "white");
        btnChat.getElement().getStyle().setProperty("border", "none");
        btnChat.getElement().getStyle().setProperty("cursor", "pointer");
        btnChat.getElement().getStyle().setProperty("fontSize", "20px");

        btnChat.setVisible(false);
        btnChat.addClickHandler(event -> {
            mainLayout.cambiaVista(new ChatGui());
        });

        btnContainer.add(btnRichiedi);
        btnContainer.add(btnChat);

        // Assemblaggio Colonna Destra
        colonnaDestra.add(headerDettaglio);
        colonnaDestra.add(lblCategoria);
        colonnaDestra.add(lblDescrizione);
        colonnaDestra.add(lblDispo);
        colonnaDestra.add(lblContro);
        colonnaDestra.add(btnContainer);

        // Assemblaggio finale dell'area contenuto
        contentArea.add(colonnaSinistra);
        contentArea.add(spacer);
        contentArea.add(colonnaDestra);
        contentArea.setCellWidth(colonnaSinistra, "45%");
        contentArea.setCellWidth(spacer, "5%");
        contentArea.setCellWidth(colonnaDestra, "50%");

        vistaMarket.add(searchBar);
        vistaMarket.add(contentArea);

        caricaAnnunci();
        initWidget(vistaMarket);
    }

    // --- METODI DI SUPPORTO ---

    private void gestisciClickRichiesta() {
        DialogBox popupOfferta = new DialogBox();
        popupOfferta.setText("Formula la tua proposta di scambio");
        popupOfferta.setGlassEnabled(true);
        popupOfferta.setAnimationEnabled(true);

        VerticalPanel layoutPopup = new VerticalPanel();
        layoutPopup.setSpacing(10);
        layoutPopup.add(new Label("Cosa offri in controprestazione per: " + annuncioSelezionato.getTitolo() + "?"));

        TextArea inputControprestazione = new TextArea();
        inputControprestazione.setWidth("350px");
        inputControprestazione.setVisibleLines(4);
        layoutPopup.add(inputControprestazione);

        Button btnInviaProposta = new Button("Invia Richiesta");
        Button btnAnnullaProposta = new Button("Annulla");

        btnAnnullaProposta.addClickHandler(e -> popupOfferta.hide());

        btnInviaProposta.addClickHandler(e -> {
            String proposta = inputControprestazione.getText().trim();
            if (proposta.isEmpty()) {
                Window.alert("Il messaggio non può essere vuoto!");
                return;
            }

            richiesteService.inviaRichiesta(annuncioSelezionato.getId(), utenteCorrente,
                    annuncioSelezionato.getAutore(), proposta, new AsyncCallback<RichiestaScambio>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore imprevisto: " + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(RichiestaScambio result) {
                            if (result == null) {
                                Window.alert("Attenzione: Hai già inoltrato una richiesta per questo annuncio!");
                                btnRichiedi.setVisible(false);
                            } else {
                                Window.alert("Proposta inviata!");
                                popupOfferta.hide();
                            }
                        }
                    });
        });

        HorizontalPanel bottoniPopup = new HorizontalPanel();
        bottoniPopup.add(btnInviaProposta);
        bottoniPopup.add(btnAnnullaProposta);
        layoutPopup.add(bottoniPopup);
        popupOfferta.setWidget(layoutPopup);
        popupOfferta.center();
    }

    private SimplePanel creaDivisorio() {
        SimplePanel divisorio = new SimplePanel();
        divisorio.setPixelSize(2, 47);
        divisorio.getElement().getStyle().setProperty("backgroundColor", "#000000");
        divisorio.getElement().getStyle().setProperty("marginLeft", "5px");
        divisorio.getElement().getStyle().setProperty("marginRight", "5px");
        return divisorio;
    }

    private Label creaDettaglioLabel(String fontSize, String marginBottom) {
        Label lbl = new Label();
        lbl.getElement().getStyle().setProperty("fontSize", fontSize);
        lbl.getElement().getStyle().setProperty("marginBottom", marginBottom);
        return lbl;
    }

    private FocusPanel creaCard(Annuncio a) {
        FocusPanel card = new FocusPanel();
        card.setWidth("100%");
        card.getElement().setId("card-annuncio-" + a.getId());
        card.addStyleName("card-annuncio-class");
        card.getElement().getStyle().setProperty("border", "1px solid #666");
        card.getElement().getStyle().setProperty("marginBottom", "15px");
        card.getElement().getStyle().setProperty("cursor", "pointer");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

        VerticalPanel cardContent = new VerticalPanel();
        cardContent.setWidth("100%");
        cardContent.setHeight("80px");
        cardContent.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        cardContent.getElement().getStyle().setProperty("padding", "0 20px");

        Label lblTitolo = new Label(a.getTitolo());
        lblTitolo.getElement().getStyle().setProperty("fontSize", "20px");

        cardContent.add(lblTitolo);
        card.add(cardContent);
        card.addClickHandler(event -> mostraDettaglio(a));

        return card;
    }

    private void caricaAnnunci() {
        servizio.getAnnunci(utenteCorrente, new AsyncCallback<List<Annuncio>>() {
            @Override
            public void onFailure(Throwable caught) {
                titoloDettaglio.setText("Errore nel caricamento degli annunci.");
            }

            @Override
            public void onSuccess(List<Annuncio> result) {
                tuttiGliAnnunci = result;
                aggiornaVistaAnnunci(tuttiGliAnnunci);
            }
        });
    }

    private void aggiornaVistaAnnunci(List<Annuncio> annunciDaMostrare) {
        colonnaSinistra.clear();

        Label messaggio = new Label("Annunci Disponibili: ");
        messaggio.getElement().getStyle().setProperty("fontWeight", "bold");
        messaggio.getElement().getStyle().setProperty("fontSize", "28px");
        messaggio.getElement().getStyle().setProperty("marginBottom", "20px");

        colonnaSinistra.add(messaggio);

        if (annunciDaMostrare == null || annunciDaMostrare.isEmpty()) {
            Label alert = new Label("Nessun annuncio trovato");
            alert.getElement().setId("alert-filtraggio-categorie");
            alert.getElement().getStyle().setProperty("fontSize", "22px");
            colonnaSinistra.add(alert);
            return;
        }

        for (Annuncio a : annunciDaMostrare) {
            colonnaSinistra.add(creaCard(a));
        }
    }

    private void mostraDettaglio(Annuncio a) {
        annuncioSelezionato = a;

        titoloDettaglio.setText(a.getTitolo());
        imgAnnuncio.setVisible(true);
        caricaImmagineProfilo(imgAnnuncio, a.getAutore());

        skillService.getValutazioniUtente(a.getAutore(), new AsyncCallback<List<Valutazione>>() {
            @Override
            public void onFailure(Throwable caught) {
                votoDettaglio.setText("N/A");
            }

            @Override
            public void onSuccess(List<Valutazione> valutazioni) {
                if (valutazioni == null || valutazioni.isEmpty()) {
                    votoDettaglio.setText("N/A");
                } else {
                    double somma = 0;
                    for (Valutazione v : valutazioni) {
                        somma += v.getVoto();
                    }
                    double media = somma / valutazioni.size();
                    votoDettaglio.setText(NumberFormat.getFormat("0.0").format(media));
                }
            }
        });

        lblCategoria.setText("CATEGORIA: " + a.getCategoria());
        lblDescrizione.setText("DESCRIZIONE: " + a.getSkillOfferta());
        lblDispo.setText("DISPONIBILITÀ: " + a.getDisponibilita());
        lblContro.setText("CONTROPRESTAZIONE: " + a.getControprestazione());

        btnRichiedi.setVisible(true);
        btnChat.setVisible(true);
    }

    private void caricaImmagineProfilo(Image imgProfilo, String username) {
        servizio.getUtente(username, new AsyncCallback<Utente>() {
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