package com.example.shiftscheduler.repository;

import com.example.shiftscheduler.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository
        extends JpaRepository<Availability, Integer> {

    // Find all availability records for a specific employee
    List<Availability> findByEmployee_EmployeeID(Integer employeeID);

    // Find availability records for a specific day
    List<Availability> findByDayOfWeek(String dayOfWeek);
}
