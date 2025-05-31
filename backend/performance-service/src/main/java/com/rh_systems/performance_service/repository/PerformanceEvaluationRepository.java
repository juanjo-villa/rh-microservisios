package com.rh_systems.performance_service.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rh_systems.performance_service.Entity.PerformanceEvaluation;

/**
 * Repository interface for PerformanceEvaluation entity.
 */
@Repository
public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Long> {
    /**
     * Finds a PerformanceEvaluation by employee ID and date.
     * @param employeeId the employee ID
     * @param date the evaluation date
     * @return an Optional containing the PerformanceEvaluation if found, or empty otherwise
     */
    @Query("SELECT pe FROM PerformanceEvaluation pe WHERE pe.employeeId = :employeeId AND pe.evaluationDate = :date")
    Optional<PerformanceEvaluation> findByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") Date date);

    /**
     * Finds a PerformanceEvaluation by employee ID and evaluation date.
     * @param employeeId the employee ID
     * @param evaluationDate the evaluation date
     * @return an Optional containing the PerformanceEvaluation if found, or empty otherwise
     */
    Optional<PerformanceEvaluation> findByEmployeeIdAndEvaluationDate(Long employeeId, LocalDate evaluationDate);

    /**
     * Finds all PerformanceEvaluations by employee ID.
     * @param employeeId the employee ID
     * @return a list of PerformanceEvaluations
     */
    List<PerformanceEvaluation> findByEmployeeId(Long employeeId);

    /**
     * Finds all PerformanceEvaluations by evaluation date.
     * @param evaluationDate the evaluation date
     * @return a list of PerformanceEvaluations
     */
    List<PerformanceEvaluation> findByEvaluationDate(LocalDate evaluationDate);

    /**
     * Finds all PerformanceEvaluations by date (legacy support).
     * @param date the evaluation date
     * @return a list of PerformanceEvaluations
     */
    @Query("SELECT pe FROM PerformanceEvaluation pe WHERE pe.evaluationDate = :date")
    List<PerformanceEvaluation> findByDate(@Param("date") Date date);
}
