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

public class SkillsGui extends Composite{
    
    private VerticalPanel mainPanel = new VerticalPanel();
    private SimplePanel contentArea = new SimplePanel();

    // Contenitori interni che si popoleranno con i dati delle skill
    private VerticalPanel listaMieSkill = new VerticalPanel();
    private VerticalPanel listaSkillsAccettate = new VerticalPanel();
    private VerticalPanel listaSkillsConcluse = new VerticalPanel();
    // interfaccia asincrona per comunicare con il server
    private SkillServiceAsync skillService = GWT.create(SkillService.class);

    public SkillsGui() {
        initWidget(mainPanel);
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);

        HorizontalPanel splitLayout = new HorizontalPanel();
        splitLayout.setWidth("100%");
        splitLayout.setSpacing(20);

        // Sidebar con menù a tendina
        VerticalPanel sidebar = new VerticalPanel();
        sidebar.setWidth("300px");

        // Tendina "Mie Skills" (Attive)
        DisclosurePanel discMieSkills = new DisclosurePanel("Mie Skills");
        discMieSkills.getElement().setId("sidebar-mie-skills");
        discMieSkills.setOpen(true);
        listaMieSkill.setSpacing(5);
        listaMieSkill.setWidth("100%");
        discMieSkills.setContent(listaMieSkill);

        // Tendina "Skills Accettate"
        DisclosurePanel discAccettate = new DisclosurePanel("Skills Accettate");
        discAccettate.getElement().setId("sidebar-skills-accettate");
        listaSkillsAccettate.setSpacing(5);
        listaSkillsAccettate.setWidth("100%");
        discAccettate.setContent(listaSkillsAccettate);

        // Tendina "Skills Concluse"
        DisclosurePanel discConcluse = new DisclosurePanel("Skills Concluse");
        discConcluse.getElement().setId("sidebar-skills-concluse");
        listaSkillsConcluse.setSpacing(5);
        listaSkillsConcluse.setWidth("100%");
        discConcluse.setContent(listaSkillsConcluse);


        sidebar.add(discMieSkills);
        sidebar.add(discAccettate);
        sidebar.add(discConcluse);

        contentArea.setWidth("100%");
        splitLayout.add(sidebar);
        splitLayout.add(contentArea);

        splitLayout.setCellVerticalAlignment(sidebar, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellVerticalAlignment(contentArea, HasVerticalAlignment.ALIGN_TOP);
        splitLayout.setCellWidth(contentArea, "100%"); 
        
        mainPanel.add(splitLayout);

        caricaSkillsDalDatabase();
    }

    private void caricaSkillsDalDatabase() {
        skillService.getMieSkills("admin", new AsyncCallback<List<Annuncio>>() {
            @Override
            public void onFailure(Throwable caught) {
                contentArea.setWidget(new Label("Errore di rete: Impossibile caricare le skill."));
            }

            @Override
            public void onSuccess(List<Annuncio> skillsDalDb) {
                listaMieSkill.clear();
                listaSkillsAccettate.clear();
                listaSkillsConcluse.clear();

                for (Annuncio skill : skillsDalDb) {
                    Button btnSkill = new Button(skill.getTitolo());
                    btnSkill.setWidth("100%");
                    // Stile base per renderli simili alle voci del wireframe
                    btnSkill.getElement().getStyle().setProperty("textAlign", "left");
                    btnSkill.getElement().getStyle().setProperty("padding", "10px");
                    btnSkill.getElement().getStyle().setProperty("backgroundColor", "#fff");
                    btnSkill.getElement().getStyle().setProperty("border", "1px solid #000");
                    
                    btnSkill.addClickHandler(event -> mostraDettagliCard(skill));

                    String statoSimulato = "ATTIVA";
                    if (skill.getTitolo().equals("Programmazione Java")) statoSimulato = "ACCETTATA";
                    if (skill.getTitolo().equals("Allenamento Tennis")) statoSimulato = "CONCLUSA";

                    switch (statoSimulato) {
                        case "ATTIVA": listaMieSkill.add(btnSkill); break;
                        case "ACCETTATA": listaSkillsAccettate.add(btnSkill); break;
                        case "CONCLUSA": listaSkillsConcluse.add(btnSkill); break;
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

        // Header della card con titolo e rating
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

        // DETTAGLI CARD
        Label lblCat = new Label("CATEGORIA: " + skill.getCategoria());
        lblCat.getElement().getStyle().setProperty("marginBottom", "10px");
        card.add(lblCat);
        
        Label lblOgg = new Label("DETTAGLI OGGETTO: " + skill.getSkillOfferta());
        lblOgg.getElement().getStyle().setProperty("marginBottom", "10px");
        card.add(lblOgg);
        
        Label lblDisp = new Label("DISPONIBILITÀ: " + skill.getDisponibilita());
        lblDisp.getElement().getStyle().setProperty("marginBottom", "10px");
        card.add(lblDisp);

        // Bottoni allineati a destra
        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");
        
        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        String statoSimulato = "ATTIVA";
        if (skill.getTitolo().equals("Programmazione Java")) statoSimulato = "ACCETTATA";
        if (skill.getTitolo().equals("Allenamento Tennis")) statoSimulato = "CONCLUSA";

        if (statoSimulato.equals("ATTIVA")) {
            buttonGroups.add(new Button("Rimuovi"));
            buttonGroups.add(new Button("Modifica"));
            Button btnChat = new Button("💬");
            btnChat.getElement().getStyle().setProperty("backgroundColor", "#007bff");
            btnChat.getElement().getStyle().setProperty("color", "#fff");
            buttonGroups.add(btnChat);
        } else if (statoSimulato.equals("ACCETTATA")) {
            buttonGroups.add(new Button("✓"));
            buttonGroups.add(new Button("X"));
            buttonGroups.add(new Button("💬"));
        } else if (statoSimulato.equals("CONCLUSA")) {
            buttonGroups.add(new Button("💬"));
            buttonGroups.add(new Button("Valuta"));
        }

        buttonWrapper.add(buttonGroups);
        // Allineamento del gruppo di bottoni tutto a destra
        buttonWrapper.setCellHorizontalAlignment(buttonGroups, HasHorizontalAlignment.ALIGN_RIGHT);
        
        card.add(buttonWrapper);
        contentArea.add(card);
    }
}

