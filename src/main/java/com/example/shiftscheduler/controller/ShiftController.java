package com.example.shiftscheduler.controller;

import com.example.shiftscheduler.entity.Shift;
import com.example.shiftscheduler.repository.EmployeeRepository;
import com.example.shiftscheduler.service.ShiftService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/shifts")
public class ShiftController {

    private final ShiftService shiftService;
    private final EmployeeRepository employeeRepository;

    public ShiftController(
            ShiftService shiftService,
            EmployeeRepository employeeRepository) {

        this.shiftService = shiftService;
        this.employeeRepository = employeeRepository;
    }

    // Display all scheduled shifts
    @GetMapping
    public String listShifts(Model model) {

        model.addAttribute(
                "shifts",
                shiftService.getAllShifts());

        return "shifts/list.html";
    }

    // Display the shift creation form
    @GetMapping("/new")
    public String showShiftForm(Model model) {

        model.addAttribute(
                "employees",
                employeeRepository.findAll());

        model.addAttribute("shift", new Shift());

        return "shifts/form.html";
    }

    // Save a new shift
    @PostMapping
    public String addShift(
            @RequestParam Integer employeeID,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime startTime,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime endTime,

            RedirectAttributes redirectAttributes) {

        try {

            shiftService.createShift(
                    employeeID,
                    date,
                    startTime,
                    endTime);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Shift scheduled successfully.");

            return "redirect:/shifts";

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage());

            return "redirect:/shifts/new";
        }
    }

    // Display the shift editing form
    @GetMapping("/edit/{shiftID}")
    public String showEditShiftForm(
            @PathVariable Integer shiftID,
            Model model,
            RedirectAttributes redirectAttributes) {

        Shift shift = shiftService.getAllShifts()
                .stream()
                .filter(s -> s.getShiftID().equals(shiftID))
                .findFirst()
                .orElse(null);

        if (shift == null) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Shift not found.");

            return "redirect:/shifts";
        }

        model.addAttribute("shift", shift);

        model.addAttribute(
                "employees",
                employeeRepository.findAll());

        return "shifts/edit.html";
    }

    // Update an existing shift
    @PostMapping("/update/{shiftID}")
    public String updateShift(
            @PathVariable Integer shiftID,

            @RequestParam Integer employeeID,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime startTime,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime endTime,

            RedirectAttributes redirectAttributes) {

        try {

            shiftService.updateShift(
                    shiftID,
                    employeeID,
                    date,
                    startTime,
                    endTime);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Shift updated successfully.");

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage());
        }

        return "redirect:/shifts";
    }

    // Delete a scheduled shift
    @PostMapping("/delete/{shiftID}")
    public String deleteShift(
            @PathVariable Integer shiftID,
            RedirectAttributes redirectAttributes) {

        try {

            shiftService.deleteShift(shiftID);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Shift deleted successfully.");

        } catch (IllegalArgumentException exception) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    exception.getMessage());
        }

        return "redirect:/shifts";
    }
}
