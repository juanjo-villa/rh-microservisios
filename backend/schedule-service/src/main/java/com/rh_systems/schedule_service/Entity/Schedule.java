package com.rh_systems.schedule_service.Entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;

/**
 * Entity representing a work schedule.
 */
@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "exit_time", nullable = false)
    private LocalTime exitTime;

    @Column(name = "total_hours", nullable = false)
    private Float totalHours;

    @Column(name = "deducted_hours", nullable = false)
    private Float deductedHours;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeSchedule> employeeSchedules;

    /**
     * Gets the ID.
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID.
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the date.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the start time.
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     * @param startTime the start time to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the exit time.
     * @return the exit time
     */
    public LocalTime getExitTime() {
        return exitTime;
    }

    /**
     * Sets the exit time.
     * @param exitTime the exit time to set
     */
    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Gets the total hours.
     * @return the total hours
     */
    public Float getTotalHours() {
        return totalHours;
    }

    /**
     * Sets the total hours.
     * @param totalHours the total hours to set
     */
    public void setTotalHours(Float totalHours) {
        this.totalHours = totalHours;
    }

    /**
     * Gets the deducted hours.
     * @return the deducted hours
     */
    public Float getDeductedHours() {
        return deductedHours;
    }

    /**
     * Sets the deducted hours.
     * @param deductedHours the deducted hours to set
     */
    public void setDeductedHours(Float deductedHours) {
        this.deductedHours = deductedHours;
    }

    /**
     * Gets the list of employee schedules.
     * @return the list of employee schedules
     */
    public List<EmployeeSchedule> getEmployeeSchedules() {
        return employeeSchedules;
    }

    /**
     * Sets the list of employee schedules.
     * @param employeeSchedules the list of employee schedules to set
     */
    public void setEmployeeSchedules(List<EmployeeSchedule> employeeSchedules) {
        this.employeeSchedules = employeeSchedules;
    }
}
