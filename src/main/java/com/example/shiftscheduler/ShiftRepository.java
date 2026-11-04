package com.example.shiftscheduler.repository;

import com.example.shiftscheduler.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {

    // Find all shifts assigned to a specific employee
    List<Shift> findByEmployee_EmployeeID(Integer employeeID);

    // Find all shifts scheduled on a specific date
    List<Shift> findByDate(LocalDate date);

    // Find shifts within a date range
    List<Shift> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
