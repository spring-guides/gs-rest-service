<!-- See expanded [macro:...] values at https://github.com/springframework-meta/springframework.org/tree/master/doc/gs-macros.md -->

# Getting Started: Building a RESTful Web Service

What you'll build
-----------------

This guide will walk you through creating a "hello world" [RESTful web service][u-rest] with Spring. The service will accept HTTP GET requests at:

    http://localhost:8080/greeting

and respond with a [JSON][u-json] representation of a greeting:

    {"id":1,"content":"Hello, World!"}

You'll also be able to customize the greeting by providing an optional `name` parameter in the query string:

    http://localhost:8080/greeting?name=User

this will override the default value of "World" and be reflected in the response:

    {"id":1,"content":"Hello, User!"}


What you'll need
----------------

 - About 15 minutes
 - A favorite text editor or IDE
 - [JDK 6][jdk] or later
 - [Maven 3.0][mvn] or later

[macro:how-to-complete-this-guide]


<a name="scratch"></a>
Set up the project
------------------

[macro:build-system-intro]

### Create the directory structure

In a project directory of your choosing, create the following subdirectory structure (e.g. with `mkdir -p src/main/java/hello` on *nix systems):

    └── src
        └── main
            └── java
                └── hello

### Create a Maven POM

[macro:maven-project-setup-options]

`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-rest-service</artifactId>
    <version>1.0</version>

    <parent>
        <groupId>org.springframework.bootstrap</groupId>
        <artifactId>spring-bootstrap-starters</artifactId>
        <version>0.5.0.BUILD-SNAPSHOT</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.bootstrap</groupId>
            <artifactId>spring-bootstrap-web-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
    </dependencies>

    <!-- TODO: remove once bootstrap goes GA -->
    <repositories>
        <repository>
            <id>spring-snapshots</id>
            <url>http://repo.springsource.org/snapshot</url>
            <snapshots><enabled>true</enabled></snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>spring-snapshots</id>
            <url>http://repo.springsource.org/snapshot</url>
            <snapshots><enabled>true</enabled></snapshots>
        </pluginRepository>
    </pluginRepositories>
</project>
```

[macro:bootstrap-starter-pom-disclaimer]


<a name="initial"></a>
Create a resource representation class
--------------------------------------

With the basics of setting up the project and build system out of the way, it's time to get to the nuts and bolts of creating our service.

It's best to begin this process by thinking about what interacting with the service will look like.

We want to handle `GET` requests for `/greeting`, optionally with a `name` parameter in the query string. In response to such a request, we'd like to send back a `200 OK` response with JSON in the body that represents a greeting. It should look something like this:

    {
        "id": 1,
        "content": "Hello, World!"
    }

The `id` field is a unique identifier for the greeting, and `content` is the textual representation of the greeting.

To model the greeting representation, we'll create a _resource representation class_. There's nothing fancy about this step—we're just creating a plain old java object with fields, constructors and accessors for the `id` and `content` data:

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

As you'll see in a moment, Spring will the use _Jackson_ library to automatically marshal instances of type `Greeting` into JSON.

Now that we've got our representation class, let's create the resource controller that will serve it.


Create a resource controller
------------------------------

In Spring's approach to building RESTful web services, HTTP requests are handled by a _controller_. These components are are easily identified by the [`@Controller`][] annotation, and the `GreetingController` below handles `GET` requests for `/greeting` by returning a new instance of our `Greeting` class:

`src/main/java/hello/GreetingController.java`
```java
package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public @ResponseBody Greeting greeting(
            @RequestParam(value="name", required=false, defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
```

This controller is concise and simple, but there's plenty going on under the hood. Let's break it down step by step.

The `@RequestMapping` annotation ensures that HTTP requests to `/greeting` are mapped to the `greeting()` method.

> **Note:** We have not explicitly specified `GET` vs. `PUT`, `POST`, etc. above, because `@RequestMapping` maps _all_ HTTP operations by default. Use `@RequestMapping(method=GET)` to narrow this down.

`@RequestParam` binds the value of the query string parameter `name` into the `name` parameter of the `greeting()` method. This query string parameter is not `required`, so if absent in the request the `defaultValue` of "World" will be used.

The implementation of the method body is straightforward—create and return a new `Greeting` object with `id` and `content` attributes based on the next value from our `counter` and formatting the given `name` using our greeting `template`.

One key difference between a traditional MVC controller and the RESTful web service controller above is in the way the HTTP response body is created. Rather than relying on a view technology (such as [JSP][u-jsp]) to perform server-side rendering of our greeting data to HTML, this service controller simply populates and returns a `Greeting` object, with the goal that its data is written directly to the HTTP response as JSON.

The [`@ResponseBody`][] annotation helps make this happen. The presence of this annotation on the `greeting()` method tells Spring MVC that it does not need to render the greeting object through a server-side view layer, but that instead that the greeting object returned _is_ the response body, and should be written out directly.

The only step that remains is converting the `Greeting` object to JSON. And fortunately, thanks to Spring's _HTTP message converter_ support, we don't need to bother with doing this conversion by hand. Because [Jackson 2][jackson] is on the classpath, Spring's [`MappingJackson2HttpMessageConverter`][] is automatically chosen to convert the `Greeting` instance to JSON.


Make the application executable
-------------------------------

While it is possible to package this service up as a traditional _web application archive_ or [WAR][u-war] file for deployment to an external application server, we demonstrate below the simpler approach of creating a _standalone application_. You'll package everything up a single, executable JAR file, driven by a good old Java `main()` method. And along the way, we'll use Spring's support for embedding the [Tomcat][u-tomcat] servlet container as the HTTP runtime, instead of deploying to an external instance.

### Create a main class

`src/main/java/hello/Application.java`

```java
package hello;

import org.springframework.bootstrap.SpringApplication;
import org.springframework.bootstrap.context.annotation.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
The `main()` method defers to the [`SpringApplication`][] helper class, providing `Application.class` as an argument to its `run()` method. This tells Spring to read the annotation metadata from `Application` and to manage it as a component in the _[Spring application context][u-application-context]_.

The `@ComponentScan` annotation tells Spring to recursively search through the `hello` package and its children for classes marked directly or indirectly with Spring's [`@Component`][] annotation. This directive ensures that Spring will find and register our `GreetingController`, because it is marked with `@Controller`, which in turn is a kind of `@Component` annotation.

The [`@EnableAutoConfiguration`][] annotation has also been added: it switches on a number of reasonable default behaviors based on the content of your classpath. For example, because the application depends on the embeddable version of Tomcat (tomcat-embed-core.jar), a Tomcat server is set up and configured with reasonable defaults on your behalf. And because the application also depends on Spring MVC (spring-webmvc.jar), a Spring MVC [`DispatcherServlet`][] is configured and registered for you—no `web.xml` necessary! Auto-configuration is a powerful, flexible mechanism—See the [API documentation][`@EnableAutoConfiguration`] for further details.

### Build an executable JAR

Now that we have our `Application` class ready to go, we simply need to instruct the build system to create a single, executable jar containing everything. This will make it dead simple to ship and version and deploy the service as an application throughout the development lifecycle, across different environments, etc.

Add the following configuration to your existing Maven POM

`pom.xml`
```xml
    <properties>
        <start-class>hello.Application</start-class>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

The `start-class` property tells Maven to create a `META-INF/MANIFEST.MF` file with a `Main-Class: hello.Application` entry. This is the key to being able to run the jar with `java -jar`.

The [Maven Shade plugin][maven-shade-plugin] extracts classes from all the jars on the classpath and builds a single "über-jar". This makes it much more convenient to execute and transport your service.

Now run the following to produce a single executable JAR file containing all necessary dependency classes and resources:

    mvn package


Run the service
---------------

That's it! You're ready to run your service with `java -jar` at the command line:

    java -jar target/gs-rest-service-1.0.jar

You'll see a bit of logging output, and the service should be up and running within a second or two.


Test the service
----------------

Now that the service is up, visit <http://localhost:8080/greeting>, where you'll see

    {"id":1,"content":"Hello, World!"}

Try providing a `name` query string parameter with <http://localhost:8080/greeting?name=User>. Notice how the value of the `content` attribute changes from "Hello, World!" to "Hello User!":

    {"id":2,"content":"Hello, User!"}

This demonstrates that the `@RequestParam` arrangement in `GreetingController` is working as expected. The `name` parameter has been given a default value of "World", but can always be explicitly overridden through the query string.

Notice also how the `id` attribute has changed from `1` to `2`. This proves that we're working against the same `GreetingController` instance across multiple requests, and that its `counter` field is being incremented on each call as expected.


Summary
-------

Congrats! You've just developed your first RESTful web service using Spring. This of course is just the beginning, and there are many more features to explore and take advantage of. Be sure to check out Spring's support for [securing](TODO), [testing](TODO), [describing](TODO) and [managing](TODO) RESTful web services.


[mvn]: http://maven.apache.org/download.cgi
[zip]: https://github.com/springframework-meta/gs-rest-service/archive/master.zip
[jdk]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[u-rest]: /understanding/rest
[u-json]: /understanding/json
[u-jsp]: /understanding/jsp
[jackson]: http://wiki.fasterxml.com/JacksonHome
[u-war]: /understanding/war
[u-tomcat]: /understanding/tomcat
[u-application-context]: /understanding/application-context
[maven-shade-plugin]: https://maven.apache.org/plugins/maven-shade-plugin
[`@Controller`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/stereotype/Controller.html
[`SpringApplication`]: http://static.springsource.org/spring-bootstrap/docs/0.5.0.BUILD-SNAPSHOT/javadoc-api/org/springframework/bootstrap/SpringApplication.html
[`@EnableAutoConfiguration`]: http://static.springsource.org/spring-bootstrap/docs/0.5.0.BUILD-SNAPSHOT/javadoc-api/org/springframework/bootstrap/context/annotation/SpringApplication.html
[`@Component`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/stereotype/Component.html
[`@ResponseBody`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/ResponseBody.html
[`MappingJackson2HttpMessageConverter`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.html
[`DispatcherServlet`]: http://static.springsource.org/spring/docs/current/javadoc-api/org/springframework/web/servlet/DispatcherServlet.html
