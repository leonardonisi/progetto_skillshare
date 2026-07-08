package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class MainLayoutGui extends Composite {

    private SimplePanel contenitoreDinamico;
    private final MarketServiceAsync servizio = GWT.create(MarketService.class);
    private String utenteCorrente;
    private Image imgProfilo;

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

        lblMarket.addClickHandler(event -> cambiaVista(new MarketGui()));

        Label lblPerTe = new Label("PER TE");
        lblPerTe.getElement().getStyle().setProperty("cursor", "pointer");
        lblPerTe.getElement().getStyle().setProperty("fontWeight", "bold");
        lblPerTe.getElement().getStyle().setProperty("fontSize", "18px");
        lblPerTe.getElement().getStyle().setProperty("marginLeft", "60px");
        lblPerTe.getElement().setId("nav-perte");

        lblPerTe.addClickHandler(event -> {
            cambiaVista(new ForYouGui());
        });

        Label lblChat = new Label("CHAT");
        lblChat.getElement().getStyle().setProperty("cursor", "pointer");
        lblChat.getElement().getStyle().setProperty("fontWeight", "bold");
        lblChat.getElement().getStyle().setProperty("fontSize", "18px");
        lblChat.getElement().getStyle().setProperty("marginLeft", "60px");
        lblChat.getElement().setId("nav-chat");
        lblChat.addClickHandler(event -> {
            new ChatGui().mostra();
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
            cambiaVista(new SkillsGui());
            menuSkill.hide();
        });

        Label itemRichieste = new Label("LE MIE RICHIESTE");
        itemRichieste.getElement().getStyle().setProperty("cursor", "pointer");
        itemRichieste.getElement().setId("menu-item-le-mie-richieste");
        itemRichieste.addClickHandler(event -> {
            cambiaVista(new RichiesteGui());
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
        header.setCellHorizontalAlignment(imgProfilo, HasHorizontalAlignment.ALIGN_CENTER);

        // inizializzazione contenitore dinamico
        contenitoreDinamico = new SimplePanel();
        contenitoreDinamico.setWidth("100%");

        mainContainer.add(header);
        mainContainer.add(contenitoreDinamico);

        cambiaVista(new MarketGui());
        initWidget(mainContainer);
    }

    // metodo per cambiare la vista mostrata nel contenitore dinamico
    private void cambiaVista(Widget nuovaVista) {
        contenitoreDinamico.clear();
        contenitoreDinamico.add(nuovaVista);
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