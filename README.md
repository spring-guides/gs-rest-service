Getting Started: Creating a REST Endpoint
=========================================

This Getting Started guide will walk you through the process of creating a simple REST endpoint using Spring.

To help you get started, we've provided an initial project structure for you in GitHub:

```sh
$ git clone https://github.com/springframework-meta/gs-rest-service.git
```

As you work through this guide, you can fill in this project with the code necessary to complete the guide. Or, if you'd prefer to see the end result, the completed project is available in the *completed* branch of the Git repository:

```sh
$ git clone -b completed https://github.com/springframework-meta/gs-rest-service.git
```

Before we can write the REST endpoint itself, there's some initial project setup that's required. Or, you can skip straight to the [fun part]().

Selecting Dependencies
----------------------
The sample in this Getting Started Guide will leverage Spring MVC and the Jackson JSON processor. Therefore, you'll need to declare the following library dependencies in your build:

  - org.springframework:spring-webmvc:3.2.2.RELEASE
  - com.fasterxml.jackson.core:jackson-core:2.1.4

Click here for details on how to map these dependencies to your specific build tool.

Setting Up DispatcherServlet
----------------------------
Spring REST endpoints are built as Spring MVC controllers. Therefore, we'll need to be sure that Spring's DispatcherServlet is configured. We can do that by creating a web application initializer class:

```java
package hello;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class HelloWorldWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { HelloWorldConfiguration.class };
	}

}
```

By extending AbstractAnnotationConfigDispatcherServletInitializer, our web application initializer will get a DispatcherServlet that is configured with @Configuration-annotated classes. All we must do is tell it where those configuration classes are and what path(s) to map DispatcherServlet to. 

With regard to the servlet path mappings, getServletMappings() returns a single-entry array of String specifying that DispatcherServlet should be mapped to "/".

The getRootConfigClasses() and getServletConfigClasses() methods specify the configuration classes. The Class array returned from getRootConfigClasses() specifies the classes for the root context provided to ContextLoaderListener. Similarly, the Class array returned from getServletConfigClasses() specifies the classes for the servlet application context provided to DispatcherServlet. 

For our purposes there will only be a servlet application context, so getRootConfigClasses() returns null. getServletConfigClasses(), however, specifies HelloWorldConfiguration as the only configuration class.

Creating a Configuration Class
------------------------------
Now that we have setup DispatcherServlet to handle requests for our application, we need to configure the Spring application context used by DispatcherServlet.

In our Spring configuration, we'll need to enable annotation-oriented Spring MVC. And we'll also need to tell Spring where it can find our endpoint controller class. The following configuration class takes care of both of those things:

```java
package hello;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan
public class HelloWorldConfiguration {
}
```
	
The @EnableWebMvc annotation turns on annotation-oriented Spring MVC. And we've also annotated the configuration class with @ComponentScan to have it look for components (including controllers) in the hello package.

Creating a Representation Class
-------------------------------
With the essential Spring MVC configuration out of the way, it's time to get to the nuts and bolts of our REST endpoint by creating a resource representation class and an endpoint controller.

Before we get too carried away with building the endpoint controller, we need to give some thought to what our API will look like. 

What we want is to handle GET requests for /hello-world, optionally with a name query parameter. In response to such a request, we'd like to send back JSON, representing a greeting, that looks something like this:

```json
{
	"id": 1,
	"content": "Hello, stranger!"
}
```
	
The id field is a unique identifier for the greeting, and content is the textual representation of the greeting.

To model the greeting representation, we’ll create a representation class:

```java
package hello;

public class Greeting {

	private final long id;
	private final String content;

	public Greeting(long id, String content) {
	this.id = id;
	this.content = content;
	}

	public long getId() {
	return id;
	}

	public String getContent() {
	return content;
	}

}
```

Now that we've got our representation class, let's create the endpoint controller that will serve it.

Creating a Resource Controller
------------------------------
In Spring, REST endpoints are just Spring MVC controllers. The following Spring MVC controller handles a GET request for /hello-world and returns our Saying resource:

```java
package hello;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello-world")
public class HelloWorldController {
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody Greeting sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
	
}
```

The key difference between a human-facing controller and a REST endpoint controller is in how the response is created. Rather than rely on a view (such as JSP) to render model data in HTML, an endpoint controller simply returns the data to be written directly to the body of the response. 

The magic is in the @ResponseBody annotation. @ResponseBody tells Spring MVC to not render a model into a view, but rather to write the returned object into the response body. It does this by using one of Spring's message converters.

>__TODO__: briefly talk about what message converters do and list the ones that come out of the box with Spring}


Building and Running the REST Endpoint
--------------------------------------
>**NOTE**: The following section probably needs to be reworked 
	      (and the build file that goes with it) to use a Servlet 3 
	      container (such as a modern Tomcat). At this point, 
	      these steps do not work since the sample code uses a
	      web app initializer instead of web.xml.

All of the pieces of our REST endpoint are in place. All that's left to do is to build it and run it.

To run the sample, issue the following Gradle command:

```sh
$ gradle jettyRun
```
	
This will cause the application to be compiled and for a Jetty server to start on port 8080. You can then point your browser or other REST client (such as Spring's RestTemplate or the Spring REST Shell) at http://localhost:8080/HelloWorldRest/hello-world to see the result. Or you can try specifying a name parameter as in http://localhost:8080/HelloWorldRest/hello-world?name=Craig.

If you simply want to build the code into a WAR file that you can deploy in your own server, issue the following Gradle command:

```sh
$ gradle build
```


Next Steps
----------
Congratulations! You have just developed a simple REST endpoint using Spring. This is a basic foundation for building a complete REST API in Spring. 

There's more to building REST APIs than is covered here. You may want to continue your exploration of Spring and REST with the following Getting Started guides:

* Handling POST, PUT, and GET requests in REST endpoints
* Creating self-describing APIs with HATEOAS
* Securing a REST endpoint with HTTP Basic
* Securing a REST endpoint with OAuth
* Consuming REST APIs
* Testing REST APIs


