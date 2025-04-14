package com.example.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeManager employeeManager;

    @Autowired
    private ObjectMapper objectMapper;

    //test GET request
    @Test
    public void testGetAllEmployees() throws Exception {
        Employees employees = new Employees();
        employees.setEmployeeList(Arrays.asList(
                new Employee(1, "James", "Smith", "smith_james@gmail.com", "Manager"),
                new Employee(2, "Mary", "Sue", "sue_mary@gmail.com", "Admin"),
                new Employee(3, "John", "Gunther", "gunther_mg@gmail.com", "employee")
        ));

        Mockito.when(employeeManager.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeList", hasSize(3)))
                .andExpect(jsonPath("$.employeeList[0].first_name", is("James")));
    }

    //test POST request
    @Test
    public void testAddEmployee() throws Exception {
        Employee newEmployee = new Employee(null, "John", "Doe", "john.doe@example.com", "Developer");

        // Simulate existing list of employees to generate the next ID
        Employees employees = new Employees();
        employees.setEmployeeList(Arrays.asList(
                new Employee(1, "James", "Smith", "smith_james@gmail.com", "Manager")
        ));

        Mockito.when(employeeManager.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/employees/2")));
    }

}
