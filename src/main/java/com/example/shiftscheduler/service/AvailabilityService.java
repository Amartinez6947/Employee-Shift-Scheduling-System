package com.example.shiftscheduler.service;

import com.example.shiftscheduler.entity.Availability;
import com.example.shiftscheduler.entity.Employee;
import com.example.shiftscheduler.repository.AvailabilityRepository;
import com.example.shiftscheduler.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;

    public AvailabilityService(
            AvailabilityRepository availabilityRepository,
            EmployeeRepository employeeRepository) {

        this.availabilityRepository = availabilityRepository;
        this.employeeRepository = employeeRepository;
    }

    // Add employee availability
    public Availability addAvailability(
            Integer employeeID,
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {

        validateAvailability(dayOfWeek, startTime, endTime);

        Employee employee = employeeRepository.findById(employeeID)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Employee not found: " + employeeID));

        Availability availability = new Availability(
                employee,
                dayOfWeek.trim().toUpperCase(Locale.ROOT),
                startTime,
                endTime
        );

        return availabilityRepository.save(availability);
    }

    // Retrieve availability for one employee
    @Transactional(readOnly = true)
    public List<Availability> getAvailabilityByEmployee(
            Integer employeeID) {

        if (!employeeRepository.existsById(employeeID)) {
            throw new IllegalArgumentException(
                    "Employee not found: " + employeeID);
        }

        return availabilityRepository
                .findByEmployee_EmployeeID(employeeID);
    }

    // Retrieve availability for a specific day
    @Transactional(readOnly = true)
    public List<Availability> getAvailabilityByDay(
            String dayOfWeek) {

        validateDay(dayOfWeek);

        return availabilityRepository.findByDayOfWeek(
                dayOfWeek.trim().toUpperCase(Locale.ROOT));
    }

    // Validate availability information
    private void validateAvailability(
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime) {

        validateDay(dayOfWeek);

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Start time and end time are required.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "End time must be later than start time.");
        }
    }

    // Validate day of week
    private void validateDay(String dayOfWeek) {

        if (dayOfWeek == null || dayOfWeek.isBlank()) {
            throw new IllegalArgumentException(
                    "Day of week is required.");
        }

        try {
            DayOfWeek.valueOf(
                    dayOfWeek.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid day of week: " + dayOfWeek);
        }
    }
}

