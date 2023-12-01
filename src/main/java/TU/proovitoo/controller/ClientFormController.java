package TU.proovitoo.controller;

import TU.proovitoo.model.Client;
import TU.proovitoo.model.Country;
import TU.proovitoo.model.User;
import TU.proovitoo.service.ClientService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.List;

import static TU.proovitoo.utils.Utils.loadClients;
import static TU.proovitoo.utils.Utils.showSuccessMessage;

public class ClientFormController extends SelectorComposer<Component> {
    @Wire
    private Textbox firstName;
    @Wire
    private Textbox lastName;
    @Wire
    private Textbox username;
    @Wire
    private Textbox email;
    @Wire
    private Textbox address;
    @Wire
    private Combobox country;
    @Wire
    private Window clientFormWindow;
    @Wire
    private Button deleteButton;
    @Wire
    private Button modifyButton;
    private Listbox clientsListbox;
    private ClientService clientService;
    @Autowired
    private ClientService countryService;
    private Client client;
    private MainPageController.OperationType action;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        ServletContext servletContext = Executions.getCurrent().getDesktop().getWebApp().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.clientService = ctx.getBean(ClientService.class);

        List<Country> countries = clientService.getAllCountries();
        country.setModel(new ListModelList<>(countries));
        country.setItemRenderer(new ComboitemRenderer<Country>() {
            @Override
            public void render(Comboitem item, Country country, int index) throws Exception {
                item.setLabel(country.getName());
                item.setValue(country);
            }
        });
        this.client = (Client) Executions.getCurrent().getArg().get("client");
        this.action = (MainPageController.OperationType) Executions.getCurrent().getArg().get("actionCommand");
        this.clientsListbox = (Listbox) Executions.getCurrent().getArg().get("clientsListbox");
        this.deleteButton = (Button) Executions.getCurrent().getArg().get("deleteButton");
        this.modifyButton = (Button) Executions.getCurrent().getArg().get("modifyButton");
    }
    @Listen("onClick = #doneButton")
    public void onDoneClicked() {
        if (!validateForm()) {
            return;
        }
            setClientValues();
            boolean addOrModifySuccessful = clientService.addOrModify(client);
            if (addOrModifySuccessful) {
                showSuccessMessage(action);
                refreshClients();
            }
            clientFormWindow.detach();
    }
    private boolean validateForm() {
        boolean isFirstNameValid = validateNameField(firstName);
        boolean isLastNameValid = validateNameField(lastName);
        boolean isUsernameValid = validateField(username, !username.getValue().trim().isEmpty() && username.getValue().trim().length() >= 5, "Username must have at least 5 characters");
        boolean isEmailValid = email.getValue().trim().isEmpty() || validateField(email, email.getValue().contains("@"), "You probably forgot @");
        boolean isAddressValid = validateField(address, !address.getValue().trim().isEmpty(), "I don't believe that this is your address");
        boolean isCountryValid = validateField(country, !country.getValue().trim().isEmpty(), "You cannot leave this empty");

        return isFirstNameValid && isLastNameValid && isUsernameValid && isEmailValid && isAddressValid && isCountryValid;
    }

    private boolean validateNameField(Textbox field) {
        if (field.getValue().trim().isEmpty()) {
            setFieldError(field, "You cannot leave this empty");
            return false;
        } else if (!isValidName(field.getValue())) {
            setFieldError(field, "You probably don't have these symbols in the name");
            return false;
        } else if (field.getValue().trim().length() < 2) {
            setFieldError(field, "You probably have a longer name");
            return false;
        }
        return true;
    }

    private boolean validateField(Textbox field, boolean condition, String errorMsg) {
        if (!condition) {
            setFieldError(field, errorMsg);
            return false;
        }
        return true;
    }

    private void setFieldError(Textbox field, String errorMsg) {
        Clients.showNotification(errorMsg, "warning", field, "end_center", 5000);
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-ZäöüõÄÖÜÕ]+");
    }

    private void setClientValues() {
        client.setFirstName(firstName.getValue());
        client.setLastName(lastName.getValue());
        client.setUsername(username.getValue());
        client.setEmail(email.getValue());
        client.setAddress(address.getValue());
        client.setCountry(country.getValue());
        if(client.getUser()==null){
            User authenticatedUser = (User) Sessions.getCurrent().getAttribute("authenticatedUser");
            client.setUser(authenticatedUser);
        }
    }

    private void refreshClients() {
        Long userId = findUserId();
        List<Client> clients = clientService.getClientsByUserId(userId);
        loadClients(clientsListbox, clients, deleteButton, modifyButton);
    }

    private static Long findUserId() {
        User authenticatedUser = (User) Sessions.getCurrent().getAttribute("authenticatedUser");
        return authenticatedUser.getId();
    }
}
