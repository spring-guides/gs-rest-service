package com.example.restservice

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest
@AutoConfigureRestTestClient
class GreetingControllerTests {

	@Test
	fun noParamGreetingShouldReturnDefaultMessage(@Autowired restTestClient: RestTestClient) {
		restTestClient.get().uri("/greeting")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.content").isEqualTo("Hello, World!")
	}

	@Test
	fun paramGreetingShouldReturnTailoredMessage(@Autowired restTestClient: RestTestClient) {
		restTestClient.get()
			.uri { it.path("/greeting").queryParam("name", "Spring Community").build() }
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.content").isEqualTo("Hello, Spring Community!")
	}

}
