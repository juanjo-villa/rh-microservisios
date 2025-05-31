package com.rh_systems.performance_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for Performance Evaluation request.
 */
public class EvaluationRequest {

    @NotNull(message = "Score cannot be null")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer score;

    /**
     * Default constructor
     */
    public EvaluationRequest() {
    }

    /**
     * Constructor with parameters
     *
     * @param score Evaluation score
     */
    public EvaluationRequest(Integer score) {
        this.score = score;
    }

    /**
     * Gets the evaluation score
     *
     * @return Evaluation score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets the evaluation score
     *
     * @param score Evaluation score to set
     */
    public void setScore(Integer score) {
        this.score = score;
    }
}