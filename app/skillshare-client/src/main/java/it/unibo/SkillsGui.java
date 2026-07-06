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

public class SkillsGui extends Composite {

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
                int indice = 0;
                for (Annuncio skill : skillsDalDb) {
                    Button btnSkill = new Button(skill.getTitolo());
                    btnSkill.setWidth("100%");
                    btnSkill.getElement().setId("btn-skill-" + indice);
                    indice++;
                    // Stile base per renderli simili alle voci del wireframe
                    btnSkill.getElement().getStyle().setProperty("textAlign", "left");
                    btnSkill.getElement().getStyle().setProperty("padding", "10px");
                    btnSkill.getElement().getStyle().setProperty("backgroundColor", "#fff");
                    btnSkill.getElement().getStyle().setProperty("border", "1px solid #000");

                    btnSkill.addClickHandler(event -> mostraDettagliCard(skill));

                    String statoSimulato = "ATTIVA";
                    if (skill.getTitolo().equals("Programmazione Java"))
                        statoSimulato = "ACCETTATA";
                    if (skill.getTitolo().equals("Allenamento Tennis"))
                        statoSimulato = "CONCLUSA";

                    switch (statoSimulato) {
                        case "ATTIVA":
                            listaMieSkill.add(btnSkill);
                            break;
                        case "ACCETTATA":
                            listaSkillsAccettate.add(btnSkill);
                            break;
                        case "CONCLUSA":
                            listaSkillsConcluse.add(btnSkill);
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

        Label lblContro = new Label("CONTROPRESTAZIONE OFFERTA: " + skill.getControprestazione());
        lblContro.getElement().getStyle().setProperty("marginBottom", "20px");
        card.add(lblContro);

        // Bottoni allineati a destra
        HorizontalPanel buttonWrapper = new HorizontalPanel();
        buttonWrapper.setWidth("100%");

        HorizontalPanel buttonGroups = new HorizontalPanel();
        buttonGroups.setSpacing(10);

        String statoSimulato = "ATTIVA";
        if (skill.getTitolo().equals("Programmazione Java"))
            statoSimulato = "ACCETTATA";
        if (skill.getTitolo().equals("Allenamento Tennis"))
            statoSimulato = "CONCLUSA";

        lblTitolo.getElement().setId("lbl-titolo");
        lblOgg.getElement().setId("lbl-descrizione");

        if (statoSimulato.equals("ATTIVA")) {
            // 1. Creiamo il bottone Rimuovi con la tua logica RPC (da HEAD)
            Button btnRimuovi = new Button("Rimuovi");
            
            btnRimuovi.addClickHandler(event -> {
                boolean confermato = Window.confirm("Sei sicuro di voler eliminare definitivamente questo annuncio dal Marketplace?");
                
                if (confermato) {
                    skillService.deleteSkill(skill.getId(), new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore di comunicazione con il server.");
                        }

                        @Override
                        public void onSuccess(Boolean eliminato) {
                            if (eliminato) {
                                // Svuota l'area centrale e ricarica la tendina aggiornata senza la skill eliminata
                                contentArea.clear();
                                caricaSkillsDalDatabase();
                            } else {
                                Window.alert("Errore: Impossibile trovare la skill da eliminare.");
                            }
                        }
                    });
                }
            });
            
            // 2. Creiamo il bottone Modifica mantenendo l'ID inserito dal tuo collega (da main)
            Button btnModifica = new Button("Modifica");
            btnModifica.getElement().setId("btn-modifica-annuncio");
            
            // Aggiungiamo i bottoni modificati al gruppo
            buttonGroups.add(btnRimuovi);
            buttonGroups.add(btnModifica);

            Button btnChat = new Button("💬");
            btnChat.getElement().getStyle().setProperty("backgroundColor", "#007bff");
            btnChat.getElement().getStyle().setProperty("color", "#fff");

            // Gestione click Modifica
            btnModifica.addClickHandler(clickEvent -> {
                card.clear();
                card.add(cardHeader);

                TextBox txtTitolo = new TextBox();
                txtTitolo.getElement().setId("input-modifica-titolo");
                txtTitolo.setText(skill.getTitolo());
                txtTitolo.setWidth("100%");
                card.add(new Label("TITOLO:"));
                card.add(txtTitolo);

                // ListBox per le categorie
                ListBox listCat = new com.google.gwt.user.client.ui.ListBox();
                card.add(new Label("CATEGORIA:"));
                card.add(listCat);

                // Recupero categorie
                CreateAdServiceAsync adService = GWT.create(CreateAdService.class);
                adService.getCategorie(new AsyncCallback<List<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        listCat.addItem(skill.getCategoria());
                    }

                    @Override
                    public void onSuccess(List<String> categorieDalDb) {
                        for (String cat : categorieDalDb) {
                            listCat.addItem(cat);
                        }

                        // Seleziona la categoria attuale dell'annuncio
                        for (int i = 0; i < listCat.getItemCount(); i++) {
                            if (listCat.getItemText(i).equalsIgnoreCase(skill.getCategoria())) {
                                listCat.setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                });

                // Skill Offerta
                TextArea txtDesc = new TextArea();
                txtDesc.getElement().setId("input-modifica-descrizione");
                txtDesc.setText(skill.getSkillOfferta());
                txtDesc.setWidth("100%");
                card.add(new Label("DETTAGLI OGGETTO:"));
                card.add(txtDesc);

                // Disponibilità
                TextArea txtDisp = new TextArea();
                txtDisp.getElement().setId("input-modifica-disponibilita");
                txtDisp.setText(skill.getDisponibilita());
                txtDisp.setWidth("100%");
                card.add(new Label("DISPONIBILITÀ:"));
                card.add(txtDisp);

                // Bottone Conferma
                Button btnConferma = new Button("Conferma");
                btnConferma.getElement().setId("btn-modifica-conferma");

                btnConferma.addClickHandler(confermaEvent -> {
                    if (txtTitolo.getText().trim().isEmpty() || txtDesc.getText().trim().isEmpty()
                            || txtDisp.getText().trim().isEmpty()) {
                        Window.alert("Tutti i campi sono obbligatori!");
                        return;
                    }

                    // Aggiorna localmente l'oggetto con i valori inseriti dall'utente nel form
                    skill.setTitolo(txtTitolo.getText().trim());
                    skill.setCategoria(listCat.getSelectedItemText());
                    skill.setOffro(txtDesc.getText().trim());
                    skill.setDisponibilita(txtDisp.getText().trim());

                    adService.aggiornaAnnuncio(skill.getId(), skill, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert("Errore di rete: " + caught.getMessage());
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            if (result) {
                                caricaSkillsDalDatabase();
                                mostraDettagliCard(skill);
                            } else {
                                Window.alert("Errore: Impossibile aggiornare l'annuncio nel database.");
                            }
                        }
                    });
                });

                HorizontalPanel confWrapper = new HorizontalPanel();
                confWrapper.setWidth("100%");
                confWrapper.add(btnConferma);
                confWrapper.setCellHorizontalAlignment(btnConferma, HasHorizontalAlignment.ALIGN_RIGHT);
                card.add(confWrapper);
            });

            buttonGroups.add(btnRimuovi);
            buttonGroups.add(btnModifica);
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
