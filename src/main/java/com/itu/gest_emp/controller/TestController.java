package com.itu.gest_emp.controller;

import servlet.annotations.*;
import servlet.ModelView;
import com.itu.gest_emp.model.Employee;
import com.itu.gest_emp.model.Department;

@Controller
public class TestController {

    @PostMapping("/save-employee")
    public String saveEmployee(Employee e) {
        // Le binding automatique va remplir :
        // - e.getName() avec la valeur de "e.name"
        // - e.getDepartments()[0].getName() avec "e.department[0].name"
        // - e.getDepartments()[1].getName() avec "e.department[1].name"
        
        StringBuilder result = new StringBuilder();
        result.append("Employee: ").append(e.getName()).append("\n");
        
        if (e.getDepartments() != null) {
            for (int i = 0; i < e.getDepartments().length; i++) {
                Department dept = e.getDepartments()[i];
                if (dept != null) {
                    result.append("Department[").append(i).append("]: ")
                          .append(dept.getName()).append("\n");
                }
            }
        }
        
        return result.toString();
    }

    // Test avec tableau en argument
    @PostMapping("/save-multiple")
    public String saveMultiple(Employee[] employees, Department d, @RequestParam("deptId") int deptId) {
        // employees[0].name, employees[1].name, etc.
        // d.name, d.id
        // deptId
        
        return String.format("Received %d employees, department: %s, deptId: %d", 
                            employees.length, d.getName(), deptId);
    }
}