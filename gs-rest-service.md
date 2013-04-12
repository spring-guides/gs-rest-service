Getting Started: Creating a REST Endpoint
=========================================

This Getting Started guide will walk you through the process of creating a simple REST endpoint using Spring.

Setting up Gradle
-----------------
We recommend you use Gradle for your Spring projects. If you’re a big Ant / Ivy, Buildr, Gradle, SBT, Leiningen, or Gant fan, that’s cool, but we use Gradle and we’ll be using Gradle in this guide. If you have any questions about how Gradle works, Building and Testing with Gradle (O'Reilly) should have what you’re looking for. (We’re assuming you know how to create a new Gradle project. If not, you can use this to get started.)

The following build.gradle file has everything we'll need for our project.

```groovy
apply plugin: 'java'
apply plugin: 'jetty'

repositories { mavenCentral() }

dependencies {
	compile "org.springframework:spring-webmvc:3.2.2.RELEASE"
	compile "org.codehaus.jackson:jackson-mapper-asl:1.9.9"
}
```

Spring's REST support is based on Spring MVC. Therefore, we must add spring-webmvc as a dependency to our project. Also, so that our endpoints can produce JSON output, we needed to include the Jackson JSON library.

Each of these dependencies has dependencies of their own that will transitively be resolved.

We've also included the 'jetty' plugin so that we can easily run and test our code via Gradle.


Setting up DispatcherServlet
----------------------------
Spring REST endpoints are built as Spring MVC controllers. Therefore, we'll need to be sure that Spring's DispatcherServlet is configured in our application's /WEB-INF/web.xml:

```xml
<servlet>
	<servlet-name>appServlet</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextClass</param-name>
		<param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
	</init-param>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>org.springframework.hello.config</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
	
<servlet-mapping>
	<servlet-name>appServlet</servlet-name>
	<url-pattern>/</url-pattern>
</servlet-mapping>
```

Here we've configured DispatcherServlet to use AnnotationConfigWebApplicationContext as the context class so that our configuration can be expressed in Java, rather than XML. We've also set told DispatcherServlet (via the contextConfigLocation initialization parameter) that it can find our configuration classes in the org.springframework.hello.config package.

Now let's create the configuration class.


Creating a Configuration Class
------------------------------
In our Spring configuration, we'll need to enable annotation-oriented Spring MVC. And we'll also need to tell Spring where it can find our endpoint controller class. The following configuration class takes care of both of those things:


```java
package org.springframework.hello.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="org.springframework.hello", 
	              excludeFilters=@Filter(Configuration.class))
public class HelloWorldConfiguration {
}
```
	
The @EnableWebMvc annotation turns on annotation-oriented Spring MVC. And we've also annotated the configuration class with @ComponentScan to have it look for components (including controllers) in the org.springframework.hello package. As it turns out, classes that are annotated with @Configuration are also discovered by component scanning, so we had to specify an exclude filter to keep it from discovering and using our configuration class a second time.

With configuration details completed, now it's time to start writing code for our endpoint.

Creating a Representation Class
-------------------------------
Before we get too carried away with building the endpoint controller, we need to give some thought to what our API will look like. 

What we want is to handle GET requests for /hello-world, optionally with a name query parameter. In response to such a request, we'd like to send back JSON looking something like this:

```json
{
	"id": 1,
	"content": "Hello, stranger!"
}
```
	
The id field is a unique identifier for the saying, and content is the textual representation of the saying.

To model this representation, we’ll create a representation class:

```java
package org.springframework.hello;

public class Saying {

	private final long id;
	private final String content;

	public Saying(long id, String content) {
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
package org.springframework.hello;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello-world")
public class HelloWorldResource {
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody Saying sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
		return new Saying(counter.incrementAndGet(), String.format(template, name));
	}
	
}
```

The key difference between a human-facing controller and a REST endpoint controller is in how the response is created. Rather than rely on a view (such as JSP) to render model data in HTML, an endpoint controller simply returns the data to be written directly to the body of the response. 

The magic is in the @ResponseBody annotation. @ResponseBody tells Spring MVC to not render a model into a view, but rather to write the returned object into the response body. It does this by using one of Spring's message converters.

{TODO: briefly talk about what message converters do and list the ones that come out of the box with Spring}


Building and Running the REST Endpoint
--------------------------------------
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


