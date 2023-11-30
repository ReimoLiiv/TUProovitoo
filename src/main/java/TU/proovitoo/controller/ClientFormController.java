package TU.proovitoo.controller;

import TU.proovitoo.model.Client;
import TU.proovitoo.model.User;
import TU.proovitoo.service.ClientService;
import jakarta.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
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
    private Textbox country;
    @Wire
    private Window clientFormWindow;
    @Wire
    private Button deleteButton;
    @Wire
    private Button modifyButton;
    private Listbox clientsListbox;
    private ClientService clientService;
    private Client client;
    private MainPageController.OperationType action;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        ServletContext servletContext = Executions.getCurrent().getDesktop().getWebApp().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.clientService = ctx.getBean(ClientService.class);

        this.client = (Client) Executions.getCurrent().getArg().get("client");
        this.action = (MainPageController.OperationType) Executions.getCurrent().getArg().get("actionCommand");
        this.clientsListbox = (Listbox) Executions.getCurrent().getArg().get("clientsListbox");
        this.deleteButton = (Button) Executions.getCurrent().getArg().get("deleteButton");
        this.modifyButton = (Button) Executions.getCurrent().getArg().get("modifyButton");
    }
    @Listen("onClick = #doneButton")
    public void onDoneClicked() {
        if (isAllFieldsFilled()) {
            setClientValues();
            boolean addOrModifySuccessful = clientService.addOrModify(client);
            if (addOrModifySuccessful) {
                showSuccessMessage(action);
                refreshClients();
            }
            clientFormWindow.detach();
        } else {
            Messagebox.show("Please fill mandatory fields!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
        }
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

    private boolean isAllFieldsFilled() {
        return !firstName.getValue().trim().isEmpty() &&
                !lastName.getValue().trim().isEmpty() &&
                !username.getValue().trim().isEmpty() &&
                !address.getValue().trim().isEmpty() &&
                !country.getValue().trim().isEmpty();
    }
}
