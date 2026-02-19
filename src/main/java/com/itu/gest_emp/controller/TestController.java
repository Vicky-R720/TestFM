package com.itu.gest_emp.controller;

import servlet.annotations.*;
import servlet.ModelView;
import servlet.JsonResponse;
import com.itu.gest_emp.model.Employee;
import com.itu.gest_emp.model.Department;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    
    // ============== TESTS API JSON ==============
    
    // Test 1 : Retour simple d'un objet
    @Json
    @GetMapping("/api/employee")
    public Employee getEmployee() {
        Employee emp = new Employee();
        emp.setName("Jean");
        return emp;
    }
    
    // Test 2 : Retour d'une liste d'objets
    @Json
    @GetMapping("/api/employees")
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        
        Employee emp1 = new Employee();
        emp1.setName("Jean");
        employees.add(emp1);
        
        Employee emp2 = new Employee();
        emp2.setName("Marie");
        employees.add(emp2);
        
        Employee emp3 = new Employee();
        emp3.setName("Pierre");
        employees.add(emp3);
        
        return employees;
    }
    
    // Test 3 : Retour d'un objet avec département
    @Json
    @GetMapping("/api/employee-detail")
    public Employee getEmployeeWithDepartment() {
        Employee emp = new Employee();
        emp.setName("Jean");
        
        Department dept1 = new Department();
        dept1.setName("IT");
        
        Department dept2 = new Department();
        dept2.setName("HR");
        
        emp.setDepartments(new Department[]{dept1, dept2});
        
        return emp;
    }
    
    // Test 4 : Retour d'une Map
    @Json
    @GetMapping("/api/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployees", 150);
        stats.put("totalDepartments", 12);
        stats.put("averageSalary", 45000.0);
        stats.put("company", "ITU Corp");
        return stats;
    }
    
    // Test 5 : Retour d'un JsonResponse personnalisé avec succès
    @Json
    @GetMapping("/api/custom-success")
    public JsonResponse getCustomSuccess() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Opération réussie");
        data.put("timestamp", System.currentTimeMillis());
        
        return JsonResponse.success(data);
    }
    
    // Test 6 : Retour d'un JsonResponse avec erreur
    @Json
    @GetMapping("/api/custom-error")
    public JsonResponse getCustomError() {
        return JsonResponse.error(404, "Ressource non trouvée");
    }
    
    // Test 7 : Liste vide
    @Json
    @GetMapping("/api/empty-list")
    public List<Employee> getEmptyList() {
        return new ArrayList<>();
    }
}