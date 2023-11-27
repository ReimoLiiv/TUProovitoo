package TU.proovitoo.controller;

import TU.proovitoo.model.User;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class MainPageController extends SelectorComposer<Window> {
    @Wire
    private Label greetingLabel;
    @Wire
    private Label clientsTitle;
    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);

        Session session = Sessions.getCurrent();
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");

        if (authenticatedUser == null) {
            Executions.sendRedirect("/login.zul");
            return;
        }
        comp.setTitle("Greetings, " + authenticatedUser.getName());
        clientsTitle.setValue("Clients:");
    }
    @Listen("onClick = #logoutButton")
    public void onLogoutClicked() {
        Session session = Sessions.getCurrent();
        session.removeAttribute("authenticatedUser");
        Executions.sendRedirect("/login.zul");
    }
}