package com.rh_systems.schedule_service.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import com.rh_systems.schedule_service.Entity.Schedule;

/**
 * Repository interface for Schedule entity.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    /**
     * Finds a Schedule by its date.
     * @param date the schedule date
     * @return an Optional containing the Schedule if found, or empty otherwise
     */
    Optional<Schedule> findByDate(LocalDate date);
}
