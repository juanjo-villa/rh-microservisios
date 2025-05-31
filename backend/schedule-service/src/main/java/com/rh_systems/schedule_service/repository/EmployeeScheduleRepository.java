package com.rh_systems.schedule_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh_systems.schedule_service.Entity.EmployeeSchedule;

/**
 * Repository interface for EmployeeSchedule entity.
 */
@Repository
public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, Long> {
    /**
     * Finds an EmployeeSchedule by employee ID and schedule ID.
     * @param employeeId the employee ID
     * @param scheduleId the schedule ID
     * @return an Optional containing the EmployeeSchedule if found, or empty otherwise
     */
    Optional<EmployeeSchedule> findByEmployeeIdAndScheduleId(Long employeeId, Long scheduleId);

    /**
     * Finds all EmployeeSchedules by employee ID.
     * @param employeeId the employee ID
     * @return a list of EmployeeSchedules
     */
    List<EmployeeSchedule> findByEmployeeId(Long employeeId);

    /**
     * Finds all EmployeeSchedules by schedule ID.
     * @param scheduleId the schedule ID
     * @return a list of EmployeeSchedules
     */
    List<EmployeeSchedule> findByScheduleId(Long scheduleId);
}
