package com.example.shiftscheduler.service;

import com.example.shiftscheduler.entity.Availability;
import com.example.shiftscheduler.entity.Shift;
import com.example.shiftscheduler.repository.AvailabilityRepository;
import com.example.shiftscheduler.repository.ShiftRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ConflictDetector {

    private final AvailabilityRepository availabilityRepository;
    private final ShiftRepository shiftRepository;

    public ConflictDetector(
            AvailabilityRepository availabilityRepository,
            ShiftRepository shiftRepository) {

        this.availabilityRepository = availabilityRepository;
        this.shiftRepository = shiftRepository;
    }

    // Check whether a shift falls outside employee availability
    public boolean checkAvailabilityConflict(Shift shift) {

        if (shift == null
                || shift.getEmployee() == null
                || shift.getDate() == null
                || shift.getStartTime() == null
                || shift.getEndTime() == null
                || !shift.getEndTime().isAfter(shift.getStartTime())) {

            return true;
        }

        Integer employeeID = shift.getEmployee().getEmployeeID();

        String dayOfWeek = shift.getDate()
                .getDayOfWeek()
                .name();

        List<Availability> availabilities =
                availabilityRepository.findAll();

        boolean hasMatchingAvailability = availabilities.stream()
                .filter(a -> a.getEmployee() != null)
                .filter(a -> Objects.equals(
                        a.getEmployee().getEmployeeID(), employeeID))
                .filter(a -> a.getDayOfWeek() != null
                        && a.getDayOfWeek().equalsIgnoreCase(dayOfWeek))
                .anyMatch(a ->
                        !shift.getStartTime().isBefore(a.getStartTime())
                                && !shift.getEndTime().isAfter(a.getEndTime())
                );

        return !hasMatchingAvailability;
    }

    // Check whether the employee already has an overlapping shift
    public boolean checkShiftOverlap(Shift shift) {

        if (shift == null
                || shift.getEmployee() == null
                || shift.getDate() == null
                || shift.getStartTime() == null
                || shift.getEndTime() == null) {

            return true;
        }

        Integer employeeID = shift.getEmployee().getEmployeeID();

        List<Shift> existingShifts =
                shiftRepository.findByDate(shift.getDate());

        return existingShifts.stream()
                .filter(existing -> existing.getEmployee() != null)
                .filter(existing -> Objects.equals(
                        existing.getEmployee().getEmployeeID(), employeeID))
                // Ignore the same shift when editing it
                .filter(existing -> shift.getShiftID() == null
                        || !Objects.equals(
                        existing.getShiftID(), shift.getShiftID()))
                // Check whether the time ranges overlap
                .anyMatch(existing ->
                        shift.getStartTime().isBefore(existing.getEndTime())
                                && shift.getEndTime().isAfter(existing.getStartTime())
                );
    }
}

