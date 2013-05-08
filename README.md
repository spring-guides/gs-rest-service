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

### Maven

Create a `pom.xml` file with the following contents:

`pom.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-rest-service-complete</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.bootstrap</groupId>
        <artifactId>spring-bootstrap-starters</artifactId>
        <version>0.0.1-SNAPSHOT</version>
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

</project>
```

Experienced Maven users who feel nervous about using an external parent project: don't panic, you can take it out later, it's just there to reduce the amount of code you have to write to get started.

### Gradle \[[copy complete `build.gradle` to clipboard](start/build.gradle)\]

Add the following within the `dependencies { }` section of your build.gradle file:

`build.gradle`
```groovy
compile "org.springframework.bootstrap:spring-bootstrap-web-starter:0.0.1-SNAPSHOT"
compile "com.fasterxml.jackson.core:jackson-databind:2.2.0-"
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

We can launch the application from a custom main class, or we can do that directly from one of the configuration classes.  The easiest way is to use the `SpringApplication` helper class:

`src/main/java/hello/HelloWorldConfiguration.java`

```java
package hello;

import org.springframework.bootstrap.SpringApplication;
import org.springframework.bootstrap.context.annotation.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan
public class HelloWorldConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldConfiguration.class, args);
    }
}
```

The `@EnableAutoConfiguration` annotation has also been added: it provides a load of defaults (like the embedded servlet container) depending on the contents of your classpath, and other things.

Running the Service
-------------------------------------

Add the following to your `pom.xml`: 

`pom.xml`
```xml
<properties>
    <start-class>hello.HelloWorldConfiguration</start-class>
</properties>
```

You can now run the application with the Maven exec plugin:

```
$ mvn exec:java

... service comes up ...
```

so in another terminal you can do this

```
$ curl localhost:8080/hello-world
{"id":1,"content":"Hello, Stranger!"}
```

Building an executable JAR
--------------------------

Add the following to your `pom.xml` file (keeping any existing properties or plugins intact):

`pom.xml`
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

The following will produce a single executable JAR file containing all necessary dependency classes:

```
$ mvn package
```

Now you can run it from the jar as well, and distribute that as an executable artifact:

```
$ java -jar target/gs-rest-service-0.0.1-SNAPSHOT.jar

... service comes up ...
```

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


