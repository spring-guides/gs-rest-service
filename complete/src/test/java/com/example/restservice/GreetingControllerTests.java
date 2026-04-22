package com.example.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GreetingControllerTests {

    @Test
    public void contextLoads() {
        // Test 1: Application context loads successfully
        assertThat(true).isTrue();
    }

    @Test
    public void greetingControllerExists() {
        // Test 2: GreetingController can be instantiated
        GreetingController controller = new GreetingController();
        assertThat(controller).isNotNull();
    }

    @Test
    public void greetingHasCorrectContent() {
        // Test 3: Greeting content contains Hello
        GreetingController controller = new GreetingController();
        Greeting greeting = controller.greeting("World");
        assertThat(greeting.content()).contains("Hello");
    }

    @Test
    public void greetingWithCustomName() {
        // Test 4: Custom name appears in greeting
        GreetingController controller = new GreetingController();
        Greeting greeting = controller.greeting("Alice");
        assertThat(greeting.content()).contains("Alice");
    }

    @Test
    public void greetingHasId() {
        // Test 5: Greeting has a valid id
        GreetingController controller = new GreetingController();
        Greeting greeting = controller.greeting("World");
        assertThat(greeting.id()).isPositive();
    }
}