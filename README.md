# Getting Started Building a RESTful Web Service

Introduction
------------
This Getting Started guide will walk you through the process of creating a simple REST service using Spring.

To help you get started, we've provided an initial project structure as well as the completed project for you in GitHub:

```sh
$ git clone https://github.com/springframework-meta/gs-rest-service.git
```

In the `start` folder, you'll find a bare project, ready for you to copy-n-paste code snippets from this document. In the `complete` folder, you'll find the complete project code.

Before we can write the REST service itself, there's some initial project setup that's required. Or, you can skip straight to the [fun part](#creating-a-representation-class).


Adding dependencies
-------------------
First you'll need to set up a basic build script. You can use any build system you like, but we've included snippets for [Maven](https://maven.apache.org) and [Gradle](http://gradle.org) here. If you're not familiar with either of these, you can refer to our [Getting Started with Maven](../gs-maven/README.md) or [Getting Started with Gradle](../gs-gradle/README.md) guides.

Add the [Spring MVC](TODO) and [Jackson](http://jackson.codehaus.org) JSON libraries as dependencies:

### Maven \[[copy complete `pom.xml` to clipboard](start/pom.xml)\]

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>3.2.2.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
        <version>2.1.4</version>
    </dependency>
</dependencies>
```

### Gradle \[[copy complete `build.gradle` to clipboard](start/build.gradle)\]

```groovy
compile 'org.springframework:spring-webmvc:3.2.2.RELEASE'
compile 'com.fasterxml.jackson.core:jackson-core:2.1.4'
```


Setting up the Spring DispatcherServlet
------------------------------------------
Spring-based RESTful web services are built as Spring MVC controllers. Therefore, we'll need to be sure that Spring's [`DispatcherServlet`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) is configured. We can do that by creating a [`WebApplicationInitializer`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/WebApplicationInitializer.html) class:

`src/main/java/gs/HelloWorldWebAppInitializer.java`
```java
package gs;

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

By extending [`AbstractAnnotationConfigDispatcherServletInitializer`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/support/AbstractAnnotationConfigDispatcherServletInitializer.html), our web application initializer will get a `DispatcherServlet` that is configured with [`@Configuration`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/context/annotation/Configuration.html)-annotated classes. All we must do is tell it where those configuration classes are and what path(s) to map `DispatcherServlet` to. 

With regard to the servlet path mappings, `getServletMappings()` returns a single-entry array of `String` specifying that `DispatcherServlet` should be mapped to "/".

The `getRootConfigClasses()` and `getServletConfigClasses()` methods specify the configuration classes. The `Class` array returned from `getRootConfigClasses()` specifies the classes for the root context provided to [`ContextLoaderListener`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/context/ContextLoaderListener.html). Similarly, the `Class` array returned from `getServletConfigClasses()` specifies the classes for the servlet application context provided to `DispatcherServlet`. 

For our purposes there will only be a servlet application context, so `getRootConfigClasses()` returns `null`. `getServletConfigClasses()`, however, specifies `HelloWorldConfiguration` as the only configuration class.


Creating a Configuration Class
------------------------------
Now that we have setup `DispatcherServlet` to handle requests for our application, we need to configure the Spring application context used by `DispatcherServlet`.

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
	
The [`@EnableWebMvc`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/config/annotation/EnableWebMvc.html) annotation turns on annotation-oriented Spring MVC. And we've also annotated the configuration class with [`@ComponentScan`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/context/annotation/ComponentScan.html) to have it look for components (including controllers) in the `hello` package.


Creating a Representation Class
-------------------------------
With the essential Spring MVC configuration out of the way, it's time to get to the nuts and bolts of our REST service by creating a resource representation class and an endpoint controller.

Before we get too carried away with building the endpoint controller, we need to give some thought to what our API will look like. 

What we want is to handle GET requests for /hello-world, optionally with a name query parameter. In response to such a request, we'd like to send back JSON, representing a greeting, that looks something like this:

```json
{
	"id": 1,
	"content": "Hello, stranger!"
}
```
	
The `id` field is a unique identifier for the greeting, and `content` is the textual representation of the greeting.

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
In Spring, REST endpoints are just Spring MVC controllers. The following Spring MVC controller handles a GET request for /hello-world and returns our `Greeting` resource:

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

The magic is in the [`@ResponseBody`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/bind/annotation/ResponseBody.html) annotation. `@ResponseBody` tells Spring MVC to not render a model into a view, but rather to write the returned object into the response body. It does this by using one of Spring's message converters. Because Jackson 2 is in the classpath, this means that [`MappingJackson2HttpMessageConverter`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.html) will handle the conversion of Greeting to JSON if the request's `Accept` header specifies that JSON should be returned.

Building and Running the REST Service
-------------------------------------
>**NOTE**: This section is a very important section, because it shows the user how all of the work done up to this point comes together and runs. The challenge here, however, is that there's no *easy* way to run this application. Gradle's Jetty plugin seems easy and natural, but it uses an older, non-Servlet 3 version of Jetty, so the application initializer will not work. There is a Gradle Tomcat plugin, but it's quite involved setup-wise. And loading this into any IDE and running it is far more involved than either of the Gradle-based options. It would be really nice to leverage Spring Bootstrap/Catalyst for running the sample. That's likely what will happen, but at this point it's too risky of an option until bootstrap/catalyst stabilizes.

Next Steps
----------
Congratulations! You have just developed a simple REST service using Spring. This is a basic foundation for building a complete REST API in Spring. 

There's more to building REST services than is covered here. You may want to continue your exploration of Spring and REST with the following Getting Started guides:

* Handling POST, PUT, and GET requests in REST services
* Creating self-describing APIs with HATEOAS
* Securing a REST service with HTTP Basic
* Securing a REST service with OAuth
* [Consuming REST services](https://github.com/springframework-meta/gs-consuming-rest-core/blob/master/README.md)
* Testing REST services


