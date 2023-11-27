package TU.proovitoo.controller;

import TU.proovitoo.service.AuthenticationService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Executions;
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

    @Listen("onClick = #loginButton")
    public void onLoginClicked() {
        String user = username.getValue();
        String pass = password.getValue();

        if (authService.authenticate(user, pass)) {
            Executions.sendRedirect("/clients.zul");
        } else {
            Messagebox.show("Invalid username or password", "Login Error", Messagebox.OK, Messagebox.ERROR);
        }
    }
}
