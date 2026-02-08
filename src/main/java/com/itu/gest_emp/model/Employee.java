package com.itu.gest_emp.model;

public class Employee {
    private String name;
    private Department[] departments;
    
    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Department[] getDepartments() { return departments; }
    public void setDepartments(Department[] departments) { this.departments = departments; }
}