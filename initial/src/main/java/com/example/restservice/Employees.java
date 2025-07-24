package com.example.restservice;

import java.util.ArrayList;
import java.util.List;

public class Employees {

    private List<Employee> employeeList;

    public List<Employee> getEmployeeList() {
        if (employeeList == null) {
            employeeList = new ArrayList<Employee>();
        }
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
