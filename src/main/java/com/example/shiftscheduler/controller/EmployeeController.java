package com.example.shiftscheduler.controller;

import com.example.shiftscheduler.entity.Employee;
import com.example.shiftscheduler.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Display all employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees",
                employeeService.getAllEmployees());

        return "employees/list.html";
    }

    // Display employee creation form.html
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/form.html";
    }

    // Save a new employee
    @PostMapping
    public String addEmployee(
            @ModelAttribute Employee employee) {

        employeeService.addEmployee(employee);
        return "redirect:/employees";
    }

    // Display employee update form.html
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Integer id,
            Model model) {

        model.addAttribute("employee",
                employeeService.getEmployee(id));

        return "employees/form.html";
    }

    // Update an employee
    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Integer id,
            @ModelAttribute Employee employee) {

        employeeService.updateEmployee(
                id,
                employee.getFirstName(),
                employee.getLastName());

        return "redirect:/employees";
    }

    // Delete an employee
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
