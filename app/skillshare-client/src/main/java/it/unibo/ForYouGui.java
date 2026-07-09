package it.unibo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Image;
import java.util.ArrayList;
import java.util.List;

public class ForYouGui extends Composite {

    // Servizi e Contenitori Principali
    private SimplePanel contenitoreDinamico;
    private final ForYouServiceAsync servizio = GWT.create(ForYouService.class);

    // Componenti della vista Marketplace
    VerticalPanel colonnaSinistra;
    VerticalPanel colonnaDestra;
    VerticalPanel dettaglioUtente;
    VerticalPanel dettaglioAnnunciUtente;

    // Elementi del dettaglio annuncio che cambieranno dinamicamente
    private String utenteCorrente;
    private Label usernameProfilo;
    private Label votoProfilo;
    private Image imgProfilo;
    private Label lblBiografia;
    private Label lblLocazione;
    private Label alertAnnunci;

    //private Button btnRichiedi;
    //private Button btnChat;
    private List<Utente> tuttiGliUtenti;
    private List<Annuncio> annunciUtenteOrdinati;

    public void mostra() {
        RootPanel.get().clear();
        RootPanel.get().add(this);
    }

    public ForYouGui() {
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

        lblMarket.addClickHandler(event -> new MainLayoutGui().mostra());

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

        lblChat.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina CHAT in costruzione...")));

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

        itemSkill.addClickHandler(event -> {
            cambiaVista(creaVistaPlaceholder("Pagina LE MIE SKILL in costruzione..."));
            menuSkill.hide();
        });

        Label itemRichieste = new Label("LE MIE RICHIESTE");        
        itemRichieste.getElement().getStyle().setProperty("cursor", "pointer");
        
        itemRichieste.addClickHandler(event -> {
            cambiaVista(creaVistaPlaceholder("Pagina LE MIE RICHIESTE in costruzione..."));
            menuSkill.hide();
        });

        menuContent.add(itemSkill);
        menuContent.add(itemRichieste);
        menuSkill.add(menuContent);

        imgProfilo = new Image();

        caricaImmagineProfilo(imgProfilo, utenteCorrente);

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

        cambiaVista(creaVistaForYou());
        initWidget(mainContainer);
    }

    // metodo per cambiare la vista mostrata nel contenitore dinamico
    private void cambiaVista(Widget nuovaVista) {
        contenitoreDinamico.clear();
        contenitoreDinamico.add(nuovaVista);
    }

    private Widget creaVistaForYou() {
        VerticalPanel vistaForYou = new VerticalPanel();
        vistaForYou.setWidth("80%");
        vistaForYou.getElement().getStyle().setProperty("margin", "0 auto");

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

        dettaglioUtente = new VerticalPanel();
        dettaglioUtente.setWidth("100%");
        dettaglioUtente.getElement().getStyle().setProperty("border", "1px solid #666");
        dettaglioUtente.getElement().getStyle().setProperty("padding", "30px");
        dettaglioUtente.getElement().getStyle().setProperty("backgroundColor", "#ffffff");
        dettaglioUtente.getElement().getStyle().setProperty("minHeight", "200px");

        // Dettaglio Utente
        // Header dettaglio (Username + Voto)
        HorizontalPanel headerDettaglioUtente = new HorizontalPanel();
        headerDettaglioUtente.setWidth("100%");
        headerDettaglioUtente.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        headerDettaglioUtente.getElement().getStyle().setProperty("marginBottom", "30px");

        usernameProfilo = new Label("Seleziona un profilo");
        usernameProfilo.getElement().setId("username-profilo");
        usernameProfilo.getElement().getStyle().setProperty("fontWeight", "bold");
        usernameProfilo.getElement().getStyle().setProperty("fontSize", "28px");

        votoProfilo = new Label("");
        votoProfilo.getElement().getStyle().setProperty("fontSize", "22px");
        votoProfilo.getElement().getStyle().setProperty("fontWeight", "bold");

        imgProfilo = new Image();
        imgProfilo.setPixelSize(40, 40);
        imgProfilo.getElement().getStyle().setProperty("borderRadius", "50%");
        imgProfilo.getElement().getStyle().setProperty("objectFit", "cover");
        imgProfilo.getElement().getStyle().setProperty("border", "2px solid #007BFF");
        imgProfilo.getElement().getStyle().setProperty("marginLeft", "15px");
        imgProfilo.getElement().setId("nav-profilo");
        imgProfilo.setVisible(false);

        headerDettaglioUtente.add(usernameProfilo);
        headerDettaglioUtente.add(votoProfilo);
        headerDettaglioUtente.add(imgProfilo);

        headerDettaglioUtente.setCellHorizontalAlignment(votoProfilo, HasHorizontalAlignment.ALIGN_RIGHT);
        headerDettaglioUtente.setCellHorizontalAlignment(imgProfilo, HasHorizontalAlignment.ALIGN_RIGHT);
        headerDettaglioUtente.setCellWidth(usernameProfilo, "100%");
        
        // Dettagli Profilo
        lblBiografia = new Label();
        lblBiografia.getElement().setId("lbl-biografia");
        lblBiografia.getElement().getStyle().setProperty("fontSize", "16px");
        lblBiografia.getElement().getStyle().setProperty("marginBottom", "20px");

        lblLocazione = new Label();
        lblLocazione.getElement().setId("lbl-descrizione");
        lblLocazione.getElement().getStyle().setProperty("fontSize", "16px");
        lblLocazione.getElement().getStyle().setProperty("marginBottom", "20px");

        dettaglioUtente.add(headerDettaglioUtente);
        dettaglioUtente.add(lblBiografia);
        dettaglioUtente.add(lblLocazione);

        dettaglioAnnunciUtente = new VerticalPanel();
        dettaglioAnnunciUtente.setWidth("100%");
        dettaglioAnnunciUtente.getElement().getStyle().setProperty("minHeight", "200px");

        dettaglioAnnunciUtente.setVisible(false);

        alertAnnunci = new Label();
        alertAnnunci.getElement().setId("alert-annunci");
        alertAnnunci.getElement().getStyle().setProperty("marginTop", "15px");
        alertAnnunci.getElement().getStyle().setProperty("marginLeft", "20px");
        alertAnnunci.getElement().getStyle().setProperty("fontWeight", "bold");
        alertAnnunci.getElement().getStyle().setProperty("fontSize", "28px");

        colonnaDestra.add(dettaglioUtente);
        colonnaDestra.add(dettaglioAnnunciUtente);

        // Assemblaggio finale dell'area contenuto
        contentArea.add(colonnaSinistra);
        contentArea.add(spacer);
        contentArea.add(colonnaDestra);

        contentArea.setCellWidth(colonnaSinistra, "45%");
        contentArea.setCellWidth(spacer, "5%");
        contentArea.setCellWidth(colonnaDestra, "50%");

        // Assemblaggio Pagina Finale
        vistaForYou.add(contentArea);

        // Avvia il caricamento asincrono dei dati dal database
        caricaUtentiConsigliati();

        return vistaForYou;
    }

    private void caricaUtentiConsigliati() {
        servizio.getUtentiConsigliati(utenteCorrente, new AsyncCallback<List<Utente>>() {
            @Override
            public void onFailure(Throwable caught) {
                usernameProfilo.setText("Errore nel caricamento degli utenti.");
            }

            @Override
            public void onSuccess(List<Utente> result) {
                tuttiGliUtenti = result; 

                aggiornaVistaUtenti(tuttiGliUtenti);
            }
        });
    }

    private void aggiornaVistaUtenti(List<Utente> utentiDaMostrare) {
        colonnaSinistra.clear();

        Label messaggio = new Label("Utenti Consigliati:");
        messaggio.getElement().getStyle().setProperty("fontWeight", "bold");
        messaggio.getElement().getStyle().setProperty("fontSize", "28px");
        messaggio.getElement().getStyle().setProperty("marginBottom", "15px");

        colonnaSinistra.add(messaggio);
        
        if (utentiDaMostrare == null || utentiDaMostrare.isEmpty()) {
            Label alert = new Label("Nessun utente trovato");
            alert.getElement().setId("alert-utenti");
            alert.getElement().getStyle().setProperty("fontSize", "22px");
            colonnaSinistra.add(alert);
            return;
        }
        
        for (Utente a : utentiDaMostrare) {
            colonnaSinistra.add(creaCardUtente(a));
        }
    }

    private FocusPanel creaCardUtente(Utente a) {
        FocusPanel card = new FocusPanel();
        card.getElement().setId("card-utente");
        card.setWidth("100%");
        card.getElement().getStyle().setProperty("border", "1px solid #666");
        card.getElement().getStyle().setProperty("marginBottom", "15px");
        card.getElement().getStyle().setProperty("backgroundColor", "#ffffff");
        
        HorizontalPanel cardContent = new HorizontalPanel();
        cardContent.setWidth("100%");
        cardContent.setHeight("80px");
        cardContent.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        cardContent.getElement().getStyle().setProperty("padding", "0 20px");

        Label lblUsername = new Label(a.getUsername());
        lblUsername.getElement().setId("username-card-utente");
        lblUsername.getElement().getStyle().setProperty("fontSize", "20px");

        Label lblVoto = new Label("4.9");
        lblVoto.getElement().getStyle().setProperty("fontSize", "22px");
        lblVoto.getElement().getStyle().setProperty("fontWeight", "bold");
        lblVoto.getElement().getStyle().setProperty("marginLeft", "15px");

        Image imgCard = new Image();
        imgCard.setPixelSize(40, 40);
        imgCard.getElement().getStyle().setProperty("borderRadius", "50%");
        imgCard.getElement().getStyle().setProperty("objectFit", "cover");
        imgCard.getElement().getStyle().setProperty("border", "2px solid #007BFF");
        imgCard.getElement().setId("card-img-profilo");
        imgCard.getElement().getStyle().setProperty("marginLeft", "15px");

        caricaImmagineProfilo(imgCard, a.getUsername());

        cardContent.add(lblUsername);
        cardContent.add(lblVoto);
        cardContent.add(imgCard);
        
        cardContent.setCellHorizontalAlignment(lblVoto, HasHorizontalAlignment.ALIGN_RIGHT);
        cardContent.setCellHorizontalAlignment(imgCard, HasHorizontalAlignment.ALIGN_RIGHT);
        cardContent.setCellWidth(lblUsername, "100%"); 

        card.add(cardContent);

        card.addClickHandler(event -> {
            mostraDettaglio(a);
            caricaAnnunciOrdinati(a.getUsername());
        });
        
        return card;
    }

    private void mostraDettaglio(Utente a) {
        dettaglioAnnunciUtente.clear();

        usernameProfilo.setText(a.getUsername());
        votoProfilo.setText("4.9"); // voto fisso di mockup, da collegare a database
        caricaImmagineProfilo(imgProfilo, a.getUsername());
        imgProfilo.setVisible(true);
        lblBiografia.setText("BIOGRAFIA: " + a.getBio());
        lblLocazione.setText("LOCAZIONE: " + a.getLocazione()); 
    }

    private void caricaAnnunciOrdinati(String username){
        dettaglioAnnunciUtente.setVisible(true);

        servizio.getAnnunciOrdinati(utenteCorrente, username, new AsyncCallback<List<Annuncio>>() {
            @Override
            public void onFailure(Throwable caught) {
                alertAnnunci.setText("Errore nel caricamento degli annunci.");
            }

            @Override
            public void onSuccess(List<Annuncio> result) {
                annunciUtenteOrdinati = result; 

                aggiornaVistaAnnunci(annunciUtenteOrdinati);
            }
        });
    }

    private void aggiornaVistaAnnunci(List<Annuncio> annunci){
        if (annunci == null || annunci.isEmpty()) {
            alertAnnunci.setText("Nessun annuncio trovato");
            dettaglioAnnunciUtente.add(alertAnnunci);
            return;
        }
        
        for (Annuncio a : annunci) {
            dettaglioAnnunciUtente.add(creaCardAnnuncio(a));
        }
    }

    private VerticalPanel creaCardAnnuncio(Annuncio a) {
        // Dettaglio Annuncio
        VerticalPanel cardAnnuncio = new VerticalPanel();
        cardAnnuncio.getElement().setId("card-utente");
        cardAnnuncio.setWidth("100%");
        cardAnnuncio.getElement().getStyle().setProperty("border", "1px solid #666");
        cardAnnuncio.getElement().getStyle().setProperty("marginTop", "15px");
        cardAnnuncio.getElement().getStyle().setProperty("cursor", "pointer");
        cardAnnuncio.getElement().getStyle().setProperty("backgroundColor", "#ffffff");

        Label titoloDettaglio = new Label(a.getTitolo());
        titoloDettaglio.getElement().setId("lbl-titolo");
        titoloDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");
        titoloDettaglio.getElement().getStyle().setProperty("fontSize", "28px");

        // Label Dettaglio dei dettagli strutturali
        Label lblCategoria = new Label("CATEGORIA: " + a.getCategoria());
        lblCategoria.getElement().setId("lbl-categoria");
        lblCategoria.getElement().getStyle().setProperty("fontSize", "16px");
        lblCategoria.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblDescrizione = new Label("DESCRIZIONE: " + a.getSkillOfferta());
        lblDescrizione.getElement().setId("lbl-descrizione");
        lblDescrizione.getElement().getStyle().setProperty("fontSize", "16px");
        lblDescrizione.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblDispo = new Label("DISPONIBILITÀ: " + a.getDisponibilita());
        lblDispo.getElement().getStyle().setProperty("fontSize", "16px");
        lblDispo.getElement().getStyle().setProperty("marginBottom", "20px");

        Label lblContro = new Label("CONTROPRESTAZIONE: " + a.getControprestazione());
        lblContro.getElement().getStyle().setProperty("fontSize", "16px");
        lblContro.getElement().getStyle().setProperty("marginBottom", "40px");

        //Contenitore Bottoni Richiedi e Chat
        FlowPanel btnContainer = new FlowPanel();
        btnContainer.setWidth("100%");
        btnContainer.getElement().getStyle().setProperty("display", "flex");
        btnContainer.getElement().getStyle().setProperty("justifyContent", "flex-end");

        // Bottone Dettaglio Richiedi
        Button btnRichiedi = new Button("RICHIEDI");
        btnRichiedi.setHeight("40px");
        btnRichiedi.setWidth("130px");
        btnRichiedi.getElement().getStyle().setProperty("fontSize", "14px");
        btnRichiedi.getElement().getStyle().setProperty("cursor", "pointer");
        btnRichiedi.getElement().getStyle().setProperty("fontWeight", "bold");
        btnRichiedi.getElement().getStyle().setProperty("background", "#333333");
        btnRichiedi.getElement().getStyle().setProperty("color", "white");
        btnRichiedi.getElement().getStyle().setProperty("border", "none");
        btnRichiedi.getElement().getStyle().setProperty("marginRight", "10px");

        btnRichiedi.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina RICHIESTA SCAMBIO in costruzione...")));

        // Bottone Dettaglio Chat
        Button btnChat = new Button("💬");
        btnChat.setHeight("40px");
        btnChat.setWidth("40px");
        btnChat.getElement().getStyle().setProperty("backgroundImage", "none");
        btnChat.getElement().getStyle().setProperty("backgroundColor", "#007BFF");
        btnChat.getElement().getStyle().setProperty("color", "white");
        btnChat.getElement().getStyle().setProperty("border", "none");
        btnChat.getElement().getStyle().setProperty("cursor", "pointer");
        btnChat.getElement().getStyle().setProperty("fontSize", "20px");

        btnChat.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina CHAT in costruzione...")));

        // Assemblaggio Contenitore Bottoni
        btnContainer.add(btnRichiedi);
        btnContainer.add(btnChat);

        // Asseblaggio Colonna Destra
        cardAnnuncio.add(titoloDettaglio);
        cardAnnuncio.add(lblCategoria);
        cardAnnuncio.add(lblDescrizione);
        cardAnnuncio.add(lblDispo);
        cardAnnuncio.add(lblContro);
        cardAnnuncio.add(btnContainer);

        return cardAnnuncio;
    }

    private void caricaImmagineProfilo(Image imgProfilo, String username) {
        servizio.getUtente(username, new AsyncCallback<Utente>() {
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

    // metodo per generare un pannello fittizio con un messaggio (usato per le pagine non ancora implementate)
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
}
