package com.example.restservice;

public class Employee {
    //Variables for employee
    Integer employee_Id;
    String first_name;
    String last_name;
    String email;
    String address;

    //Default Constructor for Employee
    public Employee(){
    }

    //Parameterized Constructor
    public Employee(Integer employee_Id, String first_name, String last_name, String email, String address) {
        this.employee_Id = employee_Id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
    }

    //Employee getters and setters
    public Integer getEmployee_Id() {
        return employee_Id;
    }

    public void setEmployee_Id(Integer employee_Id) {
        this.employee_Id = employee_Id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Employee [employee_Id=" + employee_Id + ", first_name=" + first_name + ", last_name=" + last_name + ", email=" + email + ", address=" + address + "]";
    }
}
