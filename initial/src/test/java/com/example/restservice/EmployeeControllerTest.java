package com.example.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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

    //test GET request delete or comment the other test for these
    // commented out test to work
//    @Test
//    public void testGetAllEmployees() throws Exception {
//        Employees employees = new Employees();
//        employees.setEmployeeList(Arrays.asList(
//                new Employee(1, "James", "Smith", "smith_james@gmail.com", "Manager"),
//                new Employee(2, "Mary", "Sue", "sue_mary@gmail.com", "Admin"),
//                new Employee(3, "John", "Gunther", "gunther_mg@gmail.com", "employee")
//        ));
//
//        Mockito.when(employeeManager.getAllEmployees()).thenReturn(employees);
//
//        mockMvc.perform(get("/employees")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.employeeList", hasSize(3)))
//                .andExpect(jsonPath("$.employeeList[0].first_name", is("James")));
//    }
//
//    //test POST request
//    @Test
//    public void testAddEmployee() throws Exception {
//        Employee newEmployee = new Employee(null, "John", "Doe", "john.doe@example.com", "Developer");
//
//        // Simulate existing list of employees to generate the next ID
//        Employees employees = new Employees();
//        employees.setEmployeeList(Arrays.asList(
//                new Employee(1, "James", "Smith", "smith_james@gmail.com", "Manager")
//        ));
//
//        Mockito.when(employeeManager.getAllEmployees()).thenReturn(employees);
//
//        mockMvc.perform(post("/employees")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newEmployee)))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("Location", containsString("/employees/2")));
//    }


    // Helper function to get the number of current employees
    int getEmployeeCount(EmployeeManager manager)
    {
        return manager.getAllEmployees().getEmployeeList().size();
    }

    @Test
        // Ensure that employee list is populated on initialization
    void createEmployeeManager() {
        EmployeeManager newEmployeeManager = new EmployeeManager();
        assert(getEmployeeCount(newEmployeeManager) != 0);
    }

    @Test
        // Ensure that adding an employee increases the employee count by 1
    void addEmployee() {
        EmployeeManager employeeManager = new EmployeeManager();
        int employeeCount = getEmployeeCount(employeeManager);
        Employee employee = new Employee(1, "Daria", "Jones", "dariajones@gmail.com", "Software developer");
        employeeManager.addEmployee(employee);
        assert(employeeCount + 1 == getEmployeeCount(employeeManager));
    }

    @ExtendWith(MockitoExtension.class)
    @BeforeEach void setUp()
    {
        this.employeeManager = new EmployeeManager();
        Employee newEmployee = new Employee(1, "Daria", "Jones", "dariajones@gmail.com", "Software developer");
        this.employeeManager.addEmployee(newEmployee);
    }

    @Test
        // Check whether added employee ID is found in ID field
    void employeeIdInList() {
        List<Employee> employees = this.employeeManager.getAllEmployees().getEmployeeList();
        for (int i=0; i<employees.size(); i++)
        {
            Employee employee = employees.get(i);
            if (employee.getEmployee_Id() == 1)
            {
                return;
            }
        }
        assert(false);
    }

    @Test
        // Check whether added employee first name is found in first name field
    void employeeFirstNameInList() {
        List<Employee> employees = this.employeeManager.getAllEmployees().getEmployeeList();
        for (int i=0; i<employees.size(); i++)
        {
            Employee employee = employees.get(i);
            if (employee.getFirst_name() == "Daria")
            {
                return;
            }
        }
        assert(false);
    }

    @Test
        // Check whether added employee last name is found in last name field
    void employeeLastNameInList() {
        List<Employee> employees = this.employeeManager.getAllEmployees().getEmployeeList();
        for (int i=0; i<employees.size(); i++)
        {
            Employee employee = employees.get(i);
            if (employee.getLast_name() == "Jones")
            {
                return;
            }
        }
        assert(false);
    }

    @Test
        // Check whether added employee email is found in email field
    void employeeEmailInList() {
        List<Employee> employees = this.employeeManager.getAllEmployees().getEmployeeList();
        for (int i=0; i<employees.size(); i++)
        {
            Employee employee = employees.get(i);
            if (employee.getEmail() == "dariajones@gmail.com")
            {
                return;
            }
        }
        assert(false);
    }

    @Test
        // Check whether added employee title is found in title field
    void employeeTitleInList() {
        List<Employee> employees = this.employeeManager.getAllEmployees().getEmployeeList();
        for (int i=0; i<employees.size(); i++)
        {
            Employee employee = employees.get(i);
            if (employee.getTitle() == "Software developer")
            {
                return;
            }
        }
        assert(false);
    }
}
