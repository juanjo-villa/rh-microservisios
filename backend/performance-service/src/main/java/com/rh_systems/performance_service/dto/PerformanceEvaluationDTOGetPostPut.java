package com.rh_systems.performance_service.dto;

import java.time.ZoneId;
import java.util.Date;

import com.rh_systems.performance_service.Entity.PerformanceEvaluation;

/**
 * Data Transfer Object for getting, posting, and putting performance evaluations.
 */
public class PerformanceEvaluationDTOGetPostPut {
    private Long id;
    private Date date;
    private float score; 
    private String comments;
    private Long employeeId;

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
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the score.
     * @return the score
     */
    public float getScore() {
        return score;
    }

    /**
     * Sets the score.
     * @param score the score to set
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Gets the comments.
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments.
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets the employee ID.
     * @return the employee ID
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Converts a PerformanceEvaluation entity to this DTO.
     * @param performanceEvaluationEntity the entity to convert
     */
    public void convertToPerformanceEvaluationDTO(PerformanceEvaluation performanceEvaluationEntity) {
        this.setId(performanceEvaluationEntity.getId());
        // Convert LocalDate to Date
        if (performanceEvaluationEntity.getEvaluationDate() != null) {
            this.setDate(Date.from(performanceEvaluationEntity.getEvaluationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        this.setScore(performanceEvaluationEntity.getScore());
        this.setComments(performanceEvaluationEntity.getComments());
        this.setEmployeeId(performanceEvaluationEntity.getEmployeeId());
    }
}
