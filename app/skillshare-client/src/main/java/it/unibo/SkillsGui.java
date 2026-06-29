package it.unibo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SkillsGui extends Composite{
    
    private VerticalPanel mainPanel = new VerticalPanel();
    private final VerticalPanel sidebar = new VerticalPanel();

    public SkillsGui() {
        initWidget(mainPanel);
        // 1. Titolo Pagina
        Label title = new Label("Le Mie Skill");
        title.getElement().setId("titolo-skills-dashboard");
        mainPanel.add(title);

        // 2. Sidebar e bottoni con gli ID che Selenium cerca
        Button btnMieSkills = new Button("Mie Skills");
        btnMieSkills.getElement().setId("sidebar-mie-skills");
        
        Button btnAccettate = new Button("Skills Accettate");
        btnAccettate.getElement().setId("sidebar-skills-accettate");
        
        Button btnConcluse = new Button("Skills Concluse");
        btnConcluse.getElement().setId("sidebar-skills-concluse");

        sidebar.add(btnMieSkills);
        sidebar.add(btnAccettate);
        sidebar.add(btnConcluse);
        mainPanel.add(sidebar);
    }
}

