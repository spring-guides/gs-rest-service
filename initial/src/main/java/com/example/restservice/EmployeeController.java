package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class EmployeeController {

    //inject the EmployeeManager
    @Autowired
    EmployeeManager employeeManager;

    //Get point to retrieve the employees from employeeManager
    @GetMapping("/employees")
    public Employees getAllEmployees() {
        return employeeManager.getAllEmployees();
    }

    // POST endpoint to add a new employee
    @PostMapping("/employees")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee employee) {

        // Generate ID for the new employee
        Integer id = employeeManager.getAllEmployees().getEmployeeList().size() + 1;
        employee.setEmployee_Id(id);

        // Add employee to the list
        employeeManager.addEmployee(employee);

        // Build location URI for the new employee
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getEmployee_Id())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
