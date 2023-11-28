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
    @Wire
    private Label greetingLabel;
    @Wire
    private Listbox clientsListbox;
    @Wire
    private Label clientsTitle;

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
        clientsListbox.getItems().clear(); // Clear existing items

        for (Client client : clients) {
            Listitem listItem = new Listitem();
            listItem.appendChild(new Listcell(client.getFirstName()));
            listItem.appendChild(new Listcell(client.getLastName()));
            listItem.appendChild(new Listcell(client.getUsername()));
            listItem.appendChild(new Listcell(client.getEmail()));
            listItem.appendChild(new Listcell(client.getAddress()));
            listItem.appendChild(new Listcell(client.getCountry()));

            clientsListbox.appendChild(listItem);
        }
    }
    @Listen("onClick = #logoutButton")
    public void onLogoutClicked() {
        Session session = Sessions.getCurrent();
        session.removeAttribute("authenticatedUser");
        Executions.sendRedirect("/login.zul");
    }
}