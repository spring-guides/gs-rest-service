package hello;

import org.springframework.bootstrap.SpringApplication;
import org.springframework.bootstrap.context.annotation.EnableAutoConfiguration;
import org.springframework.bootstrap.web.BootstrapServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application extends BootstrapServletInitializer {

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { Application.class };
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
