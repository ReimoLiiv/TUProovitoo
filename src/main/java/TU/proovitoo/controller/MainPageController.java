package TU.proovitoo.controller;

import TU.proovitoo.model.Client;
import TU.proovitoo.model.User;
import TU.proovitoo.service.AuthenticationService;
import TU.proovitoo.service.ClientService;
import jakarta.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.List;

public class MainPageController extends SelectorComposer<Window> {

    public enum OperationType {
        ADD,
        DELETE,
        MODIFY
    }
    @Wire
    private Label greetingLabel;
    @Wire
    private Listbox clientsListbox;
    @Wire
    private Label clientsTitle;
    @Wire
    private Button addButton;
    @Wire
    private Button deleteButton;
    @Wire
    private Button modifyButton;

    private ClientService clientService;

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        Session session = Sessions.getCurrent();
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");

        ServletContext servletContext = Executions.getCurrent().getDesktop().getWebApp().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.clientService = ctx.getBean(ClientService.class);

        if (authenticatedUser == null) {
            Executions.sendRedirect("/login.zul");
            return;
        }
        comp.setTitle("Greetings, " + authenticatedUser.getName());
        clientsTitle.setValue("Clients:");
        loadClients(authenticatedUser.getId());
    }

    private void loadClients(Long userId) {
        List<Client> clients = clientService.getClientsByUserId(userId);
        clientsListbox.getItems().clear();

        for (Client client : clients) {
            Listitem listItem = new Listitem();
            listItem.appendChild(new Listcell(client.getFirstName()));
            listItem.appendChild(new Listcell(client.getLastName()));
            listItem.appendChild(new Listcell(client.getUsername()));
            listItem.appendChild(new Listcell(client.getEmail()));
            listItem.appendChild(new Listcell(client.getAddress()));
            listItem.appendChild(new Listcell(client.getCountry()));
            listItem.setValue(client);
            clientsListbox.appendChild(listItem);
        }
    }
    @Listen("onClick = #logoutButton")
    public void onLogoutClicked() {
        Session session = Sessions.getCurrent();
        session.removeAttribute("authenticatedUser");
        Executions.sendRedirect("/login.zul");
    }
    @Listen("onSelect = #clientsListbox")
    public void onClientSelected() {
        boolean hasSelection = clientsListbox.getSelectedItem() != null;
        deleteButton.setDisabled(!hasSelection);
        modifyButton.setDisabled(!hasSelection);
    }
    @Listen("onClick = #addButton")
    public void onAddClicked() {
        // Add logic
    }
    @Listen("onClick = #modifyButton")
    public void onModifyClicked() {
        // Modify logic
    }
    @Listen("onClick = #deleteButton")
    public void onDeleteClicked() {
        Messagebox.show("Are you sure you want to delete this client?",
                "Confirm Deletion",
                new Messagebox.Button[]{
                        Messagebox.Button.YES,
                        Messagebox.Button.NO
                },
                new String[]{"YES, I want to delete", "OOPS, don't delete"},
                Messagebox.QUESTION,
                Messagebox.Button.NO,
                event -> {
                    if (event.getName().equals("onYes")) {
                        Client selectedClient = (clientsListbox.getSelectedItem()).getValue();

                        boolean deletionSuccessful = clientService.deleteClient(selectedClient.getId());

                        User authenticatedUser = (User) Sessions.getCurrent().getAttribute("authenticatedUser");
                        if (authenticatedUser != null) {
                            loadClients(authenticatedUser.getId());

                            if (deletionSuccessful) {
                                showSuccessMessage(OperationType.DELETE);
                            }
                        }
                    }
                }
        );
    }
    private void showSuccessMessage(OperationType operationType) {
        String message;
        switch (operationType) {
            case ADD:
                message = "Client successfully added!";
                break;
            case DELETE:
                message = "Client successfully deleted!";
                break;
            case MODIFY:
                message = "Client details successfully modified!";
                break;
            default:
                throw new IllegalArgumentException("Unknown operation type");
        }

        Messagebox.show(message, "Success", Messagebox.OK, Messagebox.INFORMATION);
    }
}