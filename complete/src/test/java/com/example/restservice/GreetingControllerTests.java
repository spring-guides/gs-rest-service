package com.example.restservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest
@AutoConfigureRestTestClient
@DisplayName("Greeting Controller Tests")
public class GreetingControllerTests {

  @Autowired
  private RestTestClient restTestClient;

  @Test
  @DisplayName("No parameter should return default 'World' message")
  public void noParamGreetingShouldReturnDefaultMessage() throws Exception {

    this.restTestClient.get().uri("/greeting")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.content").isEqualTo("Hello, World!");
  }

  @Test
  @DisplayName("With name parameter should return tailored message")
  public void paramGreetingShouldReturnTailoredMessage() throws Exception {

    this.restTestClient.get()
        .uri(uri -> uri.path("/greeting").queryParam("name", "Spring Community").build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.content").isEqualTo("Hello, Spring Community!");
  }

  @Test
  @DisplayName("Response should contain numeric ID")
  public void greetingShouldReturnNumericId() throws Exception {

    this.restTestClient.get().uri("/greeting")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").exists()
        .jsonPath("$.id").isNumber();
  }

  @Test
  @DisplayName("Counter should increment on multiple calls")
  public void counterShouldIncrementOnMultipleCalls() throws Exception {

    // First call
    this.restTestClient.get().uri("/greeting")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").exists();

    // Second call - counter should increment
    this.restTestClient.get().uri("/greeting")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").exists();
  }

  @Test
  @DisplayName("Empty name parameter should use empty string")
  public void emptyNameParameterShouldReturnEmptyString() throws Exception {

    this.restTestClient.get()
        .uri(uri -> uri.path("/greeting").queryParam("name", "").build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.content").isEqualTo("Hello, !");
  }

  @Test
  @DisplayName("Special characters in name should be handled")
  public void specialCharactersInNameShouldBeHandled() throws Exception {

    this.restTestClient.get()
        .uri(uri -> uri.path("/greeting").queryParam("name", "John@123!").build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.content").isEqualTo("Hello, John@123!");
  }

  @Test
  @DisplayName("Long name parameter should be handled")
  public void longNameParameterShouldBeHandled() throws Exception {

    String longName = "A".repeat(100);
    this.restTestClient.get()
        .uri(uri -> uri.path("/greeting").queryParam("name", longName).build())
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.content").isEqualTo("Hello, " + longName + "!");
  }

  @Test
  @DisplayName("Response should have valid JSON structure")
  public void responseShouldHaveValidJsonStructure() throws Exception {

    this.restTestClient.get().uri("/greeting")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").exists()
        .jsonPath("$.content").exists();
  }

  @Test
  @DisplayName("Content type should be JSON")
  public void responseContentTypeShouldBeJson() throws Exception {

    this.restTestClient.get().uri("/greeting")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentTypeCompatibleWith("application/json");
  }

}
