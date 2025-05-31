package com.rh_systems.performance_service.dto;

import java.util.Date;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for performance evaluation.
 */
public class PerformanceEvaluationDTO {
    @NotNull(message = "Score cannot be null")
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 5, message = "Score must be at most 5")
    private float score;

    @Size(max = 250, message = "Comments must be less than 250 characters")
    private String comments;

    @NotNull(message = "Date cannot be null")
    private Date date;

    @NotNull(message = "Employee ID cannot be null")
    @Min(value = 1, message = "Employee ID must be at least 1")
    private Long employeeId;

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
}
