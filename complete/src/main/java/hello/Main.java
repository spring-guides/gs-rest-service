package hello;

import java.util.HashSet;

import org.apache.catalina.Context;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

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
		
		// TODO: The following line is necessary to run hello.Main in Eclipse or from 'gradlew run'.
		//       But when the executable JAR produced by Maven Shade plugin is run, it results in
		//       an error from the DispatcherServlet being loaded twice with the name "dispatcher".
		//       Need to find a way that works equally well with 'gradlew run'/Eclipse and the
		//       executable JAR.
//		context.addServletContainerInitializer(new SpringServletContainerInitializer(), classes);
		tomcat.start();
		tomcat.getServer().await();
	}
	
}
