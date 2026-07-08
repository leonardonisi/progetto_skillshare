package it.unibo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainLayoutGui extends Composite {

    // Servizi e Contenitori Principali
    private SimplePanel contenitoreDinamico;
    private final MarketServiceAsync servizio = GWT.create(MarketService.class);

    // Componenti della vista Marketplace
    VerticalPanel colonnaSinistra;
    VerticalPanel colonnaDestra;

    // Elementi del dettaglio annuncio che cambieranno dinamicamente
    private String utenteCorrente;
    private Label titoloDettaglio;
    private Button btnRichiedi;
    private Button btnChat;
    private Label votoDettaglio;
    private Label lblCategoria;
    private Label lblDescrizione;
    private Label lblDispo;
    private Label lblContro;
    private Image imgProfilo;
    private List<Annuncio> tuttiGliAnnunci;
    private Annuncio annuncioSelezionato;

    public void mostra() {
        RootPanel.get().clear();
        RootPanel.get().add(this);
    }

    public MainLayoutGui() {
        this.utenteCorrente = SessionManager.getUtenteLoggato();

        // inizializzazione layout principale (Header Fisso + Contenitore Dinamico)
        VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.setWidth("100%");
        mainContainer.setHeight("100%");
        mainContainer.getElement().getStyle().setProperty("padding", "20px");

        // inizializzazione header
        HorizontalPanel header = new HorizontalPanel();
        header.setWidth("100%");
        header.setHeight("40px");
        header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        header.getElement().getStyle().setProperty("borderBottom", "2px solid #ccc");
        header.getElement().getStyle().setProperty("marginBottom", "30px");

        HorizontalPanel logoBenvenuto = new HorizontalPanel();
        logoBenvenuto.setSpacing(30);

        // titolo pagina
        Label logoLabel = new com.google.gwt.user.client.ui.Label("SKILLSHARE");
        logoLabel.getElement().getStyle().setProperty("fontWeight", "bold");
        logoLabel.getElement().getStyle().setProperty("fontSize", "22px");
        logoLabel.getElement().getStyle().setProperty("color", "#007BFF");
        logoLabel.getElement().setId("titolo-home");

        Label lblBenvenuto = new Label("Ciao, " + utenteCorrente);
        lblBenvenuto.getElement().getStyle().setProperty("fontSize", "22px");
        lblBenvenuto.getElement().getStyle().setProperty("whiteSpace", "nowrap");
        lblBenvenuto.getElement().setId("benvenuto-utente");

        // Link di navigazione
        HorizontalPanel navLinks = new HorizontalPanel();

        Label lblMarket = new Label("MARKET");
        lblMarket.getElement().getStyle().setProperty("cursor", "pointer");
        lblMarket.getElement().getStyle().setProperty("fontWeight", "bold");
        lblMarket.getElement().getStyle().setProperty("fontSize", "18px");
        lblMarket.getElement().setId("nav-market");

        lblMarket.addClickHandler(event -> cambiaVista(creaVistaMarketplace()));

        Label lblPerTe = new Label("PER TE");
        lblPerTe.getElement().getStyle().setProperty("cursor", "pointer");
        lblPerTe.getElement().getStyle().setProperty("fontWeight", "bold");
        lblPerTe.getElement().getStyle().setProperty("fontSize", "18px");
        lblPerTe.getElement().getStyle().setProperty("marginLeft", "60px");
        lblPerTe.getElement().setId("nav-perte");

        lblPerTe.addClickHandler(event -> new ForYouGui().mostra());

        Label lblChat = new Label("CHAT");
        lblChat.getElement().getStyle().setProperty("cursor", "pointer");
        lblChat.getElement().getStyle().setProperty("fontWeight", "bold");
        lblChat.getElement().getStyle().setProperty("fontSize", "18px");
        lblChat.getElement().getStyle().setProperty("marginLeft", "60px");
        lblChat.getElement().setId("nav-chat");

        lblChat.addClickHandler(event -> {
            cambiaVista(new ChatGui());
        });

        Label lblSkill = new Label("SKILL");
        lblSkill.getElement().getStyle().setProperty("cursor", "pointer");
        lblSkill.getElement().getStyle().setProperty("fontWeight", "bold");
        lblSkill.getElement().getStyle().setProperty("fontSize", "18px");
        lblSkill.getElement().getStyle().setProperty("marginLeft", "60px");
        lblSkill.getElement().setId("nav-skill");

        // Menù a discesa
        PopupPanel menuSkill = new PopupPanel(true);
        menuSkill.getElement().getStyle().setProperty("backgroundColor", "white");
        menuSkill.getElement().getStyle().setProperty("border", "1px solid #ccc");
        menuSkill.getElement().getStyle().setProperty("padding", "10px");

        // Comportamento Cursore
        lblSkill.addMouseOverHandler(event -> {
            menuSkill.setPopupPosition(lblSkill.getAbsoluteLeft(), lblSkill.getAbsoluteTop() + 30);
            menuSkill.show();
        });

        VerticalPanel menuContent = new VerticalPanel();
        menuContent.setSpacing(5);

        // Voci del menu
        Label itemSkill = new Label("LE MIE SKILL");
        itemSkill.getElement().getStyle().setProperty("cursor", "pointer");
        itemSkill.getElement().setId("menu-item-le-mie-skill");
        itemSkill.addClickHandler(event -> {
            cambiaVista(new SkillsGui(this));
            menuSkill.hide();
        });

        Label itemRichieste = new Label("LE MIE RICHIESTE");
        itemRichieste.getElement().getStyle().setProperty("cursor", "pointer");
        itemRichieste.getElement().setId("menu-item-le-mie-richieste");
        itemRichieste.addClickHandler(event -> {
            cambiaVista(new RichiesteGui(this));
            menuSkill.hide();
        });

        menuContent.add(itemSkill);
        menuContent.add(itemRichieste);
        menuSkill.add(menuContent);

        imgProfilo = new Image();

        caricaImmagineProfilo(imgProfilo);

        imgProfilo.setPixelSize(40, 40);
        imgProfilo.getElement().getStyle().setProperty("borderRadius", "50%");
        imgProfilo.getElement().getStyle().setProperty("objectFit", "cover");
        imgProfilo.getElement().getStyle().setProperty("cursor", "pointer");
        imgProfilo.getElement().getStyle().setProperty("border", "2px solid #007BFF");
        imgProfilo.getElement().setId("nav-profilo");

        imgProfilo.addClickHandler(event -> {
            new ProfileGui().mostra();
        });

        navLinks.add(lblMarket);
        navLinks.add(lblPerTe);
        navLinks.add(lblChat);
        navLinks.add(lblSkill);

        logoBenvenuto.add(logoLabel);
        logoBenvenuto.add(lblBenvenuto);

        header.add(logoBenvenuto);
        header.add(navLinks);
        header.add(imgProfilo);

        header.setCellWidth(logoBenvenuto, "30%");
        header.setCellWidth(navLinks, "50%");
        header.setCellHorizontalAlignment(navLinks, HasHorizontalAlignment.ALIGN_CENTER);

        // inizializzazione contenitore dinamico
        contenitoreDinamico = new SimplePanel();
        contenitoreDinamico.setWidth("100%");

        mainContainer.add(header);
        mainContainer.add(contenitoreDinamico);

        cambiaVista(creaVistaMarketplace());
        initWidget(mainContainer);
    }

    // metodo per cambiare la vista mostrata nel contenitore dinamico
    public void cambiaVista(Widget nuovaVista) {
        contenitoreDinamico.clear();
        contenitoreDinamico.add(nuovaVista);
    }

    // metodo per generare un pannello fittizio con un messaggio (usato per le
    // pagine non ancora implementate)
    private Widget creaVistaPlaceholder(String messaggio) {
        VerticalPanel placeholder = new VerticalPanel();
        placeholder.setWidth("100%");
        placeholder.setHeight("300px");
        placeholder.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        placeholder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        Label lblMessaggio = new Label(messaggio);
        lblMessaggio.getElement().getStyle().setProperty("fontSize", "20px");
        lblMessaggio.getElement().getStyle().setProperty("color", "gray");

        placeholder.add(lblMessaggio);
        return placeholder;
    }

    private Widget creaVistaMarketplace() {
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

        // Divisorio
        SimplePanel divisorio1 = new SimplePanel();
        divisorio1.setPixelSize(2, 47);
        divisorio1.getElement().getStyle().setProperty("backgroundColor", "#000000");
        divisorio1.getElement().getStyle().setProperty("marginLeft", "5px");
        divisorio1.getElement().getStyle().setProperty("marginRight", "5px");

        // Barra di Ricerca
        TextBox searchBox = new TextBox();
        searchBox.getElement().setPropertyString("placeholder", "Cerca...");
        searchBox.setWidth("500px");
        searchBox.setHeight("35px");
        searchBox.getElement().setId("search-bar");
        searchBox.getElement().getStyle().setProperty("fontSize", "16px");
        searchBox.getElement().getStyle().setProperty("padding", "5px 15px");

        // logica di riceca in tempo reale
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

        // Divisorio
        SimplePanel divisorio2 = new SimplePanel();
        divisorio2.setPixelSize(2, 47);
        divisorio2.getElement().getStyle().setProperty("backgroundColor", "#000000");
        divisorio2.getElement().getStyle().setProperty("marginLeft", "5px");
        divisorio2.getElement().getStyle().setProperty("marginRight", "5px");

        // Bottone Pubblica
        Button btnPubblica = new Button("PUBBLICA");
        btnPubblica.setHeight("47px");
        btnPubblica.setWidth("180px");
        btnPubblica.getElement().getStyle().setProperty("fontSize", "14px");
        btnPubblica.getElement().getStyle().setProperty("cursor", "pointer");
        btnPubblica.getElement().getStyle().setProperty("fontWeight", "bold");
        btnPubblica.getElement().getStyle().setProperty("backgroundImage", "none");
        btnPubblica.getElement().getStyle().setProperty("backgroundColor", "#007BFF");
        btnPubblica.getElement().getStyle().setProperty("color", "white");
        btnPubblica.getElement().getStyle().setProperty("border", "none");
        btnPubblica.getElement().setId("btn-pubblica");

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
        titoloDettaglio.getElement().setId("lbl-titolo");
        titoloDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");
        titoloDettaglio.getElement().getStyle().setProperty("fontSize", "28px");

        votoDettaglio = new Label("");
        votoDettaglio.getElement().getStyle().setProperty("fontSize", "22px");
        votoDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");

        headerDettaglio.add(titoloDettaglio);
        headerDettaglio.add(votoDettaglio);
        headerDettaglio.setCellHorizontalAlignment(votoDettaglio, HasHorizontalAlignment.ALIGN_RIGHT);

        // Label Dettaglio dei dettagli strutturali
        lblCategoria = new Label();
        lblCategoria.getElement().setId("lbl-categoria");
        lblCategoria.getElement().getStyle().setProperty("fontSize", "16px");
        lblCategoria.getElement().getStyle().setProperty("marginBottom", "20px");

        lblDescrizione = new Label();
        lblDescrizione.getElement().setId("lbl-descrizione");
        lblDescrizione.getElement().getStyle().setProperty("fontSize", "16px");
        lblDescrizione.getElement().getStyle().setProperty("marginBottom", "20px");

        lblDispo = new Label();
        lblDispo.getElement().getStyle().setProperty("fontSize", "16px");
        lblDispo.getElement().getStyle().setProperty("marginBottom", "20px");

        lblContro = new Label();
        lblContro.getElement().getStyle().setProperty("fontSize", "16px");
        lblContro.getElement().getStyle().setProperty("marginBottom", "40px");

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

        btnRichiedi.addClickHandler(
                event -> cambiaVista(creaVistaPlaceholder("Pagina RICHIESTA SCAMBIO in costruzione...")));

        // Bottone Dettaglio Chat
        btnChat = new Button("💬");
        btnChat.setHeight("40px");
        btnChat.setWidth("40px");
        btnChat.getElement().getStyle().setProperty("backgroundImage", "none");
        btnChat.getElement().getStyle().setProperty("backgroundColor", "#007BFF");
        btnChat.getElement().getStyle().setProperty("color", "white");
        btnChat.getElement().getStyle().setProperty("border", "none");
        btnChat.getElement().getStyle().setProperty("cursor", "pointer");
        btnChat.getElement().getStyle().setProperty("fontSize", "20px");
        btnChat.setVisible(false);

        btnChat.addClickHandler(event -> {
            if (annuncioSelezionato != null) {
                String autoreAnnuncio = annuncioSelezionato.getAutore();

                // Evita che l'utente apra una chat con se stesso
                if (utenteCorrente != null && utenteCorrente.equals(autoreAnnuncio)) {
                    Window.alert("Non puoi avviare una chat con te stesso!");
                    return;
                }

                // Istanzia il widget, cambia la vista e avvia la conversazione
                ChatGui vistaChat = new ChatGui();
                cambiaVista(vistaChat);
                vistaChat.apriConversazione(autoreAnnuncio);
            }
        });

        // Assemblaggio Contenitore Bottoni
        btnContainer.add(btnRichiedi);
        btnContainer.add(btnChat);

        // Asseblaggio Colonna Destra
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

        // Assemblaggio Pagina Finale
        vistaMarket.add(searchBar);
        vistaMarket.add(contentArea);

        // Avvia il caricamento asincrono dei dati dal database
        caricaAnnunci();

        return vistaMarket;
    }

    private FocusPanel creaCard(Annuncio a) {
        FocusPanel card = new FocusPanel();
        card.getElement().setId("card-annuncio");
        card.setWidth("100%");
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
        this.annuncioSelezionato = a;
        titoloDettaglio.setText(a.getTitolo());
        votoDettaglio.setText("👤 4.9"); // voto fisso di mockup, da collegare a database
        lblCategoria.setText("CATEGORIA: " + a.getCategoria());
        lblDescrizione.setText("DESCRIZIONE: " + a.getSkillOfferta());
        lblDispo.setText("DISPONIBILITÀ: " + a.getDisponibilita());
        lblContro.setText("CONTROPRESTAZIONE: " + a.getControprestazione());

        btnRichiedi.setVisible(true);
        btnChat.setVisible(true);
    }

    private void caricaImmagineProfilo(Image imgProfilo) {
        servizio.getUtente(utenteCorrente, new AsyncCallback<Utente>() {
            @Override
            public void onFailure(Throwable caught) {
                imgProfilo.setUrl("images/utente.jpg"); // Fallback in caso di errore
            }

            @Override
            public void onSuccess(Utente utenteCompleto) {
                // Qui hai l'oggetto Utente vero e proprio, quindi puoi usare il metodo!
                if (utenteCompleto != null && utenteCompleto.getFotoProfiloBase64() != null) {
                    imgProfilo.setUrl(utenteCompleto.getFotoProfiloBase64());
                } else {
                    imgProfilo.setUrl("images/utente.jpg");
                }
            }
        });
    }
}