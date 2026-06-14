package it.unibo;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainLayoutGui extends Composite {
    public MainLayoutGui() {
        VerticalPanel mainContainer = new VerticalPanel();
        mainContainer.setWidth("100%");
        mainContainer.setHeight("100%");
        mainContainer.getElement().getStyle().setProperty("padding", "20px");

        initWidget(mainContainer);
    }
}