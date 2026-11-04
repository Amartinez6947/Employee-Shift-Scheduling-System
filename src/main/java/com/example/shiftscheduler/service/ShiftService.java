package com.example.shiftscheduler.service;

import com.example.shiftscheduler.entity.Employee;
import com.example.shiftscheduler.entity.Shift;
import com.example.shiftscheduler.repository.EmployeeRepository;
import com.example.shiftscheduler.repository.ShiftRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ConflictDetector conflictDetector;

    public ShiftService(
            ShiftRepository shiftRepository,
            EmployeeRepository employeeRepository,
            ConflictDetector conflictDetector) {

        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
        this.conflictDetector = conflictDetector;
    }

    // Create a new shift
    public Shift createShift(
            Integer employeeID,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        if (date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Date and shift times are required.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be later than start time.");
        }

        Employee employee = employeeRepository.findById(employeeID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Employee not found."));

        Shift shift = new Shift(
                employee, date, startTime, endTime);

        if (conflictDetector.checkAvailabilityConflict(shift)) {
            throw new IllegalArgumentException(
                    "Shift conflicts with employee availability.");
        }

        if (conflictDetector.checkShiftOverlap(shift)) {
            throw new IllegalArgumentException(
                    "Shift overlaps with another scheduled shift.");
        }

        return shiftRepository.save(shift);
    }

    // Retrieve all shifts
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    // Retrieve shifts for a specific date
    public List<Shift> getShiftsByDate(LocalDate date) {
        return shiftRepository.findByDate(date);
    }

    // Retrieve shifts for a specific employee
    public List<Shift> getShiftsByEmployee(Integer employeeID) {
        return shiftRepository.findByEmployee_EmployeeID(employeeID);
    }

    // Update an existing shift
    public Shift updateShift(
            Integer shiftID,
            Integer employeeID,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        Shift shift = shiftRepository.findById(shiftID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Shift not found."));

        if (date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Date and shift times are required.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be later than start time.");
        }

        Employee employee = employeeRepository.findById(employeeID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Employee not found."));

        shift.setEmployee(employee);
        shift.setDate(date);
        shift.setStartTime(startTime);
        shift.setEndTime(endTime);

        if (conflictDetector.checkAvailabilityConflict(shift)) {
            throw new IllegalArgumentException(
                    "Updated shift conflicts with employee availability.");
        }

        if (conflictDetector.checkShiftOverlap(shift)) {
            throw new IllegalArgumentException(
                    "Updated shift overlaps with another scheduled shift.");
        }

        return shiftRepository.save(shift);
    }

    // Delete a shift
    public void deleteShift(Integer shiftID) {

        if (!shiftRepository.existsById(shiftID)) {
            throw new IllegalArgumentException("Shift not found.");
        }

        shiftRepository.deleteById(shiftID);
    }
}
