package TU.proovitoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ProovitooApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProovitooApplication.class);
	}

	@Bean
	public ServletRegistrationBean<DHtmlLayoutServlet> dHtmlLayoutServlet() {
		ServletRegistrationBean<DHtmlLayoutServlet> reg = new ServletRegistrationBean<>(new DHtmlLayoutServlet(), "*.zul", "*.zhtml");
		reg.setLoadOnStartup(1);
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("update-uri", "/zkau");
		reg.setInitParameters(initParameters);
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return reg;
	}

	@Bean
	public ServletRegistrationBean<DHtmlUpdateServlet> dHtmlUpdateServlet() {
		ServletRegistrationBean<DHtmlUpdateServlet> reg = new ServletRegistrationBean<>(new DHtmlUpdateServlet(), "/zkau/*");
		reg.setLoadOnStartup(2);
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return reg;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProovitooApplication.class, args);
	}
}
