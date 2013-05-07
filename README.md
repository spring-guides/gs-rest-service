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

Add the following within the `<project>` section of your pom.xml file:

`pom.xml`
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
    <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-catalina</artifactId>
        <version>7.0.39</version>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-embed-core</artifactId>
        <version>7.0.39</version>
    </dependency>
    <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-jasper</artifactId>
        <version>7.0.39</version>
    </dependency>
</dependencies>
```

### Gradle \[[copy complete `build.gradle` to clipboard](start/build.gradle)\]

Add the following within the `dependencies { }` section of your build.gradle file:

`build.gradle`
```groovy
compile 'org.springframework:spring-webmvc:3.2.2.RELEASE'
compile 'com.fasterxml.jackson.core:jackson-core:2.1.4'
```


Creating a Configuration Class
------------------------------
The first step is to set up a simple Spring configuration class. It'll look like this:

`src/main/java/hello/HelloWorldConfiguration.java`

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

This class is concise, but there's plenty going on under the hood. [`@EnableWebMvc`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/config/annotation/EnableWebMvc.html) handles the registration of a number of components that enable Spring's support for annotation-based controllers—you'll build one of those in an upcoming step. And we've also annotated the configuration class with [`@ComponentScan`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/context/annotation/ComponentScan.html) which tells Spring to scan the `hello` package for those controllers (along with any other annotated component classes).


Setting up the Spring DispatcherServlet
------------------------------------------
Spring's [`DispatcherServlet`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html) will do the work of accepting incoming HTTP requests and routing them to our controller. The simplest way to configure and register the `DispatcherServlet` is with a [`WebApplicationInitializer`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/WebApplicationInitializer.html) class as follows:

`src/main/java/hello/HelloWorldWebAppInitializer.java`
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

By extending [`AbstractAnnotationConfigDispatcherServletInitializer`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/servlet/support/AbstractAnnotationConfigDispatcherServletInitializer.html), our web application initializer will get a `DispatcherServlet` that is configured with [`@Configuration`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/context/annotation/Configuration.html)-annotated classes. All we must do is tell it where those configuration classes are and what path(s) to map `DispatcherServlet` to.

With regard to the servlet path mappings, `getServletMappings()` returns a single-entry array of `String` specifying that `DispatcherServlet` should be mapped to "/".

The `getRootConfigClasses()` and `getServletConfigClasses()` methods specify the configuration classes. The `Class` array returned from `getRootConfigClasses()` specifies the classes for the root context provided to [`ContextLoaderListener`](http://static.springsource.org/spring/docs/3.2.x/javadoc-api/org/springframework/web/context/ContextLoaderListener.html). Similarly, the `Class` array returned from `getServletConfigClasses()` specifies the classes for the servlet application context provided to `DispatcherServlet`.

For our purposes there will only be a servlet application context, so `getRootConfigClasses()` returns `null`. `getServletConfigClasses()`, however, specifies `HelloWorldConfiguration` as the only configuration class.


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

`src/main/java/hello/Greeting.java`
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

`src/main/java/hello/HelloWorldController.java`
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


Creating an executable main class
---------------------------------
_**TODO**: explain._

`src/main/java/hello/Main.java`
```java
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
```



Building an executable JAR
--------------------------

Add the following to the <build><plugins> section of your pom.xml file:

`pom.xml`
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>1.6</version>
    <configuration>
        <createDependencyReducedPom>true</createDependencyReducedPom>
        <filters>
            <filter>
                <artifact>*:*</artifact>
                <excludes>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                </excludes>
            </filter>
        </filters>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>hello.Main</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

The following will produce a single executable JAR file containing all necessary dependency classes:

```
$ mvn package
```

_**TODO:** this produces a bunch of the following. Fix?_
```
[WARNING] We have a duplicate javax/el/ExpressionFactory$4.class in /Users/cbeams/.m2/repository/org/apache/tomcat/tomcat-el-api/7.0.39/tomcat-el-api-7.0.39.jar
```


Running the Service
-------------------------------------

```
$ java -jar target/gs-rest-service-0.0.1-SNAPSHOT.jar

... service comes up ...
```

_**TODO:** this fails with the following. Fix._
```
Caused by: java.lang.IllegalArgumentException: Failed to register servlet with name 'dispatcher'.Check if there is another servlet registered under the same name.
        at org.springframework.util.Assert.notNull(Assert.java:112)
        at org.springframework.web.servlet.support.AbstractDispatcherServletInitializer.registerDispatcherServlet(AbstractDispatcherServletInitializer.java:98)
```

_**TODO:** exercise the service in some meaningful way, e.g. with `curl`._

Congratulations! You have just developed a simple REST service using Spring. This is a basic foundation for building a complete REST API in Spring.


Related Resources
-----------------

There's more to building REST services than is covered here. You may want to continue your exploration of Spring and REST with the following Getting Started guides:

* Handling POST, PUT, and GET requests in REST services
* Creating self-describing APIs with HATEOAS
* Securing a REST service with HTTP Basic
* Securing a REST service with OAuth
* [Consuming REST services](https://github.com/springframework-meta/gs-consuming-rest-core/blob/master/README.md)
* Testing REST services


