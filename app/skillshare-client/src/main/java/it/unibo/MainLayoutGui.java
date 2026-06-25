package it.unibo;

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
import com.google.gwt.user.client.ui.Widget;

public class MainLayoutGui extends Composite {
    // Contenitore dinamico per le varie viste
    private SimplePanel contenitoreDinamico;

    public void mostra() {
        RootPanel.get().clear();
        RootPanel.get().add(this); // Attacca questo layout allo schermo
    }

    public MainLayoutGui() {
        // inizializzazione layout principale
        VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.setWidth("100%");
        mainContainer.setHeight("100%");
        mainContainer.getElement().getStyle().setProperty("padding", "20px");

        // inizializzazione header
        HorizontalPanel header = new HorizontalPanel();
        header.setWidth("100%");
        header.setHeight("60px");
        header.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        header.getElement().getStyle().setProperty("borderBottom", "2px solid #ccc");
        header.getElement().getStyle().setProperty("marginBottom", "30px");

        // logo e link di navigazione
        Label logoLabel = new com.google.gwt.user.client.ui.Label("SKILLSHARE");
        logoLabel.getElement().getStyle().setProperty("fontWeight", "bold");
        logoLabel.getElement().getStyle().setProperty("fontSize", "22px");
        // ID per il test Selenium del login
        logoLabel.getElement().setId("titolo-home");

        HorizontalPanel navLinks = new HorizontalPanel();
        navLinks.setSpacing(20); 

        Label lblMarket = new Label("MARKET");
        Label lblPerTe = new Label("PER TE");
        Label lblChat = new Label("CHAT");
        Label lblProfilo = new Label("👤 Profilo");
        //ID per i tuoi futuri test Selenium della Home
        lblMarket.getElement().setId("nav-market");
        lblPerTe.getElement().setId("nav-perte");
        lblChat.getElement().setId("nav-chat");
        lblProfilo.getElement().setId("nav-profilo");

        // Stile per renderere i link cliccabili
        lblMarket.getElement().getStyle().setProperty("cursor", "pointer");

        // Stile per renderere i link cliccabili
        lblMarket.getElement().getStyle().setProperty("cursor", "pointer");
        lblMarket.getElement().getStyle().setProperty("fontWeight", "bold");
        lblPerTe.getElement().getStyle().setProperty("cursor", "pointer");
        lblPerTe.getElement().getStyle().setProperty("fontWeight", "bold");
        lblChat.getElement().getStyle().setProperty("cursor", "pointer");
        lblChat.getElement().getStyle().setProperty("fontWeight", "bold");
        lblProfilo.getElement().getStyle().setProperty("cursor", "pointer");
        lblProfilo.getElement().getStyle().setProperty("fontWeight", "bold");

        navLinks.add(lblMarket);
        navLinks.add(lblPerTe);
        navLinks.add(lblChat);

        header.add(logoLabel);
        header.add(navLinks);
        header.add(lblProfilo);
        
        // Allineamento e spaziatura
        header.setCellWidth(logoLabel, "20%");
        header.setCellWidth(navLinks, "60%");
        header.setCellHorizontalAlignment(navLinks, HasHorizontalAlignment.ALIGN_CENTER);
        header.setCellWidth(lblProfilo, "20%");
        header.setCellHorizontalAlignment(lblProfilo, HasHorizontalAlignment.ALIGN_RIGHT);

        // inizializzazione contenitore dinamico
        contenitoreDinamico = new SimplePanel();
        contenitoreDinamico.setWidth("100%");

        // gestione click sui link di navigazione (cambia vista, andranno poi implementate le vere pagine)
        lblMarket.addClickHandler(event -> cambiaVista(creaVistaMarketplace()));
        lblPerTe.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina PER TE in costruzione...")));
        lblChat.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina CHAT in costruzione...")));
        lblProfilo.addClickHandler(event -> { new ProfileGui().mostra();});

        mainContainer.add(header);
        mainContainer.add(contenitoreDinamico);
        // mostra vista iniziale (marketplace)
        cambiaVista(creaVistaMarketplace());
        initWidget(mainContainer);
    }

    // metodo per cambiare la vista mostrata nel contenitore dinamico
    private void cambiaVista(Widget nuovaVista) {
        contenitoreDinamico.clear(); 
        contenitoreDinamico.add(nuovaVista); 
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

    // costruisce la vista del marketplace con barra di ricerca, elenco di annunci e dettaglio annuncio
    private Widget creaVistaMarketplace() {
        VerticalPanel vistaMarket = new VerticalPanel();
        vistaMarket.setWidth("80%");
        vistaMarket.getElement().getStyle().setProperty("margin", "0 auto");

        // Barra di ricerca e pulsante "Pubblica"
        HorizontalPanel searchBar = new HorizontalPanel();
        searchBar.setWidth("100%");
        searchBar.setSpacing(10); 
        searchBar.getElement().getStyle().setProperty("marginBottom", "40px"); 
        searchBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        TextBox searchBox = new TextBox();
        searchBox.getElement().setPropertyString("placeholder", "Cerca...");
        searchBox.setWidth("500px"); 
        searchBox.setHeight("35px"); 
        searchBox.getElement().getStyle().setProperty("fontSize", "16px");
        searchBox.getElement().getStyle().setProperty("padding", "5px 15px");

        Button btnPubblica = new Button("PUBBLICA");
        btnPubblica.setHeight("47px"); 
        btnPubblica.setWidth("180px");
        btnPubblica.getElement().getStyle().setProperty("fontSize", "14px");
        btnPubblica.getElement().getStyle().setProperty("cursor", "pointer");
        btnPubblica.getElement().getStyle().setProperty("fontWeight", "bold");

        // test selenium
        btnPubblica.getElement().setId("btn-pubblica");

        btnPubblica.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina CREAZIONE ANNUNCIO in costruzione...")));

        searchBar.add(searchBox);
        searchBar.add(btnPubblica);

        // Area contenuto con elenco annunci a sinistra e dettaglio annuncio a destra
        HorizontalPanel contentArea = new HorizontalPanel();
        contentArea.setWidth("100%");
        contentArea.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP); 

        // Colonna Sinistra (lista annunci)
        VerticalPanel colonnaSinistra = new VerticalPanel();
        colonnaSinistra.setWidth("100%"); 
        
        for (int i = 1; i <= 3; i++) {
            FocusPanel card = new FocusPanel();
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

            Label lblTitoloCard = new Label("TITOLO " + i);
            lblTitoloCard.getElement().getStyle().setProperty("fontSize", "20px"); 
            
            cardContent.add(lblTitoloCard);
            card.add(cardContent);
            
            final int index = i;
            card.addClickHandler(event -> {
                System.out.println("Cliccato riquadro " + index);
            });

            colonnaSinistra.add(card);
        }

        SimplePanel spacer = new SimplePanel();
        spacer.setWidth("100%"); 

        // Colonna Destra (dettaglio annuncio)
        VerticalPanel colonnaDestra = new VerticalPanel();
        colonnaDestra.setWidth("100%"); 
        colonnaDestra.getElement().getStyle().setProperty("border", "1px solid #666");
        colonnaDestra.getElement().getStyle().setProperty("padding", "30px"); 
        colonnaDestra.getElement().getStyle().setProperty("backgroundColor", "#ffffff");
        colonnaDestra.getElement().getStyle().setProperty("minHeight", "380px"); 

        HorizontalPanel headerDettaglio = new HorizontalPanel();
        headerDettaglio.setWidth("100%");
        headerDettaglio.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        headerDettaglio.getElement().getStyle().setProperty("marginBottom", "30px");

        Label titoloDettaglio = new Label("TITOLO");
        titoloDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");
        titoloDettaglio.getElement().getStyle().setProperty("fontSize", "28px"); 
        
        Label votoDettaglio = new Label("👤 4.9");
        votoDettaglio.getElement().getStyle().setProperty("fontSize", "22px");
        votoDettaglio.getElement().getStyle().setProperty("fontWeight", "bold");

        headerDettaglio.add(titoloDettaglio);
        headerDettaglio.add(votoDettaglio);
        headerDettaglio.setCellHorizontalAlignment(votoDettaglio, HasHorizontalAlignment.ALIGN_RIGHT);

        colonnaDestra.add(headerDettaglio);
        // Dettagli dell'annuncio
        Label lblCategoria = new Label("CATEGORIA: Sviluppo Software");
        lblCategoria.getElement().getStyle().setProperty("fontSize", "16px"); 
        lblCategoria.getElement().getStyle().setProperty("marginBottom", "20px");
        
        Label lblDettagli = new Label("DETTAGLI OGGETTO: ...");
        lblDettagli.getElement().getStyle().setProperty("fontSize", "16px");
        lblDettagli.getElement().getStyle().setProperty("marginBottom", "20px");
        
        Label lblDispo = new Label("DISPONIBILITÀ: ...");
        lblDispo.getElement().getStyle().setProperty("fontSize", "16px");
        lblDispo.getElement().getStyle().setProperty("marginBottom", "20px"); 

        Label lblContro = new Label("CONTROPRESTAZIONE: ...");
        lblContro.getElement().getStyle().setProperty("fontSize", "16px");
        lblContro.getElement().getStyle().setProperty("marginBottom", "40px"); 

        colonnaDestra.add(lblCategoria);
        colonnaDestra.add(lblDettagli);
        colonnaDestra.add(lblDispo);
        colonnaDestra.add(lblContro);
        
        // Tasto d'azione per richiedere lo scambio
        Button btnRichiedi = new Button("RICHIEDI");
        btnRichiedi.setHeight("40px");
        btnRichiedi.setWidth("130px");
        btnRichiedi.getElement().getStyle().setProperty("fontSize", "14px");
        btnRichiedi.getElement().getStyle().setProperty("cursor", "pointer");
        btnRichiedi.getElement().getStyle().setProperty("fontWeight", "bold");
        btnRichiedi.getElement().getStyle().setProperty("backgroundImage", "none"); 
        btnRichiedi.getElement().getStyle().setProperty("background", "#333333");
        btnRichiedi.getElement().getStyle().setProperty("color", "white");
        btnRichiedi.getElement().getStyle().setProperty("border", "none");

        btnRichiedi.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina RICHIESTA SCAMBIO in costruzione...")));
        
        HorizontalPanel btnContainer = new HorizontalPanel();
        btnContainer.setWidth("100%");
        btnContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        btnContainer.add(btnRichiedi);
        
        colonnaDestra.add(btnContainer);

        contentArea.add(colonnaSinistra);
        contentArea.add(spacer);
        contentArea.add(colonnaDestra);

        contentArea.setCellWidth(colonnaSinistra, "45%");
        contentArea.setCellWidth(spacer, "5%");
        contentArea.setCellWidth(colonnaDestra, "50%");

        vistaMarket.add(searchBar);
        vistaMarket.add(contentArea);

        return vistaMarket;
    }
}