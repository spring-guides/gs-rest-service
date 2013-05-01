package hello;

import java.util.HashSet;

import org.apache.catalina.Context;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.SpringServletContainerInitializer;

public class Main {

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.setBaseDir(".");
		tomcat.getHost().setAppBase("/");

		// Add AprLifecycleListener
		StandardServer server = (StandardServer)tomcat.getServer();
		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);

		Context context = tomcat.addWebapp("/", "/");
		HashSet<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(HelloWorldWebAppInitializer.class);
		context.addServletContainerInitializer(new SpringServletContainerInitializer(), classes);
		tomcat.start();
		tomcat.getServer().await();
	}
	
}
