package com.example.restservice;

import org.springframework.stereotype.Repository;

@Repository
public class EmployeeManager {
    private static final Employees employees = new Employees();

    //sample employees
    static {
        employees.getEmployeeList().add(new Employee(1, "James", "Smith", "smith_james@gmail.com", "1234 Main Street" ));
        employees.getEmployeeList().add(new Employee(2, "Mary", "Sue", "sue_mary@gmail.com", "1111 14th Grande Street" ));
        employees.getEmployeeList().add(new Employee(3, "John", "Gunther", "gunther_mg@gmail.com", "1456 15th West Street" ));
    }

    //retrieve employees
    public Employees getAllEmployees() {
        return employees;
    }

    //add employees
    public void addEmployee(Employee employee) {
        employees.getEmployeeList().add(employee);
    }
}
