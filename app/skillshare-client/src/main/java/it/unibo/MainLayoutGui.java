package it.unibo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainLayoutGui extends Composite {
    public MainLayoutGui() {
        VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.setWidth("100%");
        mainContainer.setHeight("100%");
        mainContainer.getElement().getStyle().setProperty("padding", "20px");

        initWidget(mainContainer);

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

        mainContainer.add(header);
    }
}