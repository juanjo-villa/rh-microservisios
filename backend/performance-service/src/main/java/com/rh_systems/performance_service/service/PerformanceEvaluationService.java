package com.rh_systems.performance_service.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh_systems.performance_service.Entity.PerformanceEvaluation;
import com.rh_systems.performance_service.client.EmployeeClient;
import com.rh_systems.performance_service.dto.EmployeeDTO;
import com.rh_systems.performance_service.dto.EvaluationRequest;
import com.rh_systems.performance_service.dto.PerformanceEvaluationDTO;
import com.rh_systems.performance_service.dto.PerformanceEvaluationDTOGetPostPut;
import com.rh_systems.performance_service.repository.PerformanceEvaluationRepository;

@Service
public class PerformanceEvaluationService {
    @Autowired
    private PerformanceEvaluationRepository performanceEvaluationRepository;

    @Autowired
    private EmployeeClient employeeClient;

    /**
     * Gets all performance evaluations.
     * @return a list of PerformanceEvaluationDTOGetPostPut
     */
    public List<PerformanceEvaluationDTOGetPostPut> getAllPerformanceEvaluation() {
        List<PerformanceEvaluationDTOGetPostPut> performanceEvaluationToReturn = new ArrayList<>();
        List<PerformanceEvaluation> performanceEvaluations = performanceEvaluationRepository.findAll();
        for (PerformanceEvaluation pe : performanceEvaluations) {
            PerformanceEvaluationDTOGetPostPut performanceEvaluationDTO = new PerformanceEvaluationDTOGetPostPut();
            performanceEvaluationDTO.convertToPerformanceEvaluationDTO(pe);
            performanceEvaluationToReturn.add(performanceEvaluationDTO);
        }
        return performanceEvaluationToReturn;
    }

    /**
     * Gets a performance evaluation by its ID.
     * @param id the performance evaluation ID
     * @return an Optional containing the PerformanceEvaluationDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PerformanceEvaluationDTOGetPostPut> getPerformanceEvaluationById(Long id) {
        Optional<PerformanceEvaluation> performanceEvaluation = performanceEvaluationRepository.findById(id);
        if (performanceEvaluation.isPresent()) {
            PerformanceEvaluationDTOGetPostPut performanceEvaluationDTO = new PerformanceEvaluationDTOGetPostPut();
            performanceEvaluationDTO.convertToPerformanceEvaluationDTO(performanceEvaluation.get());
            return Optional.of(performanceEvaluationDTO);
        }
        return Optional.empty();
    }

    /**
     * Gets a performance evaluation by employee ID and date.
     * @param employeeId the employee ID
     * @param date the evaluation date
     * @return an Optional containing the PerformanceEvaluationDTOGetPostPut if found, or empty otherwise
     */
    public Optional<PerformanceEvaluationDTOGetPostPut> getPerformanceEvaluationByEmployeeIdAndDate(Long employeeId, Date date) {
        Optional<PerformanceEvaluation> performanceEvaluation;

        if (date != null) {
            // Convert Date to LocalDate
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            performanceEvaluation = performanceEvaluationRepository.findByEmployeeIdAndEvaluationDate(employeeId, localDate);

            // If not found with LocalDate, try with Date (legacy support)
            if (!performanceEvaluation.isPresent()) {
                performanceEvaluation = performanceEvaluationRepository.findByEmployeeIdAndDate(employeeId, date);
            }
        } else {
            return Optional.empty();
        }

        if (performanceEvaluation.isPresent()) {
            PerformanceEvaluationDTOGetPostPut performanceEvaluationDTO = new PerformanceEvaluationDTOGetPostPut();
            performanceEvaluationDTO.convertToPerformanceEvaluationDTO(performanceEvaluation.get());
            return Optional.of(performanceEvaluationDTO);
        }
        return Optional.empty();
    }

    /**
     * Creates a new performance evaluation.
     * @param performanceEvaluationDTO the performance evaluation data
     * @return an Optional containing the created PerformanceEvaluationDTOGetPostPut, or empty if a performance evaluation with the same employee and date exists
     */
    public Optional<PerformanceEvaluationDTOGetPostPut> createPerformanceEvaluation(PerformanceEvaluationDTO performanceEvaluationDTO) {
        if (performanceEvaluationDTO.getDate() == null) {
            throw new IllegalArgumentException("Evaluation date cannot be null");
        }

        // Convert Date to LocalDate for checking existing evaluations
        LocalDate localDate = performanceEvaluationDTO.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Check if an evaluation already exists for this employee and date
        if (performanceEvaluationRepository.findByEmployeeIdAndEvaluationDate(performanceEvaluationDTO.getEmployeeId(), localDate).isPresent() ||
            performanceEvaluationRepository.findByEmployeeIdAndDate(performanceEvaluationDTO.getEmployeeId(), performanceEvaluationDTO.getDate()).isPresent()) {
            return Optional.empty();
        }

        // Validate that the employee exists by getting employee data from employee-service
        EmployeeDTO employee;
        try {
            employee = employeeClient.getEmployeeById(performanceEvaluationDTO.getEmployeeId());
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee data: " + e.getMessage());
        }

        if (employee == null) {
            return Optional.empty();
        }

        PerformanceEvaluation performanceEvaluation = new PerformanceEvaluation();
        performanceEvaluation.setEvaluationDate(localDate);
        performanceEvaluation.setComments(performanceEvaluationDTO.getComments());
        performanceEvaluation.setScore((int) performanceEvaluationDTO.getScore());
        performanceEvaluation.setEmployeeId(employee.getId());
        performanceEvaluation.setEmployeeName(employee.getName());

        // Set default evaluator if not provided
        if (performanceEvaluation.getEvaluator() == null) {
            performanceEvaluation.setEvaluator("System");
        }

        try {
            PerformanceEvaluationDTOGetPostPut createdPerformanceEvaluationDTO = new PerformanceEvaluationDTOGetPostPut();
            createdPerformanceEvaluationDTO.convertToPerformanceEvaluationDTO(performanceEvaluationRepository.save(performanceEvaluation));
            return Optional.of(createdPerformanceEvaluationDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error saving performance evaluation: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing performance evaluation.
     * @param id the performance evaluation ID
     * @param performanceEvaluationDTO the performance evaluation data to update
     * @return an Optional containing the updated PerformanceEvaluationDTOGetPostPut, or empty if not found or date conflict
     */
    public Optional<PerformanceEvaluationDTOGetPostPut> updatePerformanceEvaluation(Long id, PerformanceEvaluationDTO performanceEvaluationDTO) {
        if (performanceEvaluationDTO.getDate() == null) {
            throw new IllegalArgumentException("Evaluation date cannot be null");
        }

        Optional<PerformanceEvaluation> performanceEvaluation = performanceEvaluationRepository.findById(id);
        if (performanceEvaluation.isPresent()) {
            // Convert Date to LocalDate for comparison
            LocalDate dtoDate = performanceEvaluationDTO.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Check if date has changed and if there's already an evaluation for the new date
            if (dtoDate != null && !performanceEvaluation.get().getEvaluationDate().equals(dtoDate)) {
                if (performanceEvaluationRepository.findByEmployeeIdAndEvaluationDate(performanceEvaluationDTO.getEmployeeId(), dtoDate).isPresent() ||
                    performanceEvaluationRepository.findByEmployeeIdAndDate(performanceEvaluationDTO.getEmployeeId(), performanceEvaluationDTO.getDate()).isPresent()) {
                    return Optional.empty();
                }
            }

            PerformanceEvaluation updatedPerformanceEvaluation = performanceEvaluation.get();
            updatedPerformanceEvaluation.setEvaluationDate(dtoDate);
            updatedPerformanceEvaluation.setComments(performanceEvaluationDTO.getComments());
            updatedPerformanceEvaluation.setScore((int) performanceEvaluationDTO.getScore());

            // Try to get updated employee info if employeeId has changed
            if (!updatedPerformanceEvaluation.getEmployeeId().equals(performanceEvaluationDTO.getEmployeeId())) {
                try {
                    EmployeeDTO employee = employeeClient.getEmployeeById(performanceEvaluationDTO.getEmployeeId());
                    if (employee != null) {
                        updatedPerformanceEvaluation.setEmployeeId(employee.getId());
                        updatedPerformanceEvaluation.setEmployeeName(employee.getName());
                    } else {
                        throw new RuntimeException("Employee not found with ID: " + performanceEvaluationDTO.getEmployeeId());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error getting employee data: " + e.getMessage(), e);
                }
            }

            try {
                PerformanceEvaluationDTOGetPostPut updatedPerformanceEvaluationDTO = new PerformanceEvaluationDTOGetPostPut();
                updatedPerformanceEvaluationDTO.convertToPerformanceEvaluationDTO(performanceEvaluationRepository.save(updatedPerformanceEvaluation));
                return Optional.of(updatedPerformanceEvaluationDTO);
            } catch (Exception e) {
                throw new RuntimeException("Error updating performance evaluation: " + e.getMessage(), e);
            }
        }
        return Optional.empty();
    }

    /**
     * Deletes a performance evaluation by its ID.
     * @param id the performance evaluation ID
     * @return true if the performance evaluation was deleted, false otherwise
     */
    public boolean deletePerformanceEvaluation(Long id) {
        if (performanceEvaluationRepository.findById(id).isPresent()) {
            performanceEvaluationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Creates a new performance evaluation with data obtained from the employee service.
     * @param employeeId the employee ID
     * @param request the evaluation request containing the score
     * @return the created performance evaluation
     */
    public PerformanceEvaluation createEvaluation(Long employeeId, EvaluationRequest request) {
        try {
            EmployeeDTO employee = employeeClient.getEmployeeById(employeeId);
            if (employee == null) {
                throw new RuntimeException("Employee not found with ID: " + employeeId);
            }

            PerformanceEvaluation evaluation = new PerformanceEvaluation();
            evaluation.setEmployeeId(employee.getId());
            evaluation.setEmployeeName(employee.getName());
            evaluation.setScore(request.getScore());
            evaluation.setEvaluationDate(LocalDate.now());
            evaluation.setEvaluator("System"); // Default evaluator

            return performanceEvaluationRepository.save(evaluation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating evaluation: " + e.getMessage(), e);
        }
    }

    /**
     * Gets all performance evaluations for a specific employee.
     * @param employeeId the employee ID
     * @return a list of PerformanceEvaluationDTOGetPostPut
     */
    public List<PerformanceEvaluationDTOGetPostPut> getPerformanceEvaluationsByEmployeeId(Long employeeId) {
        List<PerformanceEvaluationDTOGetPostPut> performanceEvaluationToReturn = new ArrayList<>();
        List<PerformanceEvaluation> performanceEvaluations = performanceEvaluationRepository.findByEmployeeId(employeeId);
        for (PerformanceEvaluation pe : performanceEvaluations) {
            PerformanceEvaluationDTOGetPostPut performanceEvaluationDTO = new PerformanceEvaluationDTOGetPostPut();
            performanceEvaluationDTO.convertToPerformanceEvaluationDTO(pe);
            performanceEvaluationToReturn.add(performanceEvaluationDTO);
        }
        return performanceEvaluationToReturn;
    }
}
