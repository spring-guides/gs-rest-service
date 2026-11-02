package com.example.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.restservice.controller.GreetingController;
import com.example.restservice.service.GreetingService;

class GreetingControllerTest {

  private final MockMvc mockMvc = MockMvcBuilders
      .standaloneSetup(new GreetingController(new GreetingService()))
      .build();

  @Test
  void returnsDefaultGreetingWhenNameIsNotProvided() throws Exception {
    mockMvc.perform(get("/greeting"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello, World!"))
        .andExpect(jsonPath("$.id").doesNotExist());
  }

  @Test
  void returnsPersonalizedGreetingWhenNameIsProvided() throws Exception {
    mockMvc.perform(get("/greeting").param("name", "Alice"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello, Alice!"))
        .andExpect(jsonPath("$.id").doesNotExist());
  }
}
