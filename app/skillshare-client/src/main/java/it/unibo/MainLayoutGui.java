package it.unibo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainLayoutGui extends Composite {

    private SimplePanel contenitoreDinamico;

    public MainLayoutGui() {
        VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.setWidth("100%");
        mainContainer.setHeight("100%");
        mainContainer.getElement().getStyle().setProperty("padding", "20px");

        HorizontalPanel header = new HorizontalPanel();
        header.setWidth("100%");
        header.setHeight("60px");
        header.setVerticalAlignment(com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE);
        header.getElement().getStyle().setProperty("borderBottom", "2px solid #ccc");
        header.getElement().getStyle().setProperty("marginBottom", "30px");

        com.google.gwt.user.client.ui.Label logoLabel = new com.google.gwt.user.client.ui.Label("SKILLSHARE");
        logoLabel.getElement().getStyle().setProperty("fontWeight", "bold");
        logoLabel.getElement().getStyle().setProperty("fontSize", "22px");

        HorizontalPanel navLinks = new HorizontalPanel();
        navLinks.setSpacing(20); 

        com.google.gwt.user.client.ui.Label lblMarket = new com.google.gwt.user.client.ui.Label("MARKET");
        com.google.gwt.user.client.ui.Label lblPerTe = new com.google.gwt.user.client.ui.Label("PER TE");
        com.google.gwt.user.client.ui.Label lblChat = new com.google.gwt.user.client.ui.Label("CHAT");
        com.google.gwt.user.client.ui.Label lblProfilo = new com.google.gwt.user.client.ui.Label("👤 Profilo");

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
        
        header.setCellWidth(logoLabel, "20%");
        header.setCellWidth(navLinks, "60%");
        header.setCellHorizontalAlignment(navLinks, com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER);
        header.setCellWidth(lblProfilo, "20%");
        header.setCellHorizontalAlignment(lblProfilo, com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT);

        // --- 2. INIZIALIZZAZIONE CONTENITORE DINAMICO ---
        contenitoreDinamico = new SimplePanel();
        contenitoreDinamico.setWidth("100%");

        // --- 3. GESTIONE DEI CLICK ---
        lblMarket.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina MARKETPLACE in costruzione...")));
        lblPerTe.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina PER TE in costruzione...")));
        lblChat.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina CHAT in costruzione...")));
        lblProfilo.addClickHandler(event -> cambiaVista(creaVistaPlaceholder("Pagina PROFILO in costruzione...")));

        mainContainer.add(header);
        mainContainer.add(contenitoreDinamico);

        cambiaVista(creaVistaPlaceholder("Pagina MARKETPLACE in costruzione..."));
        initWidget(mainContainer);
    }

    private void cambiaVista(Widget nuovaVista) {
        contenitoreDinamico.clear(); 
        contenitoreDinamico.add(nuovaVista); 
    }

    private com.google.gwt.user.client.ui.Widget creaVistaPlaceholder(String messaggio) {
        VerticalPanel placeholder = new VerticalPanel();
        placeholder.setWidth("100%");
        placeholder.setHeight("300px");
        placeholder.setVerticalAlignment(com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE);
        placeholder.setHorizontalAlignment(com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER);
        
        com.google.gwt.user.client.ui.Label lblMessaggio = new com.google.gwt.user.client.ui.Label(messaggio);
        lblMessaggio.getElement().getStyle().setProperty("fontSize", "20px");
        lblMessaggio.getElement().getStyle().setProperty("color", "gray");
        
        placeholder.add(lblMessaggio);
        return placeholder;
    }
}