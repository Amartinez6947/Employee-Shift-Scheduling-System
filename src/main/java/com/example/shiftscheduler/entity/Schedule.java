package com.example.shiftscheduler.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleID;

    private LocalDate weekStartDate;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Shift> shifts = new ArrayList<>();

    // Default constructor required by JPA
    public Schedule() {
    }

    // Constructor
    public Schedule(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    // Add a shift to the schedule
    public void addShift(Shift shift) {
        if (shift != null && !shifts.contains(shift)) {
            shifts.add(shift);
        }
    }

    // Remove a shift from the schedule
    public void removeShift(Shift shift) {
        shifts.remove(shift);
    }

    // Getters
    public Integer getScheduleID() {
        return scheduleID;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    // Setters
    public void setScheduleID(Integer scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setWeekStartDate(LocalDate weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

    // Display weekly schedule
    public List<Shift> displayWeeklySchedule() {
        return shifts;
    }
}
