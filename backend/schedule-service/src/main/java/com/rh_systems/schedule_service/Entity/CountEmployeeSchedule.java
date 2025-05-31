package com.rh_systems.schedule_service.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;

/**
 * Entity representing the count of employee schedules.
 */
@Entity
@Table(name = "count_employee_schedule")
public class CountEmployeeSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_employee_schedule", referencedColumnName = "id", nullable = false)
    private EmployeeSchedule employeeSchedule;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "work_hours", nullable = false)
    private Float workHours;

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
     * Gets the employee schedule.
     * @return the employee schedule
     */
    public EmployeeSchedule getEmployeeSchedule() {
        return employeeSchedule;
    }

    /**
     * Sets the employee schedule.
     * @param employeeSchedule the employee schedule to set
     */
    public void setEmployeeSchedule(EmployeeSchedule employeeSchedule) {
        this.employeeSchedule = employeeSchedule;
    }

    /**
     * Gets the work date.
     * @return the work date
     */
    public LocalDate getWorkDate() {
        return workDate;
    }

    /**
     * Sets the work date.
     * @param workDate the work date to set
     */
    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    /**
     * Gets the work hours.
     * @return the work hours
     */
    public Float getWorkHours() {
        return workHours;
    }

    /**
     * Sets the work hours.
     * @param workHours the work hours to set
     */
    public void setWorkHours(Float workHours) {
        this.workHours = workHours;
    }
}
