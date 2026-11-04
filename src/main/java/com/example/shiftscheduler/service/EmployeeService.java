package com.example.shiftscheduler.service;

import com.example.shiftscheduler.entity.Employee;
import com.example.shiftscheduler.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Add a new employee
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Update an existing employee
    public Employee updateEmployee(
            Integer employeeID,
            String firstName,
            String lastName) {

        Employee employee = employeeRepository.findById(employeeID)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found: " + employeeID));

        employee.setFirstName(firstName);
        employee.setLastName(lastName);

        return employeeRepository.save(employee);
    }

    // Retrieve one employee
    public Employee getEmployee(Integer employeeID) {
        return employeeRepository.findById(employeeID)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found: " + employeeID));
    }

    // Retrieve all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Delete an employee
    public void deleteEmployee(Integer employeeID) {
        Employee employee = getEmployee(employeeID);
        employeeRepository.delete(employee);
    }
}

