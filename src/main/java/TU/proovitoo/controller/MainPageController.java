package TU.proovitoo.controller;

import TU.proovitoo.model.Client;
import TU.proovitoo.model.User;
import TU.proovitoo.service.ClientService;
import jakarta.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static TU.proovitoo.utils.Utils.showSuccessMessage;

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
        openClientForm(new Client(), OperationType.ADD);
    }

    @Listen("onClick = #modifyButton")
    public void onModifyClicked() {
        Listitem selectedItem = clientsListbox.getSelectedItem();
        if (selectedItem != null) {
            Client selectedClient = selectedItem.getValue();
            openClientForm(selectedClient, OperationType.MODIFY);
        }
    }

    @Listen("onClick = #deleteButton")
    public void onDeleteClicked() {
        showDeletionConfirmation();
    }

    private void showDeletionConfirmation() {
        Messagebox.show(
                "Are you sure you want to delete this client?",
                "Confirm Deletion",
                new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.NO},
                new String[] {"YES, I want to delete", "OOPS, don't delete"},
                Messagebox.QUESTION,
                Messagebox.Button.NO,
                this::handleDeletionConfirmationResponse
        );
    }

    private void handleDeletionConfirmationResponse(Event event) {
        if ("onYes".equals(event.getName())) {
            deleteSelectedClient();
        }
    }

    private void deleteSelectedClient() {
        Client selectedClient = (clientsListbox.getSelectedItem()).getValue();
        boolean deletionSuccessful = clientService.deleteClient(selectedClient.getId());

        User authenticatedUser = (User) Sessions.getCurrent().getAttribute("authenticatedUser");
        if (authenticatedUser != null && deletionSuccessful) {
            loadClients(authenticatedUser.getId());
            showSuccessMessage(OperationType.DELETE);
        }
    }

    private void openClientForm(Client client, OperationType operationType) {
        Map<String, Object> args = new HashMap<>();
        args.put("client", client);
        args.put("actionCommand", operationType);
        args.put("clientsListbox", clientsListbox);
        args.put("deleteButton", deleteButton);
        args.put("modifyButton", modifyButton);

        Window currentWindow = getSelf();
        Window window = (Window) Executions.createComponents("clientForm.zul", currentWindow, args);
        window.doModal();
        window.setPosition("center");
    }
    private void loadClients(Long userId) {
        List<Client> clients = clientService.getClientsByUserId(userId);
        clientService.loadClients(clientsListbox, clients, deleteButton, modifyButton);
    }
}