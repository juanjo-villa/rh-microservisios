package com.rh_systems.performance_service.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;

/**
 * Entity representing a performance evaluation.
 */
@Entity
@Table(name = "performance_evaluation")
public class PerformanceEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate evaluationDate;
    private int score;
    private String comments;
    private Long employeeId;
    private String employeeName;
    private String evaluator;

    /**
     * Default constructor.
     */
    public PerformanceEvaluation() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id             the ID
     * @param evaluationDate the evaluation date
     * @param score          the score
     * @param comments       the comments
     * @param employeeId     the employee ID
     * @param employeeName   the employee name
     * @param evaluator      the evaluator
     */
    public PerformanceEvaluation(Long id, LocalDate evaluationDate, int score, String comments, Long employeeId, String employeeName, String evaluator) {
        this.id = id;
        this.evaluationDate = evaluationDate;
        this.score = score;
        this.comments = comments;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.evaluator = evaluator;
    }

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
     * Gets the evaluation date.
     * @return the evaluation date
     */
    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    /**
     * Sets the evaluation date.
     * @param evaluationDate the evaluation date to set
     */
    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    /**
     * Gets the score.
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score.
     * @param score the score to set
     */
    public void setScore(int score) {
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
     *
     * @return the employee ID
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID.
     *
     * @param employeeId the employee ID to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets the employee name.
     *
     * @return the employee name
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets the employee name.
     *
     * @param employeeName the employee name to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Gets the evaluator.
     *
     * @return the evaluator
     */
    public String getEvaluator() {
        return evaluator;
    }

    /**
     * Sets the evaluator.
     *
     * @param evaluator the evaluator to set
     */
    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }
}
