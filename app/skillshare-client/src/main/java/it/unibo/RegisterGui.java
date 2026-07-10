package it.unibo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class RegisterGui{

    private final RegisterServiceAsync registerService = GWT.create(RegisterService.class);

    public void mostra(){
        RootPanel.get().clear();

        HTML title = new HTML("<h1 style='color: #007BFF;'>Registrazione a SkillShare</h1>");
        title.getElement().setId("titolo-registrazione");

        final TextBox usernameField = new TextBox();
        usernameField.getElement().setAttribute("placeholder", "username");
        usernameField.getElement().setId("input-username");
        usernameField.setWidth("300px");

        final PasswordTextBox passwordField = new PasswordTextBox();
        passwordField.getElement().setAttribute("placeholder", "password");
        passwordField.getElement().setId("input-password");
        passwordField.setWidth("300px");

        final PasswordTextBox confirmPasswordField = new PasswordTextBox();
        confirmPasswordField.getElement().setAttribute("placeholder", "conferma password");
        confirmPasswordField.getElement().setId("input-confirm-password");
        confirmPasswordField.setWidth("300px");

        final Button registerButton = new Button("CREA ACCOUNT");
        registerButton.getElement().setId("register-button");
        registerButton.setWidth("150px");

        final Button clearButton = new Button("ANNULLA");
        clearButton.getElement().setId("cancel-button");
        clearButton.setWidth("150px");

        final Button loginPageButton = new Button("TORNA ALLA PAGINA DI LOGIN");
        loginPageButton.getElement().setId("login-page-button");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);

        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(10);
        mainPanel.setWidth("100%");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(title);
        mainPanel.add(new HTML("<h2>Inserisci username e password:</h2>"));
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(confirmPasswordField);
        mainPanel.add(buttonPanel);

        RootPanel.get().add(mainPanel);

        //imposta il focus su username
        usernameField.setFocus(true);
        usernameField.selectAll();

        //logica Handler
        class RegisterHandler implements KeyUpHandler, ClickHandler{
            public void onClick(ClickEvent event) {
                register();
            }

            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    register();
                }
            }

            private void register(){
                String username = usernameField.getText();
                String password = passwordField.getText();
                String confirm_password = confirmPasswordField.getText();

                registerService.register(username, password, confirm_password, new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("errore di rete");
                    }

                    @Override
                    public void onSuccess(String result) {
                        if ("Username già usato".equals(result)) {
                            Window.alert("Username già usato");
                        } else if ("Username troppo corto".equals(result)) {
                            Window.alert("Username troppo corto");
                        } else if ("Password troppo corta".equals(result)) {
                            Window.alert("Password troppo corta");
                        } else if ("Password non conforme".equals(result)) {
                            Window.alert("Password non conforme");
                        } else {
                            buttonPanel.clear();
                            Window.alert("Registrazione Completata");

                            buttonPanel.add(loginPageButton);
                            loginPageButton.setWidth("200px");
                        }
                    }
                });
            }
        }

        RegisterHandler registerHandler = new RegisterHandler();
        registerButton.addClickHandler(registerHandler);
        usernameField.addKeyUpHandler(registerHandler);
        passwordField.addKeyUpHandler(registerHandler);
        confirmPasswordField.addKeyUpHandler(registerHandler);

        clearButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                
                new LoginGui().mostra(); 
            }
        });

        loginPageButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                new LoginGui().mostra(); 
            }
        });
    }
}