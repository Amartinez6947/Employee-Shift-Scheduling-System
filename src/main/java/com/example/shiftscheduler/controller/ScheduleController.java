package com.example.shiftscheduler.controller;

import com.example.shiftscheduler.entity.Shift;
import com.example.shiftscheduler.repository.ShiftRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ShiftRepository shiftRepository;

    public ScheduleController(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    // Display the weekly employee schedule
    @GetMapping
    public String showWeeklySchedule(
            @RequestParam(required = false) LocalDate weekStart,
            Model model) {

        // Determine the Monday of the selected week
        LocalDate selectedWeekStart;

        if (weekStart == null) {
            selectedWeekStart = LocalDate.now()
                    .with(TemporalAdjusters.previousOrSame(
                            DayOfWeek.MONDAY));
        } else {
            selectedWeekStart = weekStart
                    .with(TemporalAdjusters.previousOrSame(
                            DayOfWeek.MONDAY));
        }

        // Determine the Sunday of the selected week
        LocalDate weekEnd = selectedWeekStart.plusDays(6);

        // Retrieve all shifts scheduled during this week
        List<Shift> shifts = shiftRepository.findByDateBetween(
                selectedWeekStart,
                weekEnd
        );

        // Diagnostic messages to verify database results
        System.out.println("-----------------------------------");
        System.out.println("WEEKLY SCHEDULE DATABASE CHECK");
        System.out.println("WEEK START: " + selectedWeekStart);
        System.out.println("WEEK END: " + weekEnd);
        System.out.println("SHIFTS FOUND: " + shifts.size());

        shifts.forEach(shift -> {
            System.out.println(
                    "SHIFT ID: " + shift.getShiftID()
                            + " | DATE: " + shift.getDate()
                            + " | EMPLOYEE: "
                            + shift.getEmployee().getFirstName()
                            + " "
                            + shift.getEmployee().getLastName()
                            + " | TIME: "
                            + shift.getStartTime()
                            + " - "
                            + shift.getEndTime()
            );
        });

        System.out.println("-----------------------------------");

        // Group shifts by their scheduled date
        Map<LocalDate, List<Shift>> shiftsByDate =
                shifts.stream()
                        .collect(Collectors.groupingBy(
                                Shift::getDate
                        ));

        // Generate all seven dates for the calendar
        List<LocalDate> weekDates =
                IntStream.range(0, 7)
                        .mapToObj(selectedWeekStart::plusDays)
                        .toList();

        // Send schedule information to the Thymeleaf template
        model.addAttribute("weekStart", selectedWeekStart);
        model.addAttribute("weekEnd", weekEnd);

        model.addAttribute(
                "previousWeek",
                selectedWeekStart.minusWeeks(1)
        );

        model.addAttribute(
                "nextWeek",
                selectedWeekStart.plusWeeks(1)
        );

        model.addAttribute("weekDates", weekDates);
        model.addAttribute("shiftsByDate", shiftsByDate);

        return "schedule/weekly";
    }
}