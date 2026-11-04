package com.example.shiftscheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shiftID;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Default constructor required by JPA
    public Shift() {
    }

    // Constructor
    public Shift(Employee employee, LocalDate date,
                 LocalTime startTime, LocalTime endTime) {

        this.employee = employee;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public Integer getShiftID() {
        return shiftID;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    // Setters
    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
