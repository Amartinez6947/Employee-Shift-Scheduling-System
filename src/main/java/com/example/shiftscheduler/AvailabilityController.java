package com.example.shiftscheduler.controller;

import com.example.shiftscheduler.entity.Availability;
import com.example.shiftscheduler.repository.EmployeeRepository;
import com.example.shiftscheduler.service.AvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;
import java.util.Collections;

@Controller
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;
    private final EmployeeRepository employeeRepository;

    public AvailabilityController(
            AvailabilityService availabilityService,
            EmployeeRepository employeeRepository) {

        this.availabilityService = availabilityService;
        this.employeeRepository = employeeRepository;
    }

    // Display availability records
    @GetMapping
    public String listAvailability(
            @RequestParam(required = false) Integer employeeID,
            Model model) {

        model.addAttribute(
                "employees",
                employeeRepository.findAll());

        model.addAttribute("selectedEmployeeID", employeeID);

        if (employeeID != null) {
            model.addAttribute(
                    "availabilityList",
                    availabilityService.getAvailabilityByEmployee(employeeID));
        } else {
            model.addAttribute(
                    "availabilityList",
                    Collections.emptyList());
        }

        return "availability/list.html";
    }

    // Display availability form.html
    @GetMapping("/new")
    public String showAvailabilityForm(Model model) {

        model.addAttribute(
                "employees",
                employeeRepository.findAll());

        model.addAttribute("availability", new Availability());

        return "availability/form.html";
    }

    // Save availability
    @PostMapping
    public String addAvailability(
            @RequestParam Integer employeeID,
            @RequestParam String dayOfWeek,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime startTime,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime endTime,
            RedirectAttributes redirectAttributes) {

        try {
            availabilityService.addAvailability(
                    employeeID,
                    dayOfWeek,
                    startTime,
                    endTime);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee availability saved successfully.");

            return "redirect:/availability?employeeID=" + employeeID;

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage());

            return "redirect:/availability/new";
        }
    }
}
