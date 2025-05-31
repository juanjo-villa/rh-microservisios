package com.rh_systems.schedule_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh_systems.schedule_service.Entity.CountEmployeeSchedule;

/**
 * Repository interface for CountEmployeeSchedule entity.
 */
@Repository
public interface CountEmployeeScheduleRepository extends JpaRepository<CountEmployeeSchedule, Long> {
    /**
     * Finds a CountEmployeeSchedule by work hours.
     * @param workHours the work hours
     * @return an Optional containing the CountEmployeeSchedule if found, or empty otherwise
     */
    Optional<CountEmployeeSchedule> findByWorkHours(Float workHours);

    /**
     * Finds a CountEmployeeSchedule by employee ID.
     * @param employeeId the employee ID
     * @return an Optional containing the CountEmployeeSchedule if found, or empty otherwise
     */
    @Query("SELECT ces FROM CountEmployeeSchedule ces WHERE ces.employeeSchedule.employeeId = :employeeId")
    Optional<CountEmployeeSchedule> findByEmployeeId(@Param("employeeId") Long employeeId);
}
