package com.example.shiftscheduler.controller;

import com.example.shiftscheduler.repository.EmployeeRepository;
import com.example.shiftscheduler.repository.AvailabilityRepository;
import com.example.shiftscheduler.repository.ShiftRepository;

import com.example.shiftscheduler.entity.Shift;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class DashboardController {

    private final EmployeeRepository employeeRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ShiftRepository shiftRepository;

    public DashboardController(
            EmployeeRepository employeeRepository,
            AvailabilityRepository availabilityRepository,
            ShiftRepository shiftRepository) {

        this.employeeRepository = employeeRepository;
        this.availabilityRepository = availabilityRepository;
        this.shiftRepository = shiftRepository;
    }

    @GetMapping("/")
    public String showDashboard(Model model) {

        // Retrieve employees, availability, and shifts
        var employees = employeeRepository.findAll();
        var availabilities = availabilityRepository.findAll();

        // Determine the current week's Monday through Sunday
        LocalDate weekStart = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate weekEnd = weekStart.plusDays(6);

        List<Shift> shifts = shiftRepository.findByDateBetween(
                weekStart,
                weekEnd
        );

        // Organize shifts by date for the weekly calendar
        Map<LocalDate, List<Shift>> shiftsByDate =
                shifts.stream().collect(
                        Collectors.groupingBy(Shift::getDate)
                );

        List<LocalDate> weekDates = IntStream.range(0, 7)
                .mapToObj(weekStart::plusDays)
                .toList();

        // Add dashboard information
        model.addAttribute("employees", employees);
        model.addAttribute("availabilities", availabilities);
        model.addAttribute("shifts", shifts);
        model.addAttribute("shiftsByDate", shiftsByDate);
        model.addAttribute("weekDates", weekDates);

        model.addAttribute("weekStart", weekStart);
        model.addAttribute("weekEnd", weekEnd);

        model.addAttribute("employeeCount", employees.size());
        model.addAttribute("shiftCount", shifts.size());
        model.addAttribute("availabilityCount", availabilities.size());

        return "dashboard";
    }
}
