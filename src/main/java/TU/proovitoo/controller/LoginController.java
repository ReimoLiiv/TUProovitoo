package TU.proovitoo.controller;

import TU.proovitoo.model.User;
import TU.proovitoo.service.AuthenticationService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
@Component
public class LoginController extends SelectorComposer<Window> {
    @Wire
    private Textbox username;
    @Wire
    private Textbox password;

    private AuthenticationService authService;

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        ServletContext servletContext = Executions.getCurrent().getDesktop().getWebApp().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.authService = ctx.getBean(AuthenticationService.class);
    }

    @Listen("onClick = #loginButton; onOK = #loginWindow")
    public void onLoginClicked() {
        String user = username.getValue();
        String pass = password.getValue();

        User authenticatedUser = authService.authenticate(user, pass);
        if (authenticatedUser != null) {
            // User is authenticated
            Session session = Sessions.getCurrent();
            session.setAttribute("authenticatedUser", authenticatedUser);
            Executions.sendRedirect("/mainPage.zul");
        } else {
            Messagebox.show("Invalid username or password", "Login Error", Messagebox.OK, Messagebox.ERROR);
        }
    }


}
